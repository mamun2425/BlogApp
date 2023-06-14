package com.springboot.blog.blog.service;

import com.springboot.blog.blog.entity.Category;
import com.springboot.blog.blog.exception.ResourceNotFoundException;
import com.springboot.blog.blog.payload.CategoryDto;
import com.springboot.blog.blog.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    public CategoryService(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;

        this.modelMapper = modelMapper;
    }


    public CategoryDto createCategory(CategoryDto categoryDto) {

        Category category = modelMapper.map(categoryDto,Category.class);
        Category response = categoryRepository.save(category);

        return modelMapper.map(response,CategoryDto.class);
    }


    public List<CategoryDto> getAllCategory() {

        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
    }

    public CategoryDto getSingleCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("category", "id", categoryId));

        return modelMapper.map(category, CategoryDto.class);
    }

    public CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("category", "id", categoryId));
        category.setId(categoryId);
        if(categoryDto.getName()!= null){
            category.setName(categoryDto.getName());
        }
        if(categoryDto.getDescription()!=null){
            category.setDescription(categoryDto.getDescription());
        }

        Category categoryUpdated = categoryRepository.save(category);
        return modelMapper.map(categoryUpdated, CategoryDto.class);
    }

    public String deleteCategory(Long categoryId) {
        categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category", "id", categoryId));
        categoryRepository.deleteById(categoryId);
        return "Category with id: " + categoryId + " deleted successfully!";
    }
}

