package com.example.JAQpApi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.JAQpApi.DTO.AuthenticationRequest;
import com.example.JAQpApi.DTO.RegistrationRequest;
import com.example.JAQpApi.Entity.JsonUtil;
import com.example.JAQpApi.Entity.Quiz.Question;
import com.example.JAQpApi.Entity.Quiz.Quiz;
import com.example.JAQpApi.Entity.User.Role;
import com.example.JAQpApi.Entity.User.User;
import com.example.JAQpApi.Repository.UserRepo;
import com.example.JAQpApi.Service.AuthService;
import com.example.JAQpApi.Service.QuizService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class JaQpApiApplication
{
	private static final Logger logger = LoggerFactory.getLogger(JaQpApiApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(JaQpApiApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(	AuthService service, QuizService quizService ) {
		return args -> {
			
			// var admin = RegistrationRequest.builder()
			// 		.username("admin")
			// 		.password("admin")
			// 		.role(Role.ADMIN)
			// 		.build();
			// service.register(admin);
			 
			AuthenticationRequest adminAuth = AuthenticationRequest.builder()
					.username("admin")
					.password("admin")
					.build();
			System.out.println("Admin token: " + service.authenticate(adminAuth).getJwtToken());

			Question q = new Question().builder()
			                           .answerNum(1)
									   .text("qwe??")
									   .answers(Arrays.asList("q", "w", "e"))
									   .build();
			Quiz quiz = new Quiz().builder().name("quiz with questions").questions(JsonUtil.convertObjectToJson(q)).build();
			quizService.addQuiz("admin", quiz);

			quiz.setName("quiz with questions, but updated");
			quizService.updateQuiz("admin", quiz, 1);
		};
	}
}
