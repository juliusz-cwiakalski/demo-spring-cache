package pl.jcw.demo.spring.cache;

import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class GreetingService {

	public static final String GREETING_CACHE_NAME = "pl.jcw.demo.spring.cache.CachedService.greeting(String)";
	public static final String GREETING_CACHE_NAME_TWO_ARG = "pl.jcw.demo.spring.cache.CachedService.greeting(String,String)";
	private Map<String, Integer> callCount = new HashMap<>();

	@Cacheable(GREETING_CACHE_NAME)
	public String greeting(String name) {
		increaseCounter(name);
		return "Hello " + name;
	}

	@Cacheable(GREETING_CACHE_NAME_TWO_ARG)
	public String greeting(String fistName, String lastName) {
		String name = fistName + " " + lastName;
		increaseCounter(name);
		return "Hello " + name;
	}

	private void increaseCounter(String in) {
		Integer counter = getCallCount().get(in);
		if (counter == null) {
			counter = 1;
		} else {
			counter = counter + 1;
		}
		getCallCount().put(in, counter);
	}

	public Map<String, Integer> getCallCount() {
		return callCount;
	}

}
