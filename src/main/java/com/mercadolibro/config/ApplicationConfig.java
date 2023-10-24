package com.mercadolibro.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mercadolibro.service.TestService;
import com.mercadolibro.service.impl.TestServiceImpl;

@Configuration
public class ApplicationConfig {
	public TestService initTestServiceImpl() {
		return new TestServiceImpl();
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
