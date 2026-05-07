package com.daemin.clean_talk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CleanTalkApplication {

	public static void main(String[] args) {
		SpringApplication.run(CleanTalkApplication.class, args);
	}

}
