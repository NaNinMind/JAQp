package com.example.JAQpApi.Controller;

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
public class AuthControllerTest {


    @LocalServerPort
    private Integer port;

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

    public static Logger log = LoggerFactory.getLogger(UserControllerTest.class);

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
      "postgres:15-alpine"
    );

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
    void setUp() {
      RestAssured.baseURI = "http://localhost:" + port;
      userRepo.deleteAll();
    }

    @AfterEach
    void tearDown(){
      quizRepo.deleteAll();
      tokenRepo.deleteAll();
      userRepo.deleteAll();
      
    }


    @Test
    void testAuthenticate_OK() {

    }
    @Test
    void testAuthenticate_notFound() {

    }
    @Test
    void testAuthenticate_badCredentials() {

    }

    @Test
    void testRegister_OK() {

    }
    @Test
    void testRegister_badCredentials() {

    }
    @Test
    void testRegister_alreadyExists() {

    }


    
}
