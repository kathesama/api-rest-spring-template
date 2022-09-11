package com.kathesama.apirestspringtemplate.apirestspringtemplate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableConfigurationProperties
@EnableMongoAuditing
@EnableMongoRepositories
@SpringBootApplication()
public class ApiRestSpringTemplateApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiRestSpringTemplateApplication.class, args);
	}

}
