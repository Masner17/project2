package com.masner.project2;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootTest
class Project2ApplicationTests {

	@Test
	void contextLoads() {
	}

	@EnableScheduling
	@SpringBootApplication
	public class Project2Application{
		public static void main(String[] args){
			SpringApplication.run(Project2Application.class, args);
		}
	}

}
