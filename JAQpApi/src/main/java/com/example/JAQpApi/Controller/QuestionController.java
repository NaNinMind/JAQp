package com.example.JAQpApi.Controller;

import com.example.JAQpApi.DTO.GetQuestionResponse;
import com.example.JAQpApi.DTO.QuestionCreateRequest;
import com.example.JAQpApi.Exceptions.AccessDeniedException;
import com.example.JAQpApi.Exceptions.ImageException;
import com.example.JAQpApi.Exceptions.NotFoundException;
import com.example.JAQpApi.Service.QuestionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/question")
@Tag(
    name = "Вопросы"
)
@ApiResponse(
    responseCode = "500",
    description = "Unexpected Error",
    content = @Content(
        mediaType = "text/plain"
    )
)
@ApiResponse(
    responseCode = "404",
    description = "NOT FOUND",
    content = @Content(
        mediaType = "text/plain"
    )
)
@ApiResponse(
    responseCode = "403",
    description = "UNATHORIZED",
    content = @Content(
        mediaType = "text/plain"
    )
)
@ApiResponse(
    responseCode = "401",
    description = "UNAUTHENTICATED",
    content = @Content(
        mediaType = "text/plain"
    )
    
)
public class QuestionController
{
    private QuestionService questionService;

    /*--- CREATE ---*/
    @Operation(
        summary = "Добавление вопроса",
        description = "Добавить вопрос к указанному квизу, принадлежащему пользователю. Требуется авторизация или права администратора.",
        responses = @ApiResponse(
            responseCode = "200",
            description = "Вопрос добавлен",
            content = @Content(
                schema = @Schema(
                    implementation = GetQuestionResponse.class
                )
            )
        ),
        requestBody = @RequestBody(
            content = @Content(
                schema = @Schema(
                    implementation = QuestionCreateRequest.class
                )
            )
        )
    )
    @PostMapping("/add")
    public GetQuestionResponse CreateQuestion(@RequestHeader String Authorization, @ModelAttribute QuestionCreateRequest request) throws AccessDeniedException, ImageException, NotFoundException
    {
            return questionService.AddQuestion(Authorization, request);
    }

    /*--- READ ---*/
    @Operation(
        summary = "Получение вопроса",
        description = "Получит вопрос к указанному квизу, принадлежащему пользователю. Требуется авторизация или права администратора.",
        responses = @ApiResponse(
            responseCode = "200",
            description = "Вопрос получен",
            content = @Content(
                schema = @Schema(
                    implementation = GetQuestionResponse.class
                )
            )
        )
    )
    @GetMapping("/{id}")
    public GetQuestionResponse getQuestion(@PathVariable Integer id) throws NotFoundException
    {
        return questionService.GetQuestion(id);
    }
    /*--- UPDATE ---*/
    @Operation(
        summary = "Изменение вопроса",
        description = "Изменить вопрос к указанному квизу, принадлежащему пользователю. Требуется авторизация или права администратора.",
        responses = @ApiResponse(
            responseCode = "200",
            description = "Вопрос изменен",
            content = @Content(
                schema = @Schema(
                    implementation = GetQuestionResponse.class
                )
            )
        ),
        requestBody = @RequestBody(
            content = @Content(
                schema = @Schema(
                    implementation = QuestionCreateRequest.class
                )
            )
        )
    )
    @PutMapping("change/{id}")
    public GetQuestionResponse ChangeQuestion(@RequestHeader String Authorization, @ModelAttribute QuestionCreateRequest request, @PathVariable Integer id) throws AccessDeniedException, ImageException, NotFoundException
    {
        return questionService.ChangeQuestion(Authorization, id, request);
    }

    @Operation(
        summary = "Изменение вопроса",
        description = "Изменить вопрос к указанному квизу, принадлежащему пользователю. Требуется авторизация или права администратора.",
        responses = @ApiResponse(
            responseCode = "200",
            description = "Вопрос изменен",
            content = @Content(
                schema = @Schema(
                    implementation = GetQuestionResponse.class
                )
            )
        )
    )
    @PutMapping("change_wo_image/{id}")
    public GetQuestionResponse ChangeQuestionWOImage(@RequestHeader String Authorization, @RequestParam String description, @PathVariable Integer id) throws AccessDeniedException, NotFoundException
    {
        return questionService.ChangeQuestion(Authorization, id, description);
    }

    /*--- DELETE ---*/
    @Operation(
        summary = "Удаление вопроса",
        responses = @ApiResponse(
            responseCode = "200",
            description = "Вопрос удален",
            content = @Content(
                mediaType = "text/plain"
            )
        )
    )
    @DeleteMapping("/remove/{id}")
    public ResponseEntity Remove(@RequestHeader String Authorization, @PathVariable Integer id) throws AccessDeniedException, ImageException, NotFoundException
    {
        questionService.DeleteQuestion(Authorization, id);
        return ResponseEntity.ok("OK");
    }
}
