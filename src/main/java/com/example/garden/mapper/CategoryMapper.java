package com.example.garden.mapper;

import com.example.garden.dto.CategoryDto;
import com.example.garden.model.Category;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class CategoryMapper  {

    public Category toEntity(CategoryDto categoryDto) {
        Category category = new Category();
        category.setId(categoryDto.id());
        category.setName(categoryDto.name());
        return category;
    }

    public CategoryDto toCategoryDto(Category category) {
        Integer id = category.getId();
        String name = category.getName();
        return new CategoryDto(id, name);
    }

    public static List<CategoryDto> toCategoryDtoList(List<Category> categories) {
        return categories.stream()
                .map(category -> new CategoryDto(category.getId(), category.getName()))
                .collect(Collectors.toList());
    }
}