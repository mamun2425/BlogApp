package com.springboot.blog.blog.service;


import com.springboot.blog.blog.entity.Comment;
import com.springboot.blog.blog.entity.Post;
import com.springboot.blog.blog.exception.MyBlogException;
import com.springboot.blog.blog.exception.ResourceNotFoundException;
import com.springboot.blog.blog.payload.CommentDto;
import com.springboot.blog.blog.repository.CommentRepository;
import com.springboot.blog.blog.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;


    public CommentService(CommentRepository commentRepository, PostRepository postRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    public CommentDto createComment(Long postId, CommentDto commentDto){

        Comment comment = mapToEntity(commentDto);
        Post post = postRepository.findById(postId).
                orElseThrow(() ->
                        new ResourceNotFoundException("Post", "id", postId));
        // set post to comment entity
        comment.setPost(post);

        commentRepository.save(comment);
        return mapToDTO(comment);
    }



    public List<CommentDto> readComment(Long postId) {
//        Post post = postRepository.findById(postId).
//                orElseThrow(() -> new
//                        ResourceNotFoundException("post","postId", postId));
//
//        List<Comment> comments = (List<Comment>) post.getComments();
//        List<CommentDto> listOfComments = comments.stream().
//                map(comment -> mapToDTO(comment)).
//                collect(Collectors.toList());
//
//        return listOfComments;
        List<Comment> comments = commentRepository.findByPostId(postId);

        return comments.stream().
                map(this::mapToDTO).collect(Collectors.toList());

    }

    public CommentDto readCommentByCommentId(Long postId, Long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new ResourceNotFoundException("post","postId", postId));

        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ResourceNotFoundException("comment", "commentId", commentId) );

        if(!comment.getPost().getId().equals(post.getId())){
            throw new MyBlogException(HttpStatus.BAD_REQUEST,"This comment is not belong to this post");

        }

        return mapToDTO(comment);

    }


    public CommentDto updateComment(Long postId, Long commentId, CommentDto commentDto) {
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new ResourceNotFoundException("post","postId", postId));

        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ResourceNotFoundException("comment", "commentId", commentId) );

        if(!comment.getPost().getId().equals(post.getId())){
            throw new MyBlogException(HttpStatus.BAD_REQUEST,"This comment is not belong to this post");

        }

        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());
        commentRepository.save(comment);

        return mapToDTO(comment);

    }


    public void deleteComment(Long postId, Long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new ResourceNotFoundException("post","postId", postId));

        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ResourceNotFoundException("comment", "commentId", commentId) );

        if(!comment.getPost().getId().equals(post.getId())){
            throw new MyBlogException(HttpStatus.BAD_REQUEST,"This comment is not belong to this post");

        }
        commentRepository.delete(comment);

    }



    // converting Comment entity to CommentDto
    private CommentDto mapToDTO(Comment comment){

//        CommentDto commentResponse = new CommentDto();
//        commentResponse.setId(comment.getId());
//        commentResponse.setName(comment.getName());
//        commentResponse.setEmail(comment.getEmail());
//        commentResponse.setBody(comment.getBody());
        return modelMapper.map(comment, CommentDto.class );
    }


    // converting CommentDto to Comment entity
    private Comment mapToEntity(CommentDto commentDto){

//        Comment comment = new Comment();
//        comment.setId(commentDto.getId());
//        comment.setName(commentDto.getName());
//        comment.setEmail(commentDto.getEmail());
//        comment.setBody(commentDto.getBody());

        return modelMapper.map(commentDto, Comment.class);
    }



}
