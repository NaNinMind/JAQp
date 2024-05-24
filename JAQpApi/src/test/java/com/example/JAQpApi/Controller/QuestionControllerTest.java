package com.example.JAQpApi.Controller;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.sql.Timestamp;
import java.util.List;

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
import org.testcontainers.utility.DockerImageName;

import com.example.JAQpApi.DTO.AuthenticationRequest;
import com.example.JAQpApi.DTO.QuestionCreateRequest;
import com.example.JAQpApi.DTO.QuizCreateRequest;
import com.example.JAQpApi.DTO.RegistrationRequest;
import com.example.JAQpApi.Entity.Quiz.Question;
import com.example.JAQpApi.Entity.Quiz.Quiz;
import com.example.JAQpApi.Entity.User.Role;
import com.example.JAQpApi.Entity.User.User;
import com.example.JAQpApi.Repository.QuestionRepo;
import com.example.JAQpApi.Repository.QuizRepo;
import com.example.JAQpApi.Repository.TagRepo;
import com.example.JAQpApi.Repository.TokenRepo;
import com.example.JAQpApi.Repository.UserRepo;
import com.example.JAQpApi.Service.AuthService;
import com.example.JAQpApi.Service.JWTService;
import com.example.JAQpApi.Service.QuizService;
import com.redis.testcontainers.RedisContainer;

import io.restassured.RestAssured;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QuestionControllerTest {



    @LocalServerPort
    private Integer port;

    public static Logger log = LoggerFactory.getLogger(UserControllerTest.class);

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
      "postgres:15-alpine"
    );
    static RedisContainer REDIS_CONTAINER =  new RedisContainer(DockerImageName.parse("ghcr.io/microsoft/garnet:1.0.7"))
                                                    .withExposedPorts(6379);
  
    int userId;
    String token;
    int quizId;

    @Autowired UserRepo userRepo;
    @Autowired QuizRepo quizRepo;
    @Autowired QuizService quizService;
    @Autowired AuthService authService;
    @Autowired JWTService jwtService;
    @Autowired TokenRepo tokenRepo;
    @Autowired QuestionRepo questionRepo;
    @Autowired TagRepo tagRepo;

    @BeforeAll
    static void beforeAll() {
      postgres.start();
      REDIS_CONTAINER.start();
    }

    @AfterAll
    static void afterAll() {
      postgres.stop();
      REDIS_CONTAINER.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
      registry.add("spring.datasource.url", postgres::getJdbcUrl);
      registry.add("spring.datasource.username", postgres::getUsername);
      registry.add("spring.datasource.password", postgres::getPassword);

      registry.add("spring.redis.host", REDIS_CONTAINER::getHost);
      registry.add("spring.redis.port", () -> REDIS_CONTAINER.getMappedPort(6379).toString());
      //registry.add("spring.cache.type", () -> "redis");
      //registry.add("spring.cache.cache-names", () -> "redis-cache");
      
      registry.add("minio.url", () -> "http://localhost:9000");
      registry.add("minio.accessKey", () -> "minio");
      registry.add("minio.secretKey", () -> "miniominio");
    }

    @BeforeEach
    void setUp() throws Exception {
      RestAssured.baseURI = "http://localhost:" + port;
      User user = User.builder()
          .username("username1")
          .password("password1")
          .role(Role.ADMIN)
          .createdAt(new Timestamp(System.currentTimeMillis()))
          .build();
      Quiz quiz = Quiz.builder()
          .name("quizName")
          .description("quizDescription")
          .build();

      authService.register(new RegistrationRequest(user.getUsername(), user.getPassword(), user.getRole()));
      var res = authService.authenticate(new AuthenticationRequest("username1", "password1"));
      token = "Bearer " + res.getJwtToken();
      userId = res.getId();
      quizId = quizService.CreateQuiz(token, new QuizCreateRequest("quizName", null, "quizDescription", List.of("tag"))).getId();
    }

    @AfterEach
    void tearDown(){
      questionRepo.deleteAll();
      quizRepo.deleteAll();
      tokenRepo.deleteAll();
      userRepo.deleteAll();
      
    }

    @Test
    void testGetQuestion_OK() {
      Quiz quiz = quizRepo.findById(quizId).get();
      Question question = Question.builder()
                                  .quiz(quiz)
                                  .description("question description")
                                  .build();
      questionRepo.save(question);


      given()
        .log().all()
      .when()
        .get("/api/question/{d}", 1)
      .then()
        .statusCode(200)
        .body("id", is(1))
        .body("description", is("question description"))
        .log().all();

    }

    @Test
    void testCreateQuestion() {
      Quiz quiz = quizRepo.findById(quizId).get();
      Question question = Question.builder()
                                  .quiz(quiz)
                                  .description("question description")
                                  .build();
      
      QuestionCreateRequest request = QuestionCreateRequest.builder()
                            .quiz_id(quizId)
                            .content("question content")
                            .build();


      given()
        .header("Authorization", token)
        .param("quiz_id", request.getQuiz_id())
        .param("content", request.getContent())
        .log().all()
      .when()
        .post("/api/question/add")
      .then()
        .log().all()
        .statusCode(200);


      
        question = questionRepo.findById(1).get();
        assertEquals(question.getDescription(), "question description");
    }

    @Test
    void testChangeQuestionWOImage() {
      Quiz quiz = quizRepo.findById(quizId).get();
      Question question = Question.builder()
                                  .quiz(quiz)
                                  .description("question description")
                                  .build();
      questionRepo.save(question);


      given()
        .log().all()
        .header("Authorization", token)
        .param("description", "new question description")
      .when()
        .put("/api/question/change_wo_image/{id}", quizId)
      .then()
        .statusCode(200);

      assertEquals(questionRepo.findById(question.getId()).get().getDescription(), "new question description");
      
    }



    @Test
    void testRemove() {
      Quiz quiz = quizRepo.findById(quizId).get();
      Question question = Question.builder()
                                  .quiz(quiz)
                                  .description("question description")
                                  .build();
      questionRepo.save(question);


      given()
        .log().all()
        .header("Authorization", token)
        .param("description", "new question description")
      .when()
        .delete("/api/question/remove/{id}", quizId)
      .then()
        .statusCode(200);

      assertFalse(questionRepo.findById(question.getId()).isPresent());
    }

    @Test
    void testChangeQuestion() {
      
    }


}
