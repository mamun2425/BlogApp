package com.springboot.blog.blog.service;

import com.springboot.blog.blog.payload.PostDto;
import com.springboot.blog.blog.payload.PostResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface PostService {
     PostDto createPost(PostDto postDto);
     PostResponse readAllPost(int pageNo, int pageSize, String sortBy, String sortDir);

     PostDto readPostById(Long id);

     PostDto updatePost(PostDto postDto, Long postId);

     void deletePost(Long postId);
     List<PostDto>readPostByCategoryId(Long postId);
}
