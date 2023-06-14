package com.springboot.blog.blog.payload;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;

import java.util.Set;

// @Data annotation used instead of getter, setter, toString() method by itself using lombok
@Data
public class PostDto {
    private Long id;
    // title cannot be empty or null. and title should have at least two characters.
    @NotEmpty
    @Size(min = 2, message = "Post title should have at least 2 characters")
    private String title;
    // description cannot be empty or null. and title should have at least 10 characters.
    @NotEmpty
    @Size(min = 10, message = "Post description should have at least 10 characters")
    private String description;
    // content cannot be empty or null.
    @NotEmpty
    private String content;

    private Long categoryId;

    private Set<CommentDto> comments;

}
