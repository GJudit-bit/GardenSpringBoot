package com.example.garden.service;

import com.example.garden.model.Category;
import com.example.garden.model.Item;
import com.example.garden.repository.CategoryRepository;
import com.example.garden.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {


    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ItemRepository itemRepository) {
        this.categoryRepository = categoryRepository;
        this.itemRepository = itemRepository;
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }
    public Category getCategoryById(Integer id) {

        return categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Category with id " + id + " does not exist."));
    }
    public List<Category> getAllCategories(Sort sort) {
        return categoryRepository.findAll(sort);
    }

    public Category updateCategory(Category category) {
        return categoryRepository.save(category);
    }
    public void deleteCategory(Integer id) {
        Category category = categoryRepository.findById(id).orElse(null);

        if (category != null) {
            List<Item> items = category.getItems();

            if (items.isEmpty()) {
                categoryRepository.deleteById(id);
            } else {
                throw new IllegalStateException("Cannot delete Category with id " + id + " because it is associated with items.");
            }
        } else {
            throw new EntityNotFoundException("Category with id " + id + " does not exist.");
        }

    }

    public void addItemToCategory(Integer categoryId, Integer itemId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));
        category.addItem(item); // Add the item to the category
        item.addCategory(category);
        categoryRepository.save(category);
        itemRepository.save(item);
    }

    public void removeItemFromCategory(Integer categoryId, Integer itemId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));
        if(item.getCategory().equals(category)) {
            item.setCategory(null); // Remove the category from the item
        }
        category.getItems().remove(item); // Remove the item from the category
        itemRepository.save(item);
        categoryRepository.save(category);
    }
}
