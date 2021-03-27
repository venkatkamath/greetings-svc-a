package com.example.restservice;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Metrics;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.apache.commons.lang3.time.StopWatch;

import static org.apache.http.conn.params.ConnManagerPNames.MAX_TOTAL_CONNECTIONS;


@RestController
@Configuration
public class GreetingController {
	private static Logger log = LoggerFactory.getLogger(GreetingController.class);

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@Autowired
	private RestTemplate restTemplate;



	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name,
							 @RequestParam(value = "sleepA", defaultValue = "0") long sleepA,
							 @RequestParam(value = "sleepB", defaultValue = "0") long sleepB) {
		log.info("greeting enter");
		try {
			Thread.sleep(sleepA);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		processGreetingPrint();
		restTemplate.getForEntity("http://localhost:8081/greeting?sleep="+sleepB,Object.class);
		log.info("greeting exit");
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}

	private void processGreetingPrint() {
		//start
		StopWatch stopwatch = StopWatch.createStarted();

		try {
			Thread.sleep(System.currentTimeMillis()%10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		stopwatch.stop();
		Metrics.timer("printTime",
				"className", this.getClass().getSimpleName(), //class
				"MethodName", "process") // method
				.record(stopwatch.getTime(), TimeUnit.NANOSECONDS);
	}

	@Bean
	public PoolingHttpClientConnectionManager poolingConnectionManager() {
		PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager();
		// set a total amount of connections across all HTTP routes
		poolingConnectionManager.setMaxTotal(200);
		// set a maximum amount of connections for each HTTP route in pool
		poolingConnectionManager.setDefaultMaxPerRoute(100);
		return poolingConnectionManager;
	}

	@Bean
	public RestTemplate restTemplate(HttpClient httpClient) {
		HttpComponentsClientHttpRequestFactory httpRequestFactory = new
				HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setHttpClient(httpClient);
		RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
		return restTemplate;
	}



}







