package com.cd2cd;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

import com.cd2cd.util.DatabaseInitDataUtil;

@RestController
@SpringBootApplication
public class AppMainStarter {
	private static Logger LOG = LoggerFactory.getLogger(AppMainStarter.class);
	
	@Autowired
	DatabaseInitDataUtil databaseInitDataUtil;
	
	@PostConstruct
    public void init() throws FileNotFoundException, SQLException, IOException {
		databaseInitDataUtil.initDatabase();
    }
	
	public static void main(String[] args) {
		LOG.info("App to Start ...");
		SpringApplication.run(AppMainStarter.class, args);
		
	}

}
