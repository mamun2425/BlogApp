package com.springboot.blog.blog.payload;

import com.springboot.blog.blog.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {

    private int id;
    private String name;
    private String username;
    private String email;
    private String password;
    private Set<Role> roles;
}
