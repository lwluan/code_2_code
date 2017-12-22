package com.cd2cd.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class AppMainStarter extends WebMvcConfigurerAdapter {
	
	private static Logger LOG = LoggerFactory.getLogger(AppMainStarter.class);
	
	public static void main(String[] args) {
		
		LOG.info("App to Start ...");
		SpringApplication.run(AppMainStarter.class, args);
		
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addRedirectViewController("/", "/html/index.html");
	}
}
