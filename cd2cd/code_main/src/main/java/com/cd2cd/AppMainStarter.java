package com.cd2cd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class AppMainStarter extends WebMvcConfigurerAdapter {
	
	private static Logger LOG = LoggerFactory.getLogger(AppMainStarter.class);

	/**
	 * 垮域
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*").allowCredentials(true)
				.allowedMethods("GET", "POST", "DELETE", "PUT", "OPTIONS").maxAge(3600);
	}
	
	public static void main(String[] args) {
		
		LOG.info("App to Start ...");
		SpringApplication.run(AppMainStarter.class, args);
		
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addRedirectViewController("/", "/html/index.html");
	}
}
