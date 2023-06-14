package com.springboot.blog.blog.payload;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private long id;

    @NotBlank(message = "Should not be null or empty or blank")
    private String name;
    @NotBlank(message = "Should not be null or empty or blank")
    @Email
    private String email;
    @NotEmpty(message = "Should not be null or empty")
    private String body;
}
