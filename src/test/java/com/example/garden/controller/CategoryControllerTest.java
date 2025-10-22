package com.example.garden.controller;

import com.example.garden.dto.CategoryDto;
import com.example.garden.dto.ItemDto;
import com.example.garden.dto.UnitOfQuantityDto;
import com.example.garden.service.CategoryService;
import com.example.garden.service.ItemService;
import com.example.garden.service.UnitOfQuantityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
    private final UnitOfQuantityService unitOfQuantityService;
    private final CategoryService categoryService;
    private static CategoryDto savedCategoryDto = null;



    @Autowired
    public CategoryControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, ItemService itemService, UnitOfQuantityService unitOfQuantityService, CategoryService categoryService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.itemService = itemService;
        this.unitOfQuantityService = unitOfQuantityService;
        this.categoryService = categoryService;
    }

    @BeforeEach
    public void init() {
        CategoryDto categoryDto = new CategoryDto(null, "Vegetables");
        savedCategoryDto = categoryService.createCategory(categoryDto);
    }

    @Test
    public void testThatCreateCategorySuccessfullyReturnsSavedCategory() throws Exception {
       CategoryDto categoryDto2 = new CategoryDto(null, "Fruits");
       String categoryJson = objectMapper.writeValueAsString(categoryDto2);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/garden/addCategory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Fruits"));

    }


    @Test
    public void testThatUpdateCategorySuccessfullyReturnsUpdatedCategory() throws Exception {
        CategoryDto categoryDto2 = new CategoryDto(savedCategoryDto.id(),"Citrus Fruits");
        String categoryJson = objectMapper.writeValueAsString(categoryDto2);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/garden/updateCategory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Citrus Fruits"));
    }

    @Test
    public void testThatFindCategoryByIdSuccessfullyReturnsCategory() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/garden/findCategoryById/" + savedCategoryDto.id())
        ).andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Vegetables"));
    }


    @Test
    public void testThatFindAllCategoriesSuccessfullyReturnsListOfCategories() throws Exception {
        CategoryDto category1 = new CategoryDto(null,"Dairy");
        categoryService.createCategory(category1);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/garden/findAllCategory")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Dairy"))
         .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Vegetables"));
    }

    @Test
    public void testThatDeleteCategorySuccessfullyDeletesCategory() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/garden/deleteCategory/" + savedCategoryDto.id())
        ).andExpect(status().isOk());

        // Verify that the category is deleted
        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            categoryService.findById(savedCategoryDto.id());
        });

        assertTrue(exception.getMessage().contains("Category with id " + savedCategoryDto.id() + " does not exist."));

    }

    @Test
    public void testThatAddItemToCategorySuccessfullyAddsItem() throws Exception {
        UnitOfQuantityDto unitOfQuantityDto = new UnitOfQuantityDto(null, "Pieces");
        UnitOfQuantityDto savedUnitOfQuantityDto=unitOfQuantityService.createUnitOfQuantity(unitOfQuantityDto);
        ItemDto item = itemService.createItem(new ItemDto(null,"Chips", savedCategoryDto, savedUnitOfQuantityDto));

        categoryService.addItemToCategory(savedCategoryDto.id(), item.id());
        mockMvc.perform(
                MockMvcRequestBuilders.post("/garden/addItemToCategory/" + savedCategoryDto.id() + "/" + item.id())
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(status().isOk());


       MvcResult resultItem = mockMvc.perform(
                MockMvcRequestBuilders.get("/garden/findItemById/" + item.id())
        ).andDo(print()).andReturn();
        String contentItem = resultItem.getResponse().getContentAsString();
        Assertions.assertThat(contentItem).contains("\"categoryDto\":{\"id\":1,\"name\":\"Vegetables\"}");
    }

    @Test
    public void testThatRemoveItemFromCategorySuccessfullyRemovesItem() throws Exception {
        // First, add the item to the category
        UnitOfQuantityDto unitOfQuantityDto = new UnitOfQuantityDto(null, "Pieces");
        UnitOfQuantityDto savedUnitOfQuantityDto=unitOfQuantityService.createUnitOfQuantity(unitOfQuantityDto);
        ItemDto item = itemService.createItem(new ItemDto(null,"Chips", savedCategoryDto, savedUnitOfQuantityDto));

        categoryService.addItemToCategory(savedCategoryDto.id(), item.id());

       ItemDto savedItem=itemService.findById(item.id());
       assertTrue(savedItem.categoryDto() != null && savedItem.categoryDto().id().equals(savedCategoryDto.id()));


        mockMvc.perform(
                MockMvcRequestBuilders.post("/garden/removeItemFromCategory/" + savedCategoryDto.id().toString() + "/" + savedItem.id().toString())
                        .contentType(MediaType.APPLICATION_JSON)

        ).andExpect(status().isOk());

        ItemDto savedItemAfter=itemService.findById(savedItem.id());
        assertNull(savedItemAfter.categoryDto());
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
        UnitOfQuantityDto unitOfQuantityDto = new UnitOfQuantityDto(null, "Pieces");
        UnitOfQuantityDto savedUnitOfQuantityDto=unitOfQuantityService.createUnitOfQuantity(unitOfQuantityDto);
        ItemDto item = itemService.createItem(new ItemDto(null,"Chips", null, savedUnitOfQuantityDto));

        mockMvc.perform(
                    MockMvcRequestBuilders.post("/garden/addItemToCategory/" + nonExistentCategoryId + "/" + item.id().toString())
                            .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().is5xxServerError())
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Category not found"));
    }

    @Test
    public void testThatRemoveItemFromCategoryThrowsExceptionWhenCategoryDoesNotExist() throws Exception {
        String nonExistentCategoryDtoId = "9990";
        UnitOfQuantityDto unitOfQuantityDto = new UnitOfQuantityDto(null, "Pieces");
        UnitOfQuantityDto savedUnitOfQuantityDto=unitOfQuantityService.createUnitOfQuantity(unitOfQuantityDto);
        ItemDto item = itemService.createItem(new ItemDto(null,"Chips", null, savedUnitOfQuantityDto));

        mockMvc.perform(
                    MockMvcRequestBuilders.post("/garden/removeItemFromCategory/" + nonExistentCategoryDtoId + "/" + item.id().toString())
                            .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().is5xxServerError())
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Category not found."));;

    }

    @Test
    public void testThatRemoveItemFromCategoryThrowsExceptionWhenItemDoesNotExist() throws Exception {
           String nonExistentItemId = "9990";

            mockMvc.perform(
                    MockMvcRequestBuilders.post("/garden/removeItemFromCategory/" + savedCategoryDto.id().toString() + "/" + nonExistentItemId)
                            .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().is5xxServerError())
             .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Item not found"));
    }

    @Test
    public void testThatCreateCategoryThrowsExceptionWhenCategoryNameNotUnique() throws Exception {
        CategoryDto categoryWithSameName = new CategoryDto(null,"Vegetables");

        Exception exception = assertThrows(DataIntegrityViolationException.class, () -> {
            categoryService.createCategory(categoryWithSameName);;
        });

        assertNotNull(exception );
    }
}
