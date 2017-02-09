package pl.jcw.demo.spring.cache;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestContext.class)
public class DemoSpringCacheApplicationTests {

	@After
	public void tearDown() {
		service.getCallCount().clear();
		for (String cacheName : cacheManager.getCacheNames()) {
			cacheManager.getCache(cacheName).clear();
		}
	}

	@Autowired
	private GreetingService service;

	@Autowired
	private CacheManager cacheManager;

	@Test
	public void contextLoads() {
		assertThat(service).isNotNull();
		assertThat(cacheManager).isNotNull();
	}

	@Test
	public void shouldCallMethodOnlyOnce() {
		// given
		String name = "Bob";

		// when
		service.greeting("Bob");
		service.greeting("Bob");

		// then
		assertThat(service.getCallCount()).containsOnly(entry(name, 1));
	}

	@Test
	public void shouldCallTwoArgumentMethodOnlyOnce() {
		// given

		// when
		service.greeting("Bob", "Smith");
		service.greeting("Bob", "Smith");

		// then
		assertThat(service.getCallCount()).containsOnly(entry("Bob Smith", 1));
	}

	@Test
	public void shouldUsePrefilledCacheValues() {
		// given
		cacheManager.getCache(GreetingService.GREETING_CACHE_NAME).put("Anna", "Hello Kowalska");

		// when
		String annaGreeting = service.greeting("Anna");
		String tomGreeting = service.greeting("Tom");
		tomGreeting = service.greeting("Tom");

		// then
		assertThat(service.getCallCount()).containsOnly(entry("Tom", 1));
		assertThat(annaGreeting).as("precached value - method should not be called").isEqualTo("Hello Kowalska");
		assertThat(tomGreeting).as("auto cached value - single call").isEqualTo("Hello Tom");
	}

	@Test
	public void shouldUsePrefilledCacheValuesInTwoArgumentMethod() {
		// given
		Cache cache = cacheManager.getCache(GreetingService.GREETING_CACHE_NAME_TWO_ARG);
		cache.put(new SimpleKey("Anna", "Smith"), "Bonjour Anna Smith");

		// when
		String annaGreeting = service.greeting("Anna", "Smith");
		String tomGreeting = service.greeting("Tom", "Smith");
		tomGreeting = service.greeting("Tom", "Smith");

		// then
		assertThat(service.getCallCount()).containsOnly(entry("Tom Smith", 1));
		assertThat(annaGreeting).as("precached value - method should not be called").isEqualTo("Bonjour Anna Smith");
		assertThat(tomGreeting).as("auto cached value - single call").isEqualTo("Hello Tom Smith");
	}

}
