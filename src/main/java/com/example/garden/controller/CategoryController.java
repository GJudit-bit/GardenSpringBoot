package com.example.garden.controller;

import com.example.garden.model.Category;
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
    public Category create(@RequestBody Category category){
        return categoryService.createCategory(category);
    }

    @PostMapping("/updateCategory")
    public Category update(@RequestBody Category category) {
        return categoryService.updateCategory(category);

    }

    @DeleteMapping("/deleteCategory/{id}")
    public void delete(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
    }

    @GetMapping("/findCategoryById/{id}")
    public Category findById(@PathVariable Integer id) {
        return categoryService.getCategoryById(id);
    }

    @GetMapping("/findAllCategory")
    public List<Category> findAll() {

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
