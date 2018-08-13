package com.bridgelabz.usermicroservice.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.bridgelabz.usermicroservice.interceptor.LoggerInterceptor;


@Configuration
public class WebConfig implements WebMvcConfigurer{
	@Override
    public void addInterceptors(InterceptorRegistry registry) {   
        registry.addInterceptor(new LoggerInterceptor()).addPathPatterns("/user/**");
        
    }
}
