package com.springboot.blog.blog.controller;



import com.springboot.blog.blog.payload.PostDto;
import com.springboot.blog.blog.payload.PostResponse;
import com.springboot.blog.blog.service.PostService;
import com.springboot.blog.blog.utils.AppConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "CRUD Rest API for Post resources"
)
@RestController
@RequestMapping("/api/blog/post")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }


    @GetMapping()
    public PostResponse readAllPost(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){

        return postService.readAllPost(pageNo,pageSize,sortBy,sortDir);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> readPostById(@PathVariable(name = "id") Long postId){

        return ResponseEntity.ok().body(postService.readPostById(postId));
    }
    // This is the normal getMapping without pagination. so updated this for pagination and commented this out.

//    @GetMapping()
//    public ResponseEntity<List<PostDto>> readAllPost(){
//        return ResponseEntity.ok().body(postService.readAllPost());
//    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<PostDto>> readPostByCategoryId(@PathVariable(name = "categoryId") Long categoryId){

       List<PostDto> postDtoList = postService.readPostByCategoryId(categoryId);
       return new ResponseEntity<>(postDtoList,HttpStatus.OK);
    }



    //security requirement field for swagger ui
    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto){

        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);

    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable(name="id") Long postId,
                                              @Valid @RequestBody PostDto postDto

    ){

        return ResponseEntity.ok().body(postService.updatePost(postDto, postId));
    }

    @SecurityRequirement(
            name = "Bearer Authentication"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable(name = "id") Long postId){
        postService.deletePost(postId);
        return new ResponseEntity<>("This post is deleted successfully", HttpStatus.OK);
    }




}
