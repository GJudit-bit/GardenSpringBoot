package com.example.garden.controller;

import com.example.garden.model.Category;
import com.example.garden.model.Item;
import com.example.garden.service.CategoryService;
import com.example.garden.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@WithMockUser(username = "testuser", roles = {"USER"})
public class CategoryControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final ItemService itemService;
    private final CategoryService categoryService;



    @Autowired
    public CategoryControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, ItemService itemService, CategoryService categoryService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.itemService = itemService;
        this.categoryService = categoryService;
    }


    @Test
    public void testThatCreateCategorySuccessfullyReturnsSavedCategory() throws Exception {
        Category category = new Category("Vegetables");
        String categoryJson = objectMapper.writeValueAsString(category);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/garden/addCategory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Vegetables"));

    }

    @Test
    public void testThatCreateCategoryWithItemsSuccessfullyReturnsSavedCategory() throws Exception {
        Category category = new Category("Vegetables");
        Item item1= new Item("Carrot");
        Item item2= new Item("Potato");
        category.addItem(item1);
        category.addItem(item2);

        String categoryJson = objectMapper.writeValueAsString(category);
        String jsonInString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(category);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/garden/addCategory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Vegetables"));

    }

    @Test
    public void testThatUpdateCategorySuccessfullyReturnsUpdatedCategory() throws Exception {
        Category category = new Category("Fruits");
        Category savedCategory = categoryService.createCategory(category);
        savedCategory.setName("Citrus Fruits");
        String categoryJson = objectMapper.writeValueAsString(savedCategory);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/garden/updateCategory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Citrus Fruits"));
    }

    @Test
    public void testThatFindCategoryByIdSuccessfullyReturnsCategory() throws Exception {
        Category category = new Category("Grains");
        Category savedCategory = categoryService.createCategory(category);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/garden/findCategoryById/" + savedCategory.getId())
        ).andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Grains"));
    }


    @Test
    public void testThatFindAllCategoriesSuccessfullyReturnsListOfCategories() throws Exception {
        Category category1 = new Category("Dairy");
        Category category2 = new Category("Meat");
        categoryService.createCategory(category1);
        categoryService.createCategory(category2);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/garden/findAllCategory")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Dairy"))
         .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Meat"));
    }

    @Test
    public void testThatDeleteCategorySuccessfullyDeletesCategory() throws Exception {
        Category category = new Category("Beverages");
        Category savedCategory = categoryService.createCategory(category);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/garden/deleteCategory/" + savedCategory.getId())

        ).andExpect(status().isOk());

        // Verify that the category is deleted
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            categoryService.getCategoryById(savedCategory.getId());
        });

        assertTrue(exception.getMessage().contains("Category with id " + savedCategory.getId() + " does not exist."));

    }

    @Test
    public void testThatAddItemToCategorySuccessfullyAddsItem() throws Exception {
        Item item = itemService.createItem(new Item("Chips"));
        Category category = categoryService.createCategory(new Category("Snacks"));

        categoryService.addItemToCategory(category.getId(), item.getId());

        mockMvc.perform(
                MockMvcRequestBuilders.post("/garden/addItemToCategory/" + category.getId().toString() + "/" + item.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(status().isOk());


       MvcResult resultItem = mockMvc.perform(
                MockMvcRequestBuilders.get("/garden/findItemById/" + item.getId())
        ).andDo(print()).andReturn();
        String contentItem = resultItem.getResponse().getContentAsString();
        Assertions.assertThat(contentItem).contains("\"category\":{\"id\":1,\"name\":\"Snacks\"}");
    }

    @Test
    public void testThatRemoveItemFromCategorySuccessfullyRemovesItem() throws Exception {
        // First, add the item to the category
       Integer categoryId = categoryService.createCategory(new Category("Desserts")).getId();
       Integer itemId = itemService.createItem(new Item("Cake")).getId();
       categoryService.addItemToCategory(categoryId, itemId);

       Item savedItem=itemService.getItemById(itemId);
       assertTrue(savedItem.getCategory() != null && savedItem.getCategory().getId().equals(categoryId));


        mockMvc.perform(
                MockMvcRequestBuilders.post("/garden/removeItemFromCategory/" + categoryId.toString() + "/" + itemId.toString())
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(status().isOk());

        Item savedItemAfter=itemService.getItemById(itemId);
        assertNull(savedItemAfter.getCategory());
    }

    @Test
    public void testThatDeleteCategoryThrowsExceptionWhenCategoryDoesNotExist() throws Exception {
        String nonExistentId = "9990";
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/garden/deleteCategory/" + nonExistentId)
        ).andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Category with id " + nonExistentId+ " does not exist."));
    }

    @Test
    public void testThatFindCategoryByIdThrowsExceptionWhenCategoryDoesNotExist() throws Exception {
        String nonExistentId = "9990";
        mockMvc.perform(
                MockMvcRequestBuilders.get("/garden/findCategoryById/" + nonExistentId)
        ).andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Category with id " + nonExistentId+ " does not exist."));
    }

    @Test
    public void testThatAddItemToCategoryThrowsExceptionWhenCategoryDoesNotExist() throws Exception {
        String nonExistentCategoryId = "9990";
        Integer itemId = itemService.createItem(new Item("Soda")).getId();
            mockMvc.perform(
                    MockMvcRequestBuilders.post("/garden/addItemToCategory/" + nonExistentCategoryId + "/" + itemId.toString())
                            .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().is5xxServerError())
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Category not found"));
    }

    @Test
    public void testThatRemoveItemFromCategoryThrowsExceptionWhenCategoryDoesNotExist() throws Exception {
        String nonExistentCategoryId = "9990";
        Integer itemId = itemService.createItem(new Item("Juice")).getId();


            mockMvc.perform(
                    MockMvcRequestBuilders.post("/garden/removeItemFromCategory/" + nonExistentCategoryId + "/" + itemId.toString())
                            .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().is5xxServerError())
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Category not found."));;

    }

    @Test
    public void testThatRemoveItemFromCategoryThrowsExceptionWhenItemDoesNotExist() throws Exception {
        Category category = new Category("Beverages");
        Category savedCategory = categoryService.createCategory(category);

        String nonExistentItemId = "9990";


            mockMvc.perform(
                    MockMvcRequestBuilders.post("/garden/removeItemFromCategory/" + savedCategory.getId().toString() + "/" + nonExistentItemId)
                            .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().is5xxServerError())
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Item not found"));
    }

    @Test
    public void testThatCreateCategoryThrowsExceptionWhenCategoryNameNotUnique() throws Exception {
        Category category = new Category("Dairy");
        categoryService.createCategory(category);

        Category categoryWithSameName = new Category("Dairy");

        Exception exception = assertThrows(DataIntegrityViolationException.class, () -> {
            categoryService.createCategory(categoryWithSameName);;
        });

        assertNotNull(exception );
    }
}
