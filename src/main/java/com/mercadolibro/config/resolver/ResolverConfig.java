package com.mercadolibro.config.resolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class ResolverConfig implements WebMvcConfigurer {
    private final BookArgumentResolver bookArgumentResolver;

    @Autowired
    public ResolverConfig(BookArgumentResolver bookArgumentResolver) {
        this.bookArgumentResolver = bookArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(bookArgumentResolver);
    }
}