package com.springboot.blog.blog.service.impl;

import com.springboot.blog.blog.entity.Category;
import com.springboot.blog.blog.entity.Post;
import com.springboot.blog.blog.exception.ResourceNotFoundException;
import com.springboot.blog.blog.payload.PostDto;
import com.springboot.blog.blog.payload.PostResponse;
import com.springboot.blog.blog.repository.CategoryRepository;
import com.springboot.blog.blog.repository.PostRepository;
import com.springboot.blog.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    public PostServiceImpl(CategoryRepository categoryRepository, PostRepository postRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PostDto createPost(PostDto postDto){

        // convert Dto/json to object
        Post post = mapToEntity(postDto);
        Category category = categoryRepository.findById(postDto.getCategoryId()).orElseThrow(()-> new ResourceNotFoundException("Category", "id", postDto.getCategoryId()));
        post.setCategory(category);

        Post newPost = postRepository.save(post);

        //convert entity to DTO to return postDto

        return mapToDTO(newPost);

    }


    @Override
    public PostResponse readAllPost(int pageNo, int pageSize, String sortBy, String sortDir){

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();

        Pageable pageable= PageRequest.of(pageNo, pageSize, sort);
        Page<Post> posts = postRepository.findAll(pageable);

        List<Post> listOfPosts = posts.getContent();
        List<PostDto> content = listOfPosts.stream().
                map(post -> mapToDTO(post)).
                collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());


        return postResponse;

    }

    @Override
    public PostDto readPostById(Long id){
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("post", "id",id));
        return mapToDTO(post);

    }

    @Override
    public List<PostDto> readPostByCategoryId(Long categoryId){
        Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category", "id", categoryId));
        List<Post> posts = postRepository.findByCategoryId(category.getId());
        return posts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());
    }

    @Override
    public PostDto updatePost(PostDto postDto, Long postId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("post", "id",postId));
        Category category = categoryRepository.findById(postDto.getCategoryId()).orElseThrow(()-> new ResourceNotFoundException("Category", "id", postDto.getCategoryId()));

        if(postDto.getTitle()!= null){
            post.setTitle(postDto.getTitle());
        }
        if(postDto.getContent()!= null){
            post.setContent(postDto.getContent());
        }
        if(postDto.getDescription()!= null){
            post.setDescription(postDto.getDescription());
        }
        post.setCategory(category);

        postRepository.save(post);

        return mapToDTO(post);

    }


    @Override
    public void deletePost(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("post", "id",postId));
        PostDto responseDto = mapToDTO(post);
        postRepository.delete(post);
    }


    // converting Post entity to postDto
    private PostDto mapToDTO(Post post){

//        converting using Model Mapper. That's why manual code is commented out.
//        PostDto postResponse = new PostDto();
//        postResponse.setId(post.getId());
//        postResponse.setTitle(post.getTitle());
//        postResponse.setDescription(post.getDescription());
//        postResponse.setContent(post.getContent());

        return modelMapper.map(post, PostDto.class);
    }


    // converting postDto to Post entity
    private Post mapToEntity(PostDto postDto){

        //        Post post = new Post();
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
        return modelMapper.map(postDto,Post.class);
    }



}
