package com.example.JAQpApi.Controller;


import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.JAQpApi.Entity.JsonUtil;
import com.example.JAQpApi.Entity.Quiz.Quiz;
import com.example.JAQpApi.Exeptions.UserException;
import com.example.JAQpApi.Service.JWTService;
import com.example.JAQpApi.Service.QuizService;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    private final JsonUtil jsonUtil;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final QuizService quizService;
    private final JWTService jwtService;

    @PostMapping
    public ResponseEntity<String> postQuiz(String quizAsJSON, @RequestHeader String authorization ){
        
        Quiz quiz;
        try {
            quiz = JsonUtil.convertJsonToObject(quizAsJSON, Quiz.class);
        } catch (JsonProcessingException e) {
            logger.warn(e.getMessage());
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            logger.warn(e.getMessage());
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

        try {
            quizService.addQuiz(jwtService.extractUsername(authorization), quiz);
        } catch (NoSuchElementException e) {
            logger.warn(e.getMessage());
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        catch (Exception e){
            logger.warn(e.getMessage());
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<String> updateQuiz(String quizAsJSON, Integer id, @RequestHeader String authorization){

        Quiz quiz;
        try {
            quiz = JsonUtil.convertJsonToObject(quizAsJSON, Quiz.class);
        } catch (JsonProcessingException e) {
            logger.warn(e.getMessage());
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            logger.warn(e.getMessage());
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

        try {
            quizService.updateQuiz(jwtService.extractUsername(authorization), quiz, id);
        } catch (NoSuchElementException e) {
            logger.warn(e.getMessage());
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        catch (Exception e){
            logger.warn(e.getMessage());
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }




        return new ResponseEntity<>(HttpStatus.OK);
    }




    
    
}
