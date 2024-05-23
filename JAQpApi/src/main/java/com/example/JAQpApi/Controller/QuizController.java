package com.example.JAQpApi.Controller;

import com.example.JAQpApi.DTO.OwnedQuizListResponse;
import com.example.JAQpApi.DTO.QuizCreateRequest;
import com.example.JAQpApi.DTO.QuizResponse;
import com.example.JAQpApi.Exceptions.*;
import com.example.JAQpApi.Service.QuizService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import com.example.JAQpApi.DTO.*;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8080")
@AllArgsConstructor
@RestController
@RequestMapping("/api/quiz")
@Tag(name = "Работа с квизами")
@ApiResponses(
    {
        @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Not found"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "UNAUTHORIZED"
        )

    }
)
public class QuizController
{

    private final QuizService quizService;

    /*--- CREATE ---*/

    @PostMapping("/create")
    @Operation(
        summary = "Создание квиза",
        description = "Создание квиза. Требуется авторизация",
        parameters = @Parameter(
            in = ParameterIn.HEADER,
            description = "JWT auth token",
            required = true
        ),
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                schema = @Schema(
                    implementation = QuizCreateRequest.class
                )
            )
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Квиз создан успешно.",
                content = @Content(
                    schema = @Schema(
                        implementation = QuizCreateResponse.class
                    )
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Неправильное изображение.",
                content = @Content(
                    mediaType = "text/plain",
                    examples = @ExampleObject(
                        value = "Неправильное изображение."
                    )
                )
            )
        }
    )
    public QuizResponse CreateQuiz(@ModelAttribute QuizCreateRequest request, @RequestHeader String Authorization) throws ImageException, NotFoundException
    {
        return quizService.CreateQuiz(Authorization, request);
    }


    /*--- READ ---*/
    // general read
    @Operation(
        summary = "Получить информацию о квизе",
        description = "Получить информацию о квизе с заданным id. При попытке получить скрытый квиз, непренадлежащий авторизированному пользователю - 403",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Квиз получен",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = QuizResponse.class
                    )
                )
            )
        }

    )
    @GetMapping("/{id}")
    public QuizResponse GetQuiz(@PathVariable Integer id) throws NotFoundException
    {
        return quizService.GetQuiz(id);
    }

    // read owned quiz
    @Operation(
        summary = "Получить свои квизы.",
        description = "Получить свои квизы. Необходима авторизация.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Квизы получены",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = OwnedQuizListResponse.class
                    )
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "UNAUTHENTICATED"
            )
        }

    )
    @GetMapping("/get_owned")
    public OwnedQuizListResponse GetOwnedQuiz(@RequestHeader @Nullable String Authorization) throws NotFoundException
    {
        return quizService.GetOwnedQuiz(Authorization);
    }


    @GetMapping("/get_questions/{id}")
    public QuestionsOfQuizResponse GetQuestions(@PathVariable Integer id) throws NotFoundException
    {
        return quizService.GetQuestionsOfQuiz(id);
    }

    /*--- UPDATE ---*/
    @Operation(
        summary = "Изменение квиза",
        responses = @ApiResponse(
            responseCode = "200",
            description = "Вопрос изменен",
            content = @Content(
                schema = @Schema(
                    implementation = QuizResponse.class
                )
            )
        ),
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                schema = @Schema(
                    implementation = QuizCreateRequest.class
                )
            )
        )
    )
    @PutMapping("change/{id}")
    public QuizResponse ChangeQuiz(@RequestHeader String Authorization, @RequestBody QuizCreateRequest request, @PathVariable Integer id) throws AccessDeniedException, ImageException, NotFoundException
    {
        return quizService.ChangeQuiz(Authorization, request, id);
    }

    @Operation(
        summary = "Изменение вопроса",
        responses = @ApiResponse(
            responseCode = "200",
            description = "Вопрос изменен",
            content = @Content(
                schema = @Schema(
                    implementation = QuizResponse.class
                )
            )
        ),
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                schema = @Schema(
                    implementation = QuizChangeRequest.class
                )
            )
        )
    )
    @PutMapping("change_wo_image/{id}")
    public QuizResponse ChangeQuizWOImage(@RequestHeader String Authorization, @RequestBody QuizChangeRequest request, @PathVariable Integer id) throws AccessDeniedException, NotFoundException
    {
        return quizService.ChangeQuiz(Authorization, request, id);
    }

    /*--- DELETE ---*/
    @Operation(
        summary = "Удаление квиза",
        responses = @ApiResponse(
            responseCode = "200",
            description = "Квиз удален",
            content = @Content(
                mediaType = "text/plain"
            )
        )
    )
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> Remove(@RequestHeader String Authorization, @PathVariable Integer id) throws AccessDeniedException, ImageException, NotFoundException
    {
        quizService.DeleteQuiz(Authorization, id);
        return ResponseEntity.ok("OK");
    }
}