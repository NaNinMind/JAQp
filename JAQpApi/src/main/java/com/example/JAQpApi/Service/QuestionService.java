package com.example.JAQpApi.Service;

import com.example.JAQpApi.DTO.QuestionCreateRequest;
import com.example.JAQpApi.Entity.Quiz.Answer;
import com.example.JAQpApi.Entity.Quiz.ImageMetadata;
import com.example.JAQpApi.Entity.Quiz.Question;
import com.example.JAQpApi.Entity.Quiz.Quiz;

import com.example.JAQpApi.DTO.GetQuestionResponse;
import com.example.JAQpApi.Exceptions.AccessDeniedException;
import com.example.JAQpApi.Exceptions.ImageException;
import com.example.JAQpApi.Exceptions.NotFoundException;
import com.example.JAQpApi.Repository.QuestionRepo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class QuestionService
{
    private final ImageService imageService;
    private final QuestionRepo questionRepo;
    private final QuizService quizService;
    private final AnswerService answerService;

    public QuestionService(ImageService imageService, QuestionRepo questionRepo, QuizService quizService, @Lazy AnswerService answerService)
    {
        this.imageService = imageService;
        this.questionRepo = questionRepo;
        this.quizService = quizService;
        this.answerService = answerService;
    }


    @CacheEvict(value = "QuestionService::GetQuestion", key = "#_id")
    public Optional<Question> ValidateAccessAndGetQuestion(String _token, Integer _id) throws NotFoundException, AccessDeniedException
    {
        Question question = questionRepo.findById(_id).orElseThrow(() -> new NotFoundException(""));
        if(quizService.ValidateAccessAndGetQuiz(_token ,question.getQuiz().getId()).isPresent())
        {
            return Optional.of(question);
        }
        else
        {
            return Optional.empty();
        }
    }

    @CacheEvict(value = "QuizService::GetQuestions", key = "#_request.quiz_id")
    public GetQuestionResponse AddQuestion(String _token, QuestionCreateRequest _request) throws NotFoundException, ImageException, AccessDeniedException
    {
        Quiz quiz = quizService.ValidateAccessAndGetQuiz(_token, _request.getQuiz_id()).orElseThrow(() -> new NotFoundException(""));
        ImageService.ImageMetadataWithName imageMetadataWithName = imageService.HandleNullableImageRequest(_token, _request.getImage());
        Question question = Question.builder()
                .image(imageMetadataWithName.getImageMetadata())
                .description(_request.getContent())
                .quiz(quiz)
                .build();
        questionRepo.save(question);
        return GetQuestionResponseFactory(question);
    }

    @CacheEvict(value = "QuestionService::GetQuestion", key = "#_id")
    public void DeleteQuestion(String _token, Integer _id) throws NotFoundException, AccessDeniedException, ImageException
    {
        Question question = questionRepo.findById(_id).orElseThrow(() -> new NotFoundException("Question", "id", Integer.toString(_id)));
        Quiz quiz = quizService.ValidateAccessAndGetQuiz(_token, question.getQuiz().getId()).orElseThrow(() -> new NotFoundException(""));
        List<Answer> answerList = question.getAnswerList();
        ImageMetadata imageMetadata = question.getImage();
        for (Answer answer : answerList)
        {
            answerService.DeleteAnswer(_token, answer.getId());
        }
        questionRepo.delete(question);
        imageService.DeleteImage(imageMetadata, _token);
    }

    private GetQuestionResponse GetQuestionResponseFactory(Question _question)
    {
        List<Integer> answerId = new ArrayList<>();
        if(_question.getAnswerList() != null)
        {
            for (Answer answer: _question.getAnswerList())
            {
                answerId.add(answer.getId());
            }
        }
        return GetQuestionResponse.builder()
                .description(_question.getDescription())
                .id(_question.getId())
                .image((_question.getImage() != null) ? _question.getImage().getName() : null)
                .answers(answerId)
                .build();
    }


    @Cacheable(value = "QuestionService::GetQuestion", key = "#_id")
    public GetQuestionResponse GetQuestion(Integer _id) throws NotFoundException
    {
        Question question = questionRepo.findById(_id).orElseThrow(() -> new NotFoundException("Question", "id", Integer.toString(_id)));
        return GetQuestionResponseFactory(question);
    }

    @CacheEvict(value = "QuestionService::GetQuestion", key = "#_id")
    public GetQuestionResponse ChangeQuestion(String _token, Integer _id, QuestionCreateRequest _request) throws AccessDeniedException, NotFoundException, ImageException
    {
        Question question = ValidateAccessAndGetQuestion(_token, _id).orElseThrow(() -> new NotFoundException("Question", "id", Integer.toString(_id)));
        ImageMetadata imageMetadata = question.getImage();
        question.setImage(null);
        question.setDescription(_request.getContent());
        questionRepo.save(question);
        imageMetadata = imageService.ChangeImage(imageMetadata, _token, _request.getImage());
        question.setImage(imageMetadata);
        questionRepo.save(question);
        return GetQuestionResponseFactory(question);
    }

    @CacheEvict(value = "QuestionService::GetQuestion", key = "#_id")
    public GetQuestionResponse ChangeQuestion(String _token, Integer _id, String _description) throws AccessDeniedException, NotFoundException
    {
        Question question = ValidateAccessAndGetQuestion(_token, _id).orElseThrow(() -> new NotFoundException("Question", "id", Integer.toString(_id)));
        question.setDescription(_description);
        questionRepo.save(question);
        return GetQuestionResponseFactory(question);
    }


}
