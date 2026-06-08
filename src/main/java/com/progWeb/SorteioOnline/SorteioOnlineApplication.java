package com.progWeb.SorteioOnline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SorteioOnlineApplication {

	public static void main(String[] args) {
		SpringApplication.run(SorteioOnlineApplication.class, args);
	}

}
