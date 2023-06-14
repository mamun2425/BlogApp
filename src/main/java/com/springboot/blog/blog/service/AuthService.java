package com.springboot.blog.blog.service;

import com.springboot.blog.blog.entity.Role;
import com.springboot.blog.blog.entity.User;
import com.springboot.blog.blog.exception.MyBlogException;
import com.springboot.blog.blog.payload.LogInDto;
import com.springboot.blog.blog.payload.RegisterDto;
import com.springboot.blog.blog.repository.RoleRepository;
import com.springboot.blog.blog.repository.UserRepository;
import com.springboot.blog.blog.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository, AuthenticationManager authenticationManager, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String login(LogInDto logInDto){
        Authentication authentication = authenticationManager.authenticate(new
                UsernamePasswordAuthenticationToken(logInDto.getUsernameOrEmail(), logInDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);

        return token;

    }


    public RegisterDto signUp(RegisterDto registerDto) {

        if(userRepository.existsByUsername(registerDto.getUsername())){
            throw new MyBlogException(HttpStatus.BAD_REQUEST, "this Username already exists.");
        }
        if(userRepository.existsByEmail(registerDto.getEmail())){
            throw new MyBlogException(HttpStatus.BAD_REQUEST, "this email already exists.");
        }
        User user = new User();
        user.setName(registerDto.getName());
        user.setEmail(registerDto.getEmail());
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER");
        roles.add(userRole);
        user.setRoles(roles);


        userRepository.save(user);

        RegisterDto response = new RegisterDto();

        response.setEmail(user.getEmail());
        response.setName(user.getName());
        response.setId(user.getId());
        response.setPassword(user.getPassword());
        response.setUsername(user.getUsername());
        response.setRoles(roles);

        return response;
    }
}
