package com.springboot.blog.blog.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;


@Data
@AllArgsConstructor
@NoArgsConstructor


public class MyBlogException extends RuntimeException{


    private HttpStatus status;
    private String message;


}
