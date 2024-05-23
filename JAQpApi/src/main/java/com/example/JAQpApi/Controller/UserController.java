package com.example.JAQpApi.Controller;

import com.example.JAQpApi.DTO.UserChangeDataRequest;
import com.example.JAQpApi.DTO.UserGeneralResponse;
import com.example.JAQpApi.Exceptions.AccessDeniedException;
import com.example.JAQpApi.Exceptions.NotFoundException;
import com.example.JAQpApi.Service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api/users")
@RequiredArgsConstructor
@SecurityRequirement( name = "bearerAuth" )
@Tag( 
    name = "Настройки профиля",
    description = "Изменение различных значение профиля и получение общей информации о пользователе")
@ApiResponses( 
    {
        @ApiResponse(
            description = "UNAUTHENTICATED",
            responseCode = "401",
            content = @Content(
                mediaType = "text/plain"
            )
        ),
        @ApiResponse(
            description = "UNAUTHORIZED",
            responseCode = "403",
            content = @Content(
                mediaType = "text/plain"
            )
        ),
        @ApiResponse(
            description = "User not found",
            responseCode = "404",
            content = @Content(
                mediaType = "text/plain"
            )
        )
    }
)
public class UserController
{
    private final UserService userService;

    @PostMapping("/{id}/setting/general")
    @Operation(
        summary = "Задание значений профиля",
        description = "Задание нескольких значений профиля для пользователя, с заданным id. Требуется совпадение авторизационных данных или наличие прав Администратора",
        parameters = {
            @Parameter(
                description = "JWT auth token",
                name = "Authorization",
                in = ParameterIn.HEADER,
                required = true
            )
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(
                schema = @Schema(
                    implementation = UserChangeDataRequest.class
                )
            )
        ),
        responses = @ApiResponse(
            description = "Изменение прошло успешно",
            responseCode = "200",
            content = @Content(
                mediaType = "text/plain"
            )
        )
    )
    public ResponseEntity<String> setGeneralInfo(@PathVariable Integer id, @RequestHeader String Authorization, @RequestBody UserChangeDataRequest request) throws AccessDeniedException, NotFoundException
    {
        userService.SetGeneralData(id, Authorization, request);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Получение информации о пользователе",
        description = "Получение информации о пользователе",
        responses = @ApiResponse(
            responseCode = "200",
            description = "Данные пользователя получены успешно",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    
                    implementation = UserGeneralResponse.class
                )
            )
        )
    )
    public UserGeneralResponse getUser(@PathVariable Integer id) throws NotFoundException
    {
        return userService.GetUserGeneralInfo(id);
    }
}
