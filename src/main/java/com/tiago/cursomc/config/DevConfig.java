package com.tiago.cursomc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.tiago.cursomc.services.DbService;
import com.tiago.cursomc.services.EmailService;
import com.tiago.cursomc.services.SmtpEmailService;

@Configuration
@Profile("dev")
public class DevConfig {
	
	@Autowired
	private DbService db;
	
	@Value("$spring.jpa.hibernate.ddl.auto")
	private String strategy;
	
	@Bean
	public boolean instantiateDatabase() throws Exception {
		
		if ( "create".equals(strategy)) {
			return false;
		}
		
		db.instantiateTestDatabase();
		
		return true;
	}
	
	@Bean
	public EmailService emailService() {
		return new SmtpEmailService();
	}

}
