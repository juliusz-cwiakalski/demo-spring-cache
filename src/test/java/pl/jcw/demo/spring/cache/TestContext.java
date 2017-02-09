package pl.jcw.demo.spring.cache;

import java.util.Collections;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("pl.jcw.demo.spring.cache")
@EnableCaching
public class TestContext {
	@Bean
	public CacheManager cacheManager() {
		SimpleCacheManager cacheManager = new SimpleCacheManager(){
			@Override
			protected Cache getMissingCache(String name) {
				return new ConcurrentMapCache(name);
			}
		};
		cacheManager.setCaches(Collections.emptyList());
		return cacheManager;
	}
}
