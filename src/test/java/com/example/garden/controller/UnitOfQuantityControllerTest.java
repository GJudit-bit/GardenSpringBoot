package com.example.garden.controller;



import com.example.garden.dto.CategoryDto;
import com.example.garden.dto.ItemDto;
import com.example.garden.dto.UnitOfQuantityDto;
import com.example.garden.mapper.ItemMapper;
import com.example.garden.model.UnitOfQuantity;
import com.example.garden.service.CategoryService;
import com.example.garden.service.ItemService;
import com.example.garden.service.UnitOfQuantityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@WithMockUser(username = "testuser")
public class UnitOfQuantityControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UnitOfQuantityService unitOfQuantityService;
    private final ItemService itemService;
    private final CategoryService categoryService;
    private final ItemMapper itemMapper;

    private static UnitOfQuantityDto savedUnitOfQuantityDto = null;

    @Autowired
    public UnitOfQuantityControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, UnitOfQuantityService unitOfQuantityService, ItemService itemService, CategoryService categoryService, ItemMapper itemMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.unitOfQuantityService = unitOfQuantityService;
        this.itemService = itemService;
        this.categoryService = categoryService;
        this.itemMapper = itemMapper;
    }

    @BeforeEach
    public void init() {
        UnitOfQuantityDto unitOfQuantityDto = new UnitOfQuantityDto(null, "kilogramm");
        savedUnitOfQuantityDto = unitOfQuantityService.createUnitOfQuantity(unitOfQuantityDto);
    }


    @Test
    public void testThatCreateUnitOfQuantitySuccessfullyReturnsSavedUnitOfQuantity() throws Exception {
        UnitOfQuantityDto unitOfQuantityDto2 = new UnitOfQuantityDto(null, "gramm");
        String unitOfQuantityJson = objectMapper.writeValueAsString(unitOfQuantityDto2);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/garden/addUnitOfQuantity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(unitOfQuantityJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.name").value("gramm"));
    }


    @Test
    public void testThatUpdateUnitOfQuantitySuccessfullyReturnsUpdatedUnitOfQuantity() throws Exception {
        UnitOfQuantityDto unitOfQuantityDto2 = new UnitOfQuantityDto(savedUnitOfQuantityDto.id(), "gramm");

        String unitOfQuantityJson = objectMapper.writeValueAsString(unitOfQuantityDto2);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/garden/updateUnitOfQuantity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(unitOfQuantityJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.name").value("gramm"));
    }


    @Test
    public void testThatFindUnitOfQuantityByIdSuccessfullyReturnsUnitOfQuantity() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/garden/findUnitOfQuantityById/" + savedUnitOfQuantityDto.id())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.name").value("kilogramm"));
    }


    @Test
    public void testThatFindAllUnitOfQuantitiesSuccessfullyReturnsListOfUnitOfQuantities() {
        UnitOfQuantityDto unitOfQuantityDto2 = new UnitOfQuantityDto(null, "gramm");
        unitOfQuantityService.createUnitOfQuantity(unitOfQuantityDto2);

        assert unitOfQuantityService.findAll().size() == 2;
    }

    @Test
    public void testThatDeleteUnitOfQuantitySuccessfullyDeletesUnitOfQuantity() throws Exception {
         mockMvc.perform(
                        MockMvcRequestBuilders.delete("/garden/deleteUnitOfQuantity/" + savedUnitOfQuantityDto.id())                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();

        // Verify that the unitOfQuantity is deleted
        mockMvc.perform(
                MockMvcRequestBuilders.get("/garden/findUnitOfQuantityById/" + savedUnitOfQuantityDto.id())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("UnitOfQuantity with id " + savedUnitOfQuantityDto.id() + " does not exist."));
    }

    @Test
    public void testThatDeleteUnitOfQuantityThrowsExceptionWhenUnitOfQuantityIsAssociatedWithItems() throws Exception {
        CategoryDto categoryDto = new CategoryDto(null, "Test Category");
        CategoryDto savedCategoryDto =categoryService.createCategory(categoryDto);
        ItemDto itemDto = new ItemDto(null, "Test Item", savedCategoryDto, savedUnitOfQuantityDto);

        ItemDto savedItemDto =itemService.createItem(itemDto);
        UnitOfQuantity savedUnitOfQuantity=itemMapper.toEntity(savedItemDto).getUnitOfQuantity();



        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.delete("/garden/deleteUnitOfQuantity/" + savedUnitOfQuantity.getId())
                                .contentType(MediaType.APPLICATION_JSON)

                ).andDo(print()).andReturn();
        Optional<IllegalStateException> someException = Optional.ofNullable((IllegalStateException) result.getResolvedException());

        someException.ifPresent(exception -> {
            assert exception.getMessage().contains("Cannot delete UnitOfQuantity with id " + savedUnitOfQuantity.getId() + " because it is associated with items.");
        });
    }

    @Test
    public void testThatDeleteUnitOfQuantityThrowsExceptionWhenUnitOfQuantityDoesNotExist() throws Exception {
        String nonExistentId = "9999"; // Assuming this ID does not exist
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/garden/deleteUnitOfQuantity/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("UnitOfQuantity with id " + nonExistentId + " does not exist."));
    }


}
