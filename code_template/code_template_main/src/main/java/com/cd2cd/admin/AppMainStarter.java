package com.cd2cd.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppMainStarter {
	
	private static Logger LOG = LoggerFactory.getLogger(AppMainStarter.class);
	
	public static void main(String[] args) {
		
		LOG.info("App to Start ...");
		SpringApplication.run(AppMainStarter.class, args);
		
	}
}
