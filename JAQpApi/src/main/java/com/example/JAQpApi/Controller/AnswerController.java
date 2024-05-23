package com.example.JAQpApi.Controller;


import com.example.JAQpApi.DTO.AnswerCreateRequest;
import com.example.JAQpApi.DTO.AnswerCreateResponse;
import com.example.JAQpApi.Exceptions.*;
import com.example.JAQpApi.Service.AnswerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import com.example.JAQpApi.DTO.*;
import com.example.JAQpApi.Exceptions.*;
import com.example.JAQpApi.Service.AnswerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import com.example.JAQpApi.DTO.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/answer")
@Tag(
    name = "Ответы к вопросам квиза"
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
public class AnswerController
{
    private final AnswerService answerService;

    @PostMapping("/add")
    @Operation(
        summary = "Добавление ответа",
        description = "Добавление ответа к указанному вопросу квиза, принадлежащего пользователю. Требуется авторизация или права администратора.",
        responses = @ApiResponse(
            responseCode = "200",
            description = "Успешно добавлен ответ"
        )
    )
    public GetAnswerResponse AddAnswer(@RequestHeader String Authorization, @ModelAttribute AnswerCreateRequest request) throws AccessDeniedException, ImageException, NotFoundException
    {
        // return new AnswersCreateResponse();
        return answerService.AddAnswer(Authorization, request);
    }

    @DeleteMapping("/remove")
    @Operation(
        summary = "Удаление ответа",
        description = "Удвление ответа к указанному вопросу квиза, принадлежащего пользователю. Требуется авторизация или права администратора.",
        responses = @ApiResponse(
            responseCode = "200",
            description = "Успешно удален ответ"
        )
    )
    public ResponseEntity<String> RemoveAnswer(@RequestHeader String Authorization, @PathVariable Integer answerID) throws AccessDeniedException, ImageException, NotFoundException
    {
        return new ResponseEntity<>("OK", HttpStatus.OK);
        // return answerService.AddAnswer(Authorization, request);
    }

    @PutMapping
    @Operation(
        summary = "Изменение ответа",
        description = "Изменение ответа к указанному вопросу квиза, принадлежащего пользователю. Требуется авторизация или права администратора.",
        responses = @ApiResponse(
            responseCode = "200",
            description = "Успешно изменен ответ"
        )
    )
    ResponseEntity<AnswerCreateResponse> ChangeAnswer(@RequestHeader String Authorization, @PathVariable Integer answerID, @ModelAttribute AnswerCreateRequest request) throws AccessDeniedException, ImageException, NotFoundException
    {
        return new ResponseEntity<>(new AnswerCreateResponse(), HttpStatus.OK);
        // return answerService.AddAnswer(Authorization, request);
    }

    @GetMapping
    @Operation(
        summary = "Получить ответ",
        description = "Получение ответа к указанному вопросу квиза, принадлежащего пользователю. Требуется авторизация или права администратора.",
        responses = @ApiResponse(
            responseCode = "200",
            description = "Успешно получен ответ"
        )
    )
    ResponseEntity<AnswerCreateResponse> GetAnswer(@RequestHeader String Authorization, @PathVariable Integer answerID, @PathVariable Integer questioID, @ModelAttribute AnswerCreateRequest request) throws AccessDeniedException, ImageException, NotFoundException
    {
        return new ResponseEntity<>(new AnswerCreateResponse(), HttpStatus.OK);
        // return answerService.AddAnswer(Authorization, request);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> Remove(@RequestHeader String Authorization, @PathVariable Integer id) throws AccessDeniedException, ImageException, NotFoundException
    {
        answerService.DeleteAnswer(Authorization, id);
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/{id}")
    public GetAnswerResponse GetAnswer(@PathVariable Integer id) throws NotFoundException
    {
        return answerService.GetAnswer(id);
    }

    @PutMapping("change/{id}")
    public GetAnswerResponse ChangeAnswer(@RequestHeader String Authorization, @ModelAttribute AnswerCreateRequest request, @PathVariable Integer id) throws AccessDeniedException, ImageException, NotFoundException
    {
        return answerService.ChangeAnswer(Authorization, id,  request);
    }

    @PutMapping("change_wo_image/{id}")
    public GetAnswerResponse ChangeQuizWOImage(@RequestHeader String Authorization, @ModelAttribute ChangeAnswerRequest request, @PathVariable Integer id) throws AccessDeniedException, NotFoundException
    {
        return answerService.ChangeAnswer(Authorization, id, request);
    }

}
