package com.eden.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.eden.interceptor.LoginCheckInterceptor;


@Configuration
public class MvcConfig implements WebMvcConfigurer{
	

	@Autowired
	private LoginCheckInterceptor loginCheckInterceptor;

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("register").setViewName("regist");
		registry.addViewController("login").setViewName("login");
		registry.addViewController("emplist").setViewName("emplist");
		registry.addViewController("addEmp").setViewName("addEmp");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		registry.addInterceptor(loginCheckInterceptor).addPathPatterns("/**").excludePathPatterns("/css","/img","/js");
		registry.addInterceptor(loginCheckInterceptor).addPathPatterns("/**").excludePathPatterns("/**/.");
	}
	
	

}
