package com.questionPro.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class QuestionProTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuestionProTaskApplication.class, args);
	}

}
