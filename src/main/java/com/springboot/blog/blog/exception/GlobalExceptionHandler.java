package com.springboot.blog.blog.exception;

import com.springboot.blog.blog.payload.ErrorDetailsDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // Handle specific exception
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetailsDto> handleResourceNotFoundException(
            ResourceNotFoundException exception, WebRequest webRequest){

        ErrorDetailsDto errorDetailsDto = new ErrorDetailsDto(
                new Date() ,exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetailsDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MyBlogException.class)
    public ResponseEntity<ErrorDetailsDto> handleMyBlogException(
            MyBlogException exception, WebRequest webRequest){

        ErrorDetailsDto errorDetailsDto = new ErrorDetailsDto(
                new Date() ,exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetailsDto, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDetailsDto> handleAccessDeniedException(
            AccessDeniedException exception, WebRequest webRequest){

        ErrorDetailsDto errorDetailsDto = new ErrorDetailsDto(
                new Date() ,exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetailsDto, HttpStatus.UNAUTHORIZED);
    }





    // Handle global exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetailsDto> handleMyBlogException(
            Exception exception, WebRequest webRequest){

        ErrorDetailsDto errorDetailsDto = new ErrorDetailsDto(
                new Date() ,exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetailsDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }




    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error -> {
            String fieldName = ((FieldError)error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName,message);
        }));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);

    }
}
