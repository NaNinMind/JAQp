package com.example.JAQpApi.Service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.JAQpApi.DTO.QuizCreateRequest;
import com.example.JAQpApi.DTO.QuizResponse;
import com.example.JAQpApi.Entity.Quiz.Quiz;
import com.example.JAQpApi.Entity.User.Role;
import com.example.JAQpApi.Entity.User.User;
import com.example.JAQpApi.Exceptions.AccessDeniedException;
import com.example.JAQpApi.Exceptions.ImageException;
import com.example.JAQpApi.Exceptions.NotFoundException;
import com.example.JAQpApi.Repository.QuizRepo;
import lombok.RequiredArgsConstructor;

import static org.mockito.ArgumentMatchers.*;

@TestInstance(Lifecycle.PER_CLASS)
@RequiredArgsConstructor
public class QuizServiceTest {
    

    @Mock private QuizRepo quizRepo;
    @Mock private AuthService authService;
    @Mock private QuestionService questionService;

    @Mock
    QuizService mockService;

    @InjectMocks
    QuizService quizService;

    @BeforeAll
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }


    //QUIZ CREATE TESTS

    @Test
    void testCreateQuiz_NoImage_Correct() throws NotFoundException, ImageException {

        // assemble
        QuizCreateRequest quizCreateRequest = QuizCreateRequest.builder()
                                                                .name("quizName")
                                                                .description("quizDescription")
                                                                .thumbnail(null)
                                                                .build();
        User mockUser = User.builder()
                            .id(1)
                            .username("mockUsername")
                            .password("mockPassword")
                            .role(Role.ADMIN)
                            .build();                                                        
        Quiz mockQuiz = Quiz.builder()
                            .description(quizCreateRequest.getDescription())
                            .name(quizCreateRequest.getName())
                            .thumbnail(null)
                            .owner(mockUser)
                            .id(1)
                            .build();
        String mockToken = "mockToken";
        
        when(authService.GetUserByToken(mockToken)).thenReturn(mockUser);
        when(quizRepo.save(any(Quiz.class))).thenReturn(mockQuiz);
        // act
        QuizResponse response = quizService.CreateQuiz(mockToken, quizCreateRequest);

        // assert
        assertEquals(response.getId(), mockQuiz.getId());
        assertEquals(response.getDescription(), mockQuiz.getDescription());
        assertEquals(response.getName(), mockQuiz.getName());
        
    }

    // AUX METHODS TESTS
    @Test
    void testValidateAccessAndGetQuiz_OK() throws AccessDeniedException, NotFoundException {
        //assemble
        String mockToken = "qwe";
        Integer mockId = 1;
        int mockUserId = 1;

        User mockUser = User.builder()
                            .id(mockUserId)
                            .build();
        Quiz mockQuiz = Quiz.builder()
                            .id(mockId)
                            .owner(mockUser)
                            .build();

        when(quizRepo.findById(anyInt())).thenReturn(Optional.of(mockQuiz));
        when(authService.GetUserByToken(anyString())).thenReturn(mockUser);
        //act
        Optional<Quiz> result = quizService.ValidateAccessAndGetQuiz(mockToken, mockId);

        //assert
        assertEquals(result.get().getId(), mockId);
        assertEquals(result.get().getOwner(),mockUser);

    }
}
