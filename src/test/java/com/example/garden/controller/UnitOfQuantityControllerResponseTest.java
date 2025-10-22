package com.example.garden.controller;

import com.example.garden.dto.UnitOfQuantityDto;
import com.example.garden.mapper.UnitOfQuantityMapper;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@WithMockUser(username = "testuser")
public class UnitOfQuantityControllerResponseTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UnitOfQuantityService unitOfQuantityService;
    private final UnitOfQuantityMapper unitOfQuantityMapper;
    private static UnitOfQuantityDto savedUnitOfQuantityDto = null;

    @Autowired
    public UnitOfQuantityControllerResponseTest(MockMvc mockMvc, ObjectMapper objectMapper, UnitOfQuantityService unitOfQuantityService, UnitOfQuantityMapper unitOfQuantityMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.unitOfQuantityService = unitOfQuantityService;
        this.unitOfQuantityMapper = unitOfQuantityMapper;
    }

    @BeforeEach
    public void init() {
        UnitOfQuantityDto unitOfQuantityDto = new UnitOfQuantityDto(null, "kilogramm");
        savedUnitOfQuantityDto = unitOfQuantityService.createUnitOfQuantity(unitOfQuantityDto);
    }

    @Test
    public void testThatCreateUnitOfQuantitySuccessfullyReturnsHttp200OK() throws Exception {
        UnitOfQuantityDto unitOfQuantityDto2 = new UnitOfQuantityDto(null, "gramm");
        String unitOfQuantityJson = objectMapper.writeValueAsString(unitOfQuantityDto2);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/garden/addUnitOfQuantity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(unitOfQuantityJson)
        ).andExpect(MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatUpdateUnitOfQuantitySuccessfullyReturnsHttp200Ok() throws Exception {

        UnitOfQuantity savedUnitOfQuantity = unitOfQuantityMapper.toEntity(savedUnitOfQuantityDto);
        savedUnitOfQuantity.setName("gramm");
        String savedUnitOfQuantityJson = objectMapper.writeValueAsString(savedUnitOfQuantity);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/garden/updateUnitOfQuantity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(savedUnitOfQuantityJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    public void testThatDeleteUnitOfQuantitySuccessfullyReturnsHttp200Ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/garden/deleteUnitOfQuantity/" + savedUnitOfQuantityDto.id())
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    public void testThatFindAllUnitOfQuantitiesSuccessfullyReturnsHttp200Ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/garden/findAllUnitOfQuantities")
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

}
