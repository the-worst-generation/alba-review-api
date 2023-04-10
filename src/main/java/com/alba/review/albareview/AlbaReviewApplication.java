package com.alba.review.albareview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AlbaReviewApplication {
	public static void main(String[] args) {
		SpringApplication.run(AlbaReviewApplication.class, args);
	}

}
