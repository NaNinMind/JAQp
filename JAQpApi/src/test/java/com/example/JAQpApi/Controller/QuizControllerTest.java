package com.example.JAQpApi.Controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

import com.example.JAQpApi.DTO.AuthenticationRequest;
import com.example.JAQpApi.DTO.QuizChangeRequest;
import com.example.JAQpApi.DTO.RegistrationRequest;
import com.example.JAQpApi.Entity.Quiz.Question;
import com.example.JAQpApi.Entity.Quiz.Quiz;
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
public class QuizControllerTest {

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
    //-----------------------------
    @Test
    void testCreateQuiz() throws Exception {

      int id = given()
        .param("name", "q1")
        .header("Authorization", "Bearer " + token)
        .log().all()
      .when()
        .post("/api/quiz/create")
      .then()
        .statusCode(200)
        .body("name", equalTo("q1"))
      .extract()
        .path("id");
      
      assertEquals(quizRepo.findById(id).get().getName(), "q1");
      
    
    }

    @Test
    void testChangeQuiz() throws Exception {
      
      int id = given()
        .param("name", "q1")
        .header("Authorization", "Bearer " + token)
      .when()
        .post("/api/quiz/create")
      .then()
        .statusCode(200)
        .body("name", equalTo("q1"))
      .extract()
        .path("id");

      QuizChangeRequest quizChangeRequest = new QuizChangeRequest("qNew", "descrNew", List.of("tag1"));
      given()
        .accept("application/json")
        .contentType("application/json")
        .body(quizChangeRequest)
        .header("Authorization", "Bearer " + token)
        .log().all()
      .when()
        .put("/api/quiz/change_wo_image/{id}", id)
      .then()
        .statusCode(200)
        .body("name", equalTo("qNew"))
        .body("description",equalTo("descrNew"))
      .extract()
        .path("id");

        Quiz changedQiuz = quizRepo.findById(id).get();
        assertEquals(changedQiuz.getName(), "qNew");
        assertEquals(changedQiuz.getDescription(), "descrNew");

    }

    @Test
    void testGetOwnedQuiz() {
      User owner = userRepo.findByUsername("username1").get();
      Quiz quiz1 = Quiz.builder()
                        .name("q1")
                        .isPublic(true)
                        .owner(owner)
                        .build();
      Quiz quiz2 = Quiz.builder()
                        .name("q2")
                        .isPublic(true)
                        .owner(owner)
                        .build();
                        
      quizRepo.save(quiz1);
      quizRepo.save(quiz2);

      int id1 = quizRepo.findByName("q1").get().getId();
      int id2 = quizRepo.findByName("q2").get().getId();



      given()
        .header("Authorization", "Bearer " + token)
        .log().all()
      .when()
        .get("/api/quiz/get_owned")
      .then()
        .statusCode(200)
        .body("quizDataList", hasSize(2))
        .body("quizDataList.id", org.hamcrest.Matchers.hasItems(id1, id2));
      }
    @Test
    void testGetQuiz() {
      User owner = userRepo.findByUsername("username1").get();
      Quiz quiz1 = Quiz.builder()
                        .name("q1")
                        .owner(owner)
                        .isPublic(true)
                        .build();
      Quiz quiz2 = Quiz.builder()
                        .name("q2")
                        .owner(owner)
                        .isPublic(true)
                        .build();
                        
      quizRepo.save(quiz1);
      quizRepo.save(quiz2);
      
      given()
        .header("Authorization", "Bearer " + token)
        .log().all()
      .when()
        .get("/api/quiz/{id}", 2)
      .then()
        .statusCode(200)
        .body("id", equalTo(2));
    }

    @Test
    void testGetQuestions() {
      User owner = userRepo.findByUsername("username1").get();
      Quiz quiz1 = Quiz.builder()
                        .name("testGet")
                        .id(1)
                        .isPublic(true)
                        .owner(owner)
                        .build();

      

      Question q1 = Question.builder()
                            .description("q1")
                            .quiz(quiz1)
                            .build();
      Question q2 = Question.builder()
                            .description("q2")
                            .quiz(quiz1)
                            .build();
      List<Question> questionList = List.of(q1, q2);

      quiz1.setQuestions(questionList);

      quizRepo.save(quiz1);

      int id = quizRepo.findById(1).get().getId();

      

      given()
        .header("Authorization", "Bearer " + token)
        .log().all()
      .when()
        .get("/api/quiz/get_questions/{id}", id)
      .then()
        .log().all()
        .statusCode(200)
        .body("questions",hasSize(2));
    }



    @Test
    void testRemove() {
      User owner = userRepo.findByUsername("username1").get();
      Quiz quiz1 = Quiz.builder()
                        .name("testDel")
                        .owner(owner)
                        .isPublic(true)
                        .build();
      quizRepo.save(quiz1);
      int id = quizRepo.findByName("testDel").get().getId();
      given()
        .header("Authorization", "Bearer " + token)
        .log().all()
      .when()
        .delete("/api/quiz/remove/{id}", id);
      

      List<Quiz> res = quizRepo.findAll();
      
      assertTrue(res.isEmpty());
    }
}
