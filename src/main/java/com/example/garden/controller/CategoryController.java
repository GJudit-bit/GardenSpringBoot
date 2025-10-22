package com.example.garden.controller;

import com.example.garden.dto.CategoryDto;
import com.example.garden.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/garden")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/addCategory")
    public CategoryDto create(@RequestBody CategoryDto categoryDto) {
        return categoryService.createCategory(categoryDto);
    }

    @PostMapping("/updateCategory")
    public CategoryDto update(@RequestBody CategoryDto categoryDto) {
        return categoryService.updateCategory(categoryDto);
    }

    @DeleteMapping("/deleteCategory/{id}")
    public void delete(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
    }

    @GetMapping("/findCategoryById/{id}")
    public CategoryDto findById(@PathVariable Integer id) {
        return categoryService.findById(id);
    }

    @GetMapping("/findAllCategory")
    public List<CategoryDto> findAll() {
        return categoryService.getAllCategories(Sort.by("name"));
    }

    @PostMapping("/addItemToCategory/{categoryId}/{itemId}")
    public void addItemToCategory(@PathVariable Integer categoryId, @PathVariable Integer itemId) {
        categoryService.addItemToCategory(categoryId, itemId);
    }

    @PostMapping("/removeItemFromCategory/{categoryId}/{itemId}")
    public void removeItemFromCategory(@PathVariable Integer categoryId, @PathVariable Integer itemId) {
        categoryService.removeItemFromCategory(categoryId, itemId);
    }

}
