package com.example.garden.controller;

import com.example.garden.dto.CategoryDto;
import com.example.garden.dto.ItemDto;
import com.example.garden.dto.UnitOfQuantityDto;
import com.example.garden.model.Category;
import com.example.garden.model.Item;
import com.example.garden.model.UnitOfQuantity;
import com.example.garden.service.CategoryService;
import com.example.garden.service.ItemService;
import com.example.garden.service.UnitOfQuantityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@WithMockUser(username = "testuser", roles = {"USER"})
public class ItemControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final ItemService itemService;
    private final CategoryService categoryService;
    private final UnitOfQuantityService unitOfQuantityService;
    private ItemDto savedItemDto;


    @Autowired
    public ItemControllerTest(MockMvc mockMvc, ItemService itemService, CategoryService categoryService, UnitOfQuantityService unitOfQuantityService) {
       this.mockMvc = mockMvc;
        this.categoryService = categoryService;
        this.unitOfQuantityService = unitOfQuantityService;
        this.objectMapper = new ObjectMapper();
        this.itemService = itemService;
    }

    @BeforeEach
    public void init() {
        CategoryDto categoryDto = new CategoryDto(null, "Vegetables");
        CategoryDto savedCategoryDto = categoryService.createCategory(categoryDto);
        UnitOfQuantityDto unitOfQuantityDto = new UnitOfQuantityDto(null, "Pieces");
        UnitOfQuantityDto savedUnitOfQuantityDto=unitOfQuantityService.createUnitOfQuantity(unitOfQuantityDto);
        savedItemDto = itemService.createItem(new ItemDto(null,"Tomato", savedCategoryDto, savedUnitOfQuantityDto));

    }


    @Test
    public void testThatCreateItemSuccessfullyReturnsSavedItem() throws Exception {
        CategoryDto categoryDto = new CategoryDto(null, "Fruits");
        CategoryDto savedCategoryDto = categoryService.createCategory(categoryDto);
        UnitOfQuantityDto unitOfQuantityDto = new UnitOfQuantityDto(null, "kilogramm");
        UnitOfQuantityDto savedUnitOfQuantityDto=unitOfQuantityService.createUnitOfQuantity(unitOfQuantityDto);

        ItemDto itemDto = itemService.createItem(new ItemDto(null,"alma", savedCategoryDto, savedUnitOfQuantityDto));

        String itemJson = objectMapper.writeValueAsString(itemDto);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/garden/addItem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.name").value("alma"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categoryDto.name").value("Fruits"));
    }

    @Test
    public void testThatUpdateItemSuccessfullyReturnsUpdatedItem() throws Exception {
        ItemDto itemDto= new ItemDto(savedItemDto.id(),"alma",savedItemDto.categoryDto(),savedItemDto.unitOfQuantityDto());
        String itemJson = objectMapper.writeValueAsString(itemDto);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/garden/updateItem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.name").value("alma"));
    }


    @Test
    public void testThatFindItemByIdSuccessfullyReturnsItem() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/garden/findItemById/" + savedItemDto.id())
        ).andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Tomato"));
    }


    @Test
    public void testThatFindAllItemsSuccessfullyReturnsListOfItems() {
        ItemDto item = new ItemDto(null,"répa",null,null);
        itemService.createItem(item);

        assert itemService.getAllItems(Sort.by("name")).size() == 2;
    }

    @Test
    public void testThatDeleteItemSuccessfullyDeletesItem() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/garden/deleteItem/" + savedItemDto.id())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(content().string(""));
    }

    @Test
    public void testThatDeleteItemNotDeleteCategoryAndUnitOfQuantity() throws Exception {

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/garden/deleteItem/" + savedItemDto.id())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(content().string(""));


        assertNotNull(categoryService.findById(savedItemDto.categoryDto().id()));
        assertNotNull(unitOfQuantityService.findById(savedItemDto.unitOfQuantityDto().id()));
    }


    @Test
    public void testThatDeleteItemThrowsExceptionWhenItemDoesNotExist() throws Exception {
        Integer nonExistentId = 9990;
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/garden/deleteItem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nonExistentId))
        ).andExpect(MockMvcResultMatchers.status().is5xxServerError());
    }


}
