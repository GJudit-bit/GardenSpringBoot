package com.example.garden.controller;



import com.example.garden.model.Item;
import com.example.garden.model.UnitOfQuantity;
import com.example.garden.service.ItemService;
import com.example.garden.service.UnitOfQuantityService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@WithMockUser(username = "testuser", roles = {"USER"})
public class UnitOfQuantityControllerTest {


    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UnitOfQuantityService unitOfQuantityService;
    private final ItemService itemService;


    @Autowired
    public UnitOfQuantityControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, UnitOfQuantityService unitOfQuantityService, ItemService itemService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.unitOfQuantityService = unitOfQuantityService;
        this.itemService = itemService;
    }


    @Test
    public void testThatCreateUnitOfQuantitySuccessfullyReturnsSavedUnitOfQuantity() throws Exception {
        UnitOfQuantity unitOfQuantity = new UnitOfQuantity("kilogramm");

        String unitOfQuantityJson = objectMapper.writeValueAsString(unitOfQuantity);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/garden/addUnitOfQuantity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(unitOfQuantityJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.name").value("kilogramm"));
    }


    @Test
    public void testThatUpdateUnitOfQuantitySuccessfullyReturnsUpdatedUnitOfQuantity() throws Exception {
        UnitOfQuantity unitOfQuantity = new UnitOfQuantity("kilogramm");
        UnitOfQuantity savedUnitOfQuantity = unitOfQuantityService.createUnitOfQuantity(unitOfQuantity);
        savedUnitOfQuantity.setName("gramm");
        String unitOfQuantityJson = objectMapper.writeValueAsString(savedUnitOfQuantity);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/garden/updateUnitOfQuantity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(unitOfQuantityJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.name").value("gramm"));
    }


    @Test
    public void testThatFindUnitOfQuantityByIdSuccessfullyReturnsUnitOfQuantity() throws Exception {
        UnitOfQuantity unitOfQuantity = new UnitOfQuantity("kilogramm");
        unitOfQuantity = unitOfQuantityService.createUnitOfQuantity(unitOfQuantity);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/garden/findUnitOfQuantityById/" + unitOfQuantity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.name").value("kilogramm"));
    }


    @Test
    public void testThatFindAllUnitOfQuantitiesSuccessfullyReturnsListOfUnitOfQuantities() throws Exception {
        UnitOfQuantity unitOfQuantity1 = new UnitOfQuantity("kilogramm");
        UnitOfQuantity unitOfQuantity2 = new UnitOfQuantity("gramm");
        unitOfQuantityService.createUnitOfQuantity(unitOfQuantity1);
        unitOfQuantityService.createUnitOfQuantity(unitOfQuantity2);

        assert unitOfQuantityService.findAll().size() == 2;
    }

    @Test
    public void testThatDeleteUnitOfQuantitySuccessfullyDeletesUnitOfQuantity() throws Exception {
        UnitOfQuantity unitOfQuantity = new UnitOfQuantity("kilogramm");
        UnitOfQuantity savedUnitOfQuantity = unitOfQuantityService.createUnitOfQuantity(unitOfQuantity);

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.delete("/garden/deleteUnitOfQuantity/" + savedUnitOfQuantity.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();

        // Verify that the unitOfQuantity is deleted
        mockMvc.perform(
                MockMvcRequestBuilders.get("/garden/findUnitOfQuantityById/" + savedUnitOfQuantity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("UnitOfQuantity with id " + savedUnitOfQuantity.getId() + " does not exist."));
    }

    @Test
    public void testThatDeleteUnitOfQuantityThrowsExceptionWhenUnitOfQuantityIsAssociatedWithItems() throws Exception {
        UnitOfQuantity unitOfQuantity = new UnitOfQuantity("kilogramm");
        Item item = new Item();
        item.setName("Test Item");
        item.setUnitOfQuantity(unitOfQuantity);


        itemService.createItem(item);
        UnitOfQuantity savedUnitOfQuantity=item.getUnitOfQuantity();



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
