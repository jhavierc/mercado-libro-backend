package com.mercadolibro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mercadolibro.service.TestService;
import com.mercadolibro.service.impl.TestServiceImpl;

@Configuration
public class AplicationConfig {
	
	@Bean
	public TestService initTestServiceImpl() {
		return new TestServiceImpl();
	}


}
