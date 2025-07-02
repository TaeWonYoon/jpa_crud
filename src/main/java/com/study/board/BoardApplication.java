package com.study.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.study")
@EntityScan(basePackages = {"com.study.board.entity", "com.study.User.entity", "com.study.file.entity", "com.study.admin.introduction.entity"})
@EnableJpaRepositories(basePackages = "com.study")
public class BoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardApplication.class, args);
	}

}
