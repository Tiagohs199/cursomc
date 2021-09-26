package com.tiago.cursomc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.tiago.cursomc.services.DbService;

@Configuration
@Profile("test")
public class TestConfig {
	
	@Autowired
	private DbService db;
	
	@Bean
	public boolean instantiateDatabase() throws Exception {
		
		db.instantiateTestDatabase();
		
		return true;
	}

}