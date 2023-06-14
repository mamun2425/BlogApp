package com.springboot.blog.blog.controller;

import com.springboot.blog.blog.payload.JwtAuthResponseDto;
import com.springboot.blog.blog.payload.LogInDto;
import com.springboot.blog.blog.payload.RegisterDto;
import com.springboot.blog.blog.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/blog/auth")
@Tag(
        name = "CRUD Rest API for Auth resources"
)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping({"/login","/signin"})
    public ResponseEntity<JwtAuthResponseDto> login(@RequestBody LogInDto logInDto){

        String token = authService.login(logInDto);

        JwtAuthResponseDto jwtAuthResponseDto = new JwtAuthResponseDto();
        jwtAuthResponseDto.setAccessToken(token);

        return new ResponseEntity<>(jwtAuthResponseDto, HttpStatus.OK);

    }
    @PostMapping({"/signup", "/register"})
    public ResponseEntity<RegisterDto> signUp(@RequestBody RegisterDto registerDto){

        RegisterDto response = authService.signUp(registerDto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


}
