package com.example.garden.controller;

import com.example.garden.model.Category;
import com.example.garden.model.Item;
import com.example.garden.model.UnitOfQuantity;
import com.example.garden.service.CategoryService;
import com.example.garden.service.ItemService;
import com.example.garden.service.UnitOfQuantityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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


    @Autowired
    public ItemControllerTest(MockMvc mockMvc, ItemService itemService, CategoryService categoryService, UnitOfQuantityService unitOfQuantityService) {

        this.mockMvc = mockMvc;
        this.categoryService = categoryService;
        this.unitOfQuantityService = unitOfQuantityService;
        this.objectMapper = new ObjectMapper();
        this.itemService = itemService;
    }


    @Test
    public void testThatCreateItemSuccessfullyReturnsSavedItem() throws Exception {
        Item item = new Item("alma");

        String itemJson = objectMapper.writeValueAsString(item);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/garden/addItem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.name").value("alma")
        );
    }

    @Test
    public void testThatCreateItemWithCategoryAndUnitOfQuantitySuccessfullyReturnsSavedItem() throws Exception {
        UnitOfQuantity unitOfQuantity = new UnitOfQuantity("kg");
        Category category = new Category("zöldség");
        Item item = new Item("répa");
        item.addCategory(category);
        item.setUnitOfQuantity(unitOfQuantity);

        String itemJson = objectMapper.writeValueAsString(item);
        System.out.println("Item:"+itemJson);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/garden/addItem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.name").value("répa")
        );
        }


    @Test
    public void testThatUpdateItemSuccessfullyReturnsUpdatedItem() throws Exception {
        Item item = new Item("alma");
        Item savedItem = itemService.createItem(item);
        savedItem.setName("repa");
        String itemJson = objectMapper.writeValueAsString(savedItem);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/garden/updateItem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.name").value("repa"));
    }


    @Test
    public void testThatFindItemByIdSuccessfullyReturnsItem() throws Exception {
        Item item = new Item("alma");
        Item savedItem = itemService.createItem(item);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/garden/findItemById/" + savedItem.getId())
        ).andExpect(MockMvcResultMatchers.jsonPath("$.name").value("alma"));
    }


    @Test
    public void testThatFindAllItemsSuccessfullyReturnsListOfItems() {
        Item item1 = new Item("alma");
        Item item2 = new Item("répa");
        itemService.createItem(item1);
        itemService.createItem(item2);

        assert itemService.getAllItems(Sort.by("name")).size() == 2;
    }

    @Test
    public void testThatDeleteItemSuccessfullyDeletesItem() throws Exception {
        Item item = new Item("alma");
        Item savedItem = itemService.createItem(item);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/garden/deleteItem/" + savedItem.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(content().string(""));
    }

    @Test
    public void testThatDeleteItemNotDeleteCategoryAndUnitOfQuantity() throws Exception {
        UnitOfQuantity unitOfQuantity = new UnitOfQuantity("kg");
        Category category = new Category("zöldség");
        Item item = new Item("répa");
        item.addCategory(category);
        item.setUnitOfQuantity(unitOfQuantity);
        Item savedItem = itemService.createItem(item);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/garden/deleteItem/" + savedItem.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(content().string(""));


        assertNotNull(categoryService.getCategoryById(category.getId()));
        assertNotNull(unitOfQuantityService.findById(unitOfQuantity.getId()));
    }


    @Test
    public void testThatDeleteItemThrowsExceptionWhenItemDoesNotExist() throws Exception {
        Integer nonExistentId = 9990;
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/garden/deleteItem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nonExistentId))
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    //test that delete item not works when item has categories or unit of quantity or registration
}
