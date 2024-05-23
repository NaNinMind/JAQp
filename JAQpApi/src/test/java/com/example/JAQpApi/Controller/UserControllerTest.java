package com.example.JAQpApi.Controller;


import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;

import com.example.JAQpApi.DTO.AuthenticationRequest;
import com.example.JAQpApi.DTO.RegistrationRequest;
import com.example.JAQpApi.DTO.UserChangeDataRequest;
import com.example.JAQpApi.Entity.User.Role;
import io.restassured.RestAssured;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.List;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import com.example.JAQpApi.Entity.User.User;
import com.example.JAQpApi.Repository.TokenRepo;
import com.example.JAQpApi.Repository.UserRepo;
import com.example.JAQpApi.Service.AuthService;
import com.example.JAQpApi.Service.JWTService;
import com.example.JAQpApi.Service.UserService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

  @LocalServerPort
  private Integer port;

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

  @Autowired
  UserRepo userRepo;

  @Autowired
  TokenRepo tokenRepo;

  @Autowired
  UserService userService;

  @Autowired
  AuthService authService;

  @Autowired
  JWTService jwtService;

  @Autowired
  PasswordEncoder passwordEncoder;


  @BeforeEach
  void setUp() {
    RestAssured.baseURI = "http://localhost:" + port;
    tokenRepo.deleteAll();
    userRepo.deleteAll();
  }

  @Test
  void test_editUser_OK() throws Exception{
    String password = "password";
    User user1 = User.builder()
                    .username("username")
                    .password(password)
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .role(Role.USER)
                    .build();
    
    authService.register(new RegistrationRequest(user1.getUsername(), user1.getPassword(), user1.getRole()));
    var authResponse = authService.authenticate(new AuthenticationRequest(user1.getUsername(), password));
    String token = authResponse.getJwtToken();
    UserChangeDataRequest userChangeDataRequest = new UserChangeDataRequest("new first name", "new last name", OffsetDateTime.now());
    
    int id = userRepo.findByUsername(user1.getUsername()).get().getId();
    given()
      .contentType("application/json")
      .body(userChangeDataRequest)
      .header("Authorization", "Bearer " + token)
      .log().all()
    .when()
      .post(String.format("/api/users/%d/setting/general", id))
    .then()
      .statusCode(200);
    

    User editedUser = userRepo.findByUsername(user1.getUsername()).get();
    assertEquals(editedUser.getFirstName(),"new first name");
    assertEquals(editedUser.getLastName(),"new last name");
  }
    

  @Test
  void test_editUser_UNAUTHORIZED_wrongID() throws Exception{
    String password = "password";
    User user1 = User.builder()
                    .username("username")
                    .password(password)
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .role(Role.USER)
                    .build();
    

    authService.register(new RegistrationRequest(user1.getUsername(), user1.getPassword(), user1.getRole()));
    var authResponse = authService.authenticate(new AuthenticationRequest(user1.getUsername(), password));
    String token = authResponse.getJwtToken();
    UserChangeDataRequest userChangeDataRequest = new UserChangeDataRequest("new first name", "new last name", OffsetDateTime.now());
    int id = userRepo.findByUsername(user1.getUsername()).get().getId();
    given()
      .contentType("application/json")
      .body(userChangeDataRequest)
      .header("Authorization", "Bearer " + token)
    .when()
    .post(String.format("/api/users/%d/setting/general", id + 1))
    .then()
      .statusCode(403);
  }

  @Test
  void test_editUser_UNAUTHORIZED_wrongToken() throws Exception{
    String password1 = "password1";
    String password2 = "password2";
    User user1 = User.builder()
                    .username("username")
                    .password(password1)
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .role(Role.USER)
                    .build();
    User user2 = User.builder()
                    .username("username2")
                    .password(password2)
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .role(Role.USER)
                    .build();
    
    authService.register(new RegistrationRequest(user1.getUsername(), user1.getPassword(), user1.getRole()));
    authService.register(new RegistrationRequest(user2.getUsername(), user2.getPassword(), user2.getRole()));
    var authResponse = authService.authenticate(new AuthenticationRequest(user1.getUsername(), password1));
    String token = authResponse.getJwtToken();
    UserChangeDataRequest userChangeDataRequest = new UserChangeDataRequest("new first name", "new last name", OffsetDateTime.now());
    int id = userRepo.findByUsername(user1.getUsername()).get().getId();
    given()
      .contentType("application/json")
      .body(userChangeDataRequest)
      .header("Authorization", "Bearer " + token)
    .when()
    .post(String.format("/api/users/%d/setting/general", id + 1))
    .then()
      .statusCode(403);
  }
  
  @Test
  void test_editUser_badValue(){

  }







}