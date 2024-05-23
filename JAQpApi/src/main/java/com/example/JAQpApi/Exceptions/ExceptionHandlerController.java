package com.example.JAQpApi.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController
{
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(Exception re)
    {
        return new ResponseEntity<>(re.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ImageInvalidException.class)
    public ResponseEntity<String> handleInvalidImageException(Exception _exception)
    {
        return new ResponseEntity<>(_exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ImageStorageException.class)
    public ResponseEntity<String> handleImageStorageException(Exception _exception)
    {
        return new ResponseEntity<>(_exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(Exception _exception)
    {
        return new ResponseEntity<>(_exception.getMessage(), HttpStatus.FORBIDDEN);
    }
}
