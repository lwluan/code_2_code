package com.cd2cd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class AppMainStarter {
	private static Logger LOG = LoggerFactory.getLogger(AppMainStarter.class);
	
	@GetMapping("/helloWorld")
    public String helloWorld() {
        return "helloWorld";
    }
	
	public static void main(String[] args) {
		LOG.info("App to Start ...");
		SpringApplication.run(AppMainStarter.class, args);
		
	}

}
