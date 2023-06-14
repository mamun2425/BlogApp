package com.springboot.blog.blog.controller;


import com.springboot.blog.blog.payload.CommentDto;
import com.springboot.blog.blog.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/blog/")
@Tag(
        name = "CRUD Rest API for Comment resources"
)
public class CommentController {

    private final CommentService commentService;


    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/post/{postId}/comment")
    public ResponseEntity<List<CommentDto>> readCommentByPostId(
            @PathVariable(value = "postId") Long postId){
        return new ResponseEntity<>(commentService.readComment(postId), HttpStatus.OK);
    }

    @GetMapping("/post/{postId}/comment/{commentId}")
    public ResponseEntity<CommentDto> readCommentByPostIdAndCommentId(
            @PathVariable(value = "postId") Long postId,
            @PathVariable(value = "commentId") Long commentId

    ){
        return new ResponseEntity<>(commentService.readCommentByCommentId(postId,commentId), HttpStatus.OK);
    }


    @Operation(
            summary = "Create comment api",
            description = "Create comment REST api is used to create new comment for specific post and save in database."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/post/{postId}/comment")
    public ResponseEntity<CommentDto> createComment(
            @PathVariable(value = "postId") Long postId,
            @Valid @RequestBody CommentDto commentDto) {

        return new ResponseEntity<>(commentService.createComment(postId,commentDto), HttpStatus.CREATED);

    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/post/{postId}/comment/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable(value = "postId") Long postId,
            @PathVariable(value = "commentId") Long commentId,
            @Valid @RequestBody CommentDto commentDto

    ){
        return new ResponseEntity<>(commentService.updateComment(postId,commentId, commentDto), HttpStatus.OK);
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/post/{postId}/comment/{commentId}")
    public ResponseEntity<String> deleteComment(
            @PathVariable(value = "postId") Long postId,
            @PathVariable(value = "commentId") Long commentId

    ){
        commentService.deleteComment(postId,commentId);
        return new ResponseEntity<>("The comment with comment Id: " + commentId + " and post id: " + postId + " is deleted successfully.", HttpStatus.OK);
    }
}
