package com.rbank.customer.statement.validation.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

/**
 * @author Michael Philomin Raj
 *
 */

@Configuration
public class RestTemplateAutoConfiguration {

	@Bean
	@Primary
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}



}