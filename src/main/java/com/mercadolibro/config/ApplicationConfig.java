package com.mercadolibro.config;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.mercadolibro.service.TestService;
import com.mercadolibro.service.impl.TestServiceImpl;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilderFactory;

@Configuration
public class ApplicationConfig {
	@Bean
	public TestService initTestServiceImpl() {
		return new TestServiceImpl();
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setUriTemplateHandler(uriBuilderFactory());
		return restTemplate;
	}

	@Bean
	public UriBuilderFactory uriBuilderFactory() {
		DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
		factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
		return factory;
	}

	@Bean
	public Gson gson() {
		return new Gson();
	}
}
