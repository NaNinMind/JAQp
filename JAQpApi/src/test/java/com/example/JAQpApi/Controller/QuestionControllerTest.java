package com.example.JAQpApi.Controller;

import java.sql.Timestamp;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import com.example.JAQpApi.DTO.AuthenticationRequest;
import com.example.JAQpApi.DTO.RegistrationRequest;
import com.example.JAQpApi.Entity.User.Role;
import com.example.JAQpApi.Entity.User.User;
import com.example.JAQpApi.Repository.QuestionRepo;
import com.example.JAQpApi.Repository.QuizRepo;
import com.example.JAQpApi.Repository.TokenRepo;
import com.example.JAQpApi.Repository.UserRepo;
import com.example.JAQpApi.Service.AuthService;
import com.example.JAQpApi.Service.JWTService;
import com.example.JAQpApi.Service.QuizService;

import io.restassured.RestAssured;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QuestionControllerTest {



    @LocalServerPort
    private Integer port;

    public static Logger log = LoggerFactory.getLogger(UserControllerTest.class);

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
      "postgres:15-alpine"
    );

    User user;
    String token;

    @Autowired
    UserRepo userRepo;

    @Autowired
    QuizRepo quizRepo;

    @Autowired
    QuizService quizService;

    @Autowired
    AuthService authService;

    @Autowired
    JWTService jwtService;

    @Autowired
    TokenRepo tokenRepo;

    @Autowired
    QuestionRepo questionRepo;

    @BeforeAll
    static void beforeAll() {
      postgres.start();
    }

    @AfterAll
    static void afterAll() {
      postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
      registry.add("spring.datasource.url", postgres::getJdbcUrl);
      registry.add("spring.datasource.username", postgres::getUsername);
      registry.add("spring.datasource.password", postgres::getPassword);
      registry.add("minio.url", () -> "http://localhost:9000");
      registry.add("minio.accessKey", () -> "minio");
      registry.add("minio.secretKey", () -> "miniominio");
    }

    @BeforeEach
    void setUp() throws Exception {
      RestAssured.baseURI = "http://localhost:" + port;
      user = User.builder()
          .username("username1")
          .password("password1")
          .role(Role.ADMIN)
          .createdAt(new Timestamp(System.currentTimeMillis()))
          .build();
      authService.register(new RegistrationRequest(user.getUsername(), user.getPassword(), user.getRole()));
      token = authService.authenticate(new AuthenticationRequest("username1", "password1")).getJwtToken();
    }

    @AfterEach
    void tearDown(){
      quizRepo.deleteAll();
      tokenRepo.deleteAll();
      userRepo.deleteAll();
      
    }



    @Test
    void testChangeQuestion() {

    }

    @Test
    void testChangeQuestionWOImage() {

    }

    @Test
    void testCreateQuestion() {

    }

    @Test
    void testRemove() {

    }

    @Test
    void testGetQuestion() {

    }
}
