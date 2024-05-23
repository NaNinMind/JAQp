package com.example.JAQpApi.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import com.example.JAQpApi.DTO.QuizStatsResponse;
import com.example.JAQpApi.DTO.UserStatsResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/stats")
@Tag( 
    name = "Статистика"
)
@ApiResponses(
    {
        @ApiResponse(
            responseCode = "401",
            description = "UNAUTHENTICATED",
            content = @Content(
                mediaType = "text/plain"
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "UNAUTHORIZED",
            content = @Content(
                mediaType = "text/plain"
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unexpected Server Error",
            content = @Content(
                mediaType = "text/plain"
            )
        )
    }
)
public class StatsController {
    


    @GetMapping("/{id}")
    @Operation(
        summary = "Поулчить статистику пользователя.",
        description = "Получение статистики пользователя.",
        responses = @ApiResponse(
            responseCode = "200",
            description = "Статистика получена.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    implementation = UserStatsResponse.class
                )
            )
        )
    )
    public ResponseEntity<UserStatsResponse> getUserStats(@PathVariable Integer id) {

        return new ResponseEntity<UserStatsResponse>(new UserStatsResponse(), HttpStatus.OK);
    }

    
    @GetMapping("/quiz/{id}")
    @Operation(
        summary = "Поулчить статистику квиза.",
        description = "Получение статистики квиза.",
        responses = @ApiResponse(
            responseCode = "200",
            description = "Статистика получена.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    implementation = QuizStatsResponse.class
                )
            )
        )
    )
    public ResponseEntity<QuizStatsResponse> getQuizStats(@PathVariable Integer id) {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    


}
