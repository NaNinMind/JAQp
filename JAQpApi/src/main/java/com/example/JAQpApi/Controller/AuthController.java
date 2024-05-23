package com.example.JAQpApi.Controller;

import com.example.JAQpApi.DTO.AuthenticationRequest;
import com.example.JAQpApi.DTO.AuthenticationResponse;
import com.example.JAQpApi.DTO.RegistrationRequest;
import com.example.JAQpApi.Entity.User.Role;
import com.example.JAQpApi.Exceptions.NotFoundException;
import com.example.JAQpApi.Exceptions.UserAlreadyExists;
import com.example.JAQpApi.Service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
//import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификационные endpoint-ы")
public class AuthController {

    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    private final AuthService authService;

    @PostMapping("/register")
    @Operation(
        description = "Регистрация пользователя. ",
        summary = "Регистрация",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                    implementation = RegistrationRequest.class
                )
            )
        ),
        responses = {
            @ApiResponse(
                description = "Успешная регистрация",
                responseCode = "200",
                content = @Content(
                    mediaType = "text/plain",
                    examples = @ExampleObject(
                        value = "Пользователь зарегистрирован"
                    )
                )
            ),
            @ApiResponse(
                content = @Content(
                    mediaType = "text/plain"
                ),
                description = "Пользователь с таким ником уже существует",
                responseCode = "400"
            ),
            @ApiResponse(
                content = @Content(
                    mediaType = "text/plain"
                ),
                description = "Unexpected Error",
                responseCode = "500"
            )
        }
    )
    public ResponseEntity<String> register(
            @RequestBody RegistrationRequest request
    ) {
        if (request.getRole() == null)
        {
            request.setRole(Role.USER);
        }
        MDC.put("Username", request.getUsername());
        ResponseEntity<String> resp;
        try {
            logger.info("Registration attempt");
            
            authService.register(request);
            resp = ResponseEntity.ok("User registered");

            MDC.put("response", resp.toString());
            logger.info("User registered successfully.");
        } catch (UserAlreadyExists e) {
            resp = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            logger.warn(resp.toString());

        } catch (Exception e){
            resp = ResponseEntity.badRequest().build();
            MDC.put("response", resp.toString());
            logger.error(e.getMessage());
        }

        MDC.clear();
        return resp;
    }
    @PostMapping("/authenticate")
    @Operation(
        description = "Авторизация пользователя с получением токена",
        summary = " Аутентификация/Авторизация",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Успешная регитрация",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(
                            implementation = AuthenticationResponse.class)
                    )
                }
            ),
            @ApiResponse(
                content = @Content(
                    mediaType = "text/plain"
                ),
                responseCode = "400",
                description = "Пользователь с таким логином не существует"
            ),
            @ApiResponse(
                content = @Content(
                    mediaType = "text/plain"
                ),
                description = "Unexpected Error",
                responseCode = "500"
            )

        }
    )
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        MDC.put("Username", request.getUsername());
        ResponseEntity<AuthenticationResponse> resp;
        try{
            logger.info("Authentication attempt");
            AuthenticationResponse authResp = authService.authenticate(request);
            //MDC.put("response", authResp.toString());
            logger.info("User authenticated | id = " + authResp.getId() + " | token = " + authResp.getJwtToken() );
            resp = ResponseEntity.ok(authResp);
        }
        catch(NotFoundException e){
            resp = new ResponseEntity<>(new AuthenticationResponse("No user with this username"), HttpStatus.BAD_REQUEST);
            logger.warn(resp.toString());
        }
        catch(Exception e){
            logger.error(e.getMessage());
            resp = new ResponseEntity<>(new AuthenticationResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }

        MDC.clear();
        return resp;
    }

    @PostMapping("/logout")
    @Operation(
        description = "Выйти из аккаунта пользователя.",
        summary = "LOGOUT",
        responses = {
            @ApiResponse(
                description = "Успешный выход из системы",
                responseCode = "200",
                content = @Content(
                    mediaType = "text/plain",
                    examples = @ExampleObject(
                        value = "Успешный выход из системы."
                    )
                )
            ),
            @ApiResponse(
                content = @Content(
                    mediaType = "text/plain"
                ),
                description = "not logged in",
                responseCode = "401"
            ),
            @ApiResponse(
                content = @Content(
                    mediaType = "text/plain"
                ),
                description = "Unexpected Error",
                responseCode = "500"
            )
        }
    )
    ResponseEntity<String> logout( @RequestHeader String Authorization){
        return new ResponseEntity<>(HttpStatus.OK);
    } 







}
