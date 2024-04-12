package com.example.JAQpApi.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.swing.text.html.Option;

import org.springframework.stereotype.Service;

import com.example.JAQpApi.Entity.JsonUtil;
import com.example.JAQpApi.Entity.Quiz.Quiz;
import com.example.JAQpApi.Entity.User.User;
import com.example.JAQpApi.Exeptions.UserException;
import com.example.JAQpApi.Repository.QuizRepo;
import com.example.JAQpApi.Repository.UserRepo;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepo quizRepo;
    private final UserRepo userRepo;
    private final JsonUtil jsonUtil;


    public void addQuiz( String username, Quiz quiz ) throws NoSuchElementException{

        User user = userRepo.findByUsername(username).orElseThrow();
        quiz.setUser(user);
        quizRepo.save(quiz);
        
    }

    public void updateQuiz ( String username, Quiz quiz, Integer quizId) throws NoSuchElementException{
        Optional<User> user = userRepo.findByUsername(username);
        if ( user.isPresent() ){
            Quiz dbQuiz = quizRepo.findById(quizId).orElseThrow();

            /*** update fields ***** */
            dbQuiz.setName(quiz.getName());
            dbQuiz.setQuestions(quiz.getQuestions());
            /*********************** */

            quizRepo.save(dbQuiz);
        }
    }
    
}
