package com.example.JAQpApi.Service;

import com.example.JAQpApi.DTO.*;
import com.example.JAQpApi.Entity.Quiz.ImageMetadata;
import com.example.JAQpApi.Entity.Quiz.Question;
import com.example.JAQpApi.Entity.Quiz.Quiz;
import com.example.JAQpApi.Entity.User.User;
import com.example.JAQpApi.Exceptions.AccessDeniedException;
import com.example.JAQpApi.Exceptions.ImageException;
import com.example.JAQpApi.Exceptions.NotFoundException;
import com.example.JAQpApi.Repository.ImageMetadataRepo;
import com.example.JAQpApi.Repository.QuizRepo;

import lombok.NoArgsConstructor;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class QuizService
{
    private final QuizRepo quizRepo;
    private final ImageMetadataRepo imageMetadataRepo;
    private final AuthService authService;
    private final ImageService imageService;
    private final QuestionService questionService;

    //no test
    private QuizResponse QuizResponseFactory(Quiz _quiz)
    {
        return QuizResponse.builder()
                .id(_quiz.getId())
                .description(_quiz.getDescription())
                .image_name((_quiz.getThumbnail() != null) ? _quiz.getThumbnail().getName() : null)
                .name(_quiz.getName())
                .build();
    }
    //no test
    public QuizService(QuizRepo quizRepo, ImageMetadataRepo imageMetadataRepo, AuthService authService, ImageService imageService, @Lazy QuestionService questionService)
    {
        this.quizRepo = quizRepo;
        this.imageMetadataRepo = imageMetadataRepo;
        this.authService = authService;
        this.imageService = imageService;
        this.questionService = questionService;
    }
    //TESTED
    public Optional<Quiz> ValidateAccessAndGetQuiz(String _token, Integer _id) throws AccessDeniedException, NotFoundException
    {
        Quiz result = quizRepo.findById(_id).orElseThrow(() -> new NotFoundException("Quiz", "id", _id.toString()));
        if (Objects.equals(authService.GetUserByToken(_token).getId(), result.getOwner().getId()))
        {
            return Optional.of(result);
        }
        throw new AccessDeniedException("Access denied");
    }
    //TESTED
    public QuizResponse CreateQuiz(String _token, QuizCreateRequest _request) throws NotFoundException, ImageException
    {
        ImageMetadata thumbnail = null;
        if (_request.getThumbnail() != null)
        {
            thumbnail = imageMetadataRepo.findById(imageService.UploadFile(_request.getThumbnail(), _token)).orElseThrow(() -> new ImageException("Unknown image error"));
        }
        User owner = authService.GetUserByToken(_token);
        Quiz quiz = Quiz.builder()
                .description(_request.getDescription())
                .name(_request.getName())
                .thumbnail(thumbnail)
                .owner(owner)
                .build();
        quiz = quizRepo.save(quiz);
        return QuizResponseFactory(quiz);
    }
    //NO LOGIC TO UNITTEST
    public OwnedQuizListResponse GetOwnedQuiz(String _token) throws NotFoundException
    {
        User owner = authService.GetUserByToken(_token);
        List<QuizData> list = new ArrayList<>();
        for (Quiz quiz : quizRepo.findAllByOwner(owner))
        {
            list.add(QuizData.builder()
                    .id(quiz.getId())
                    .name(quiz.getName())
                    .build());
        }
        return new OwnedQuizListResponse(list);
    }
    //NO LOGIC TO UNITTEST
    public QuestionsOfQuizResponse GetQuestionsOfQuiz(Integer _id) throws NotFoundException
    {
        return QuestionsOfQuizResponse.toDto(quizRepo.findById(_id).orElseThrow(() -> new NotFoundException("")).getQuestions());
    }
    //NO LOGIC TO UNITTEST
    public QuizResponse GetQuiz(Integer _id) throws NotFoundException
    {
        Quiz quiz = quizRepo.findById(_id).orElseThrow(() -> new NotFoundException(""));
        return QuizResponseFactory(quiz);
    }
    //NO LOGIC TO UNITTEST
    public void DeleteQuiz(String _token, Integer _id) throws AccessDeniedException, NotFoundException, ImageException
    {
        Quiz quiz = ValidateAccessAndGetQuiz(_token, _id).orElseThrow(() -> new NotFoundException("Quiz", "id", _id.toString()));
        List<Question> questions = quiz.getQuestions();
        ImageMetadata imageMetadata = quiz.getThumbnail();
        if ( questions != null ){
            for (Question question : questions)
            {
                questionService.DeleteQuestion(_token, question.getId());
            }
        }
        quizRepo.delete(quiz);
        if ( imageMetadata != null){
            imageService.DeleteImage(imageMetadata, _token);
        }
    }
    //TOTEST
    protected Quiz ChangeQuiz(String _token, Integer _id, String _name, String _description) throws AccessDeniedException, NotFoundException
    {
        Quiz quiz = ValidateAccessAndGetQuiz(_token, _id).orElseThrow(() -> new NotFoundException("Quiz", "id", _id.toString()));
        quiz.setDescription(_description);
        quiz.setName(_name);
        return quiz;
    }
    //NO LOGIC TO UNITTEST
    public QuizResponse ChangeQuiz(String _token, QuizCreateRequest _request, Integer _id) throws AccessDeniedException, NotFoundException, ImageException
    {
        Quiz quiz = ChangeQuiz(_token, _id, _request.getName(), _request.getDescription());
        ImageMetadata imageMetadata = quiz.getThumbnail();
        quiz.setThumbnail(null);
        quizRepo.save(quiz);
        imageMetadata = imageService.ChangeImage(imageMetadata, _token, _request.getThumbnail());
        quiz.setThumbnail(imageMetadata);
        quizRepo.save(quiz);
        return QuizResponseFactory(quiz);
    }
    //NO LOGIC TO UNITTEST
    public QuizResponse ChangeQuiz(String _token, QuizChangeRequest _request, Integer _id) throws AccessDeniedException, NotFoundException
    {
        Quiz quiz = ChangeQuiz(_token, _id, _request.getName(), _request.getDescription());
        quizRepo.save(quiz);
        return QuizResponseFactory(quiz);
    }
}
