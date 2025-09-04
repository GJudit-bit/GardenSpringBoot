package com.example.garden.controller;

import com.example.garden.model.UnitOfQuantity;
import com.example.garden.service.ItemService;
import com.example.garden.service.UnitOfQuantityService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@WithMockUser(username = "testuser", roles = {"USER"})
public class UnitOfQuantityControllerResponseTest {

        private final MockMvc mockMvc;
        private final ObjectMapper objectMapper;
        private final UnitOfQuantityService unitOfQuantityService;
        private final ItemService itemService;


    @Autowired
    public UnitOfQuantityControllerResponseTest(MockMvc mockMvc, ObjectMapper objectMapper, UnitOfQuantityService unitOfQuantityService, ItemService itemService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.unitOfQuantityService = unitOfQuantityService;
        this.itemService = itemService;
    }

        @Test
        public void testThatCreateUnitOfQuantitySuccessfullyReturnsHttp200OK() throws Exception {
        UnitOfQuantity unitOfQuantity = new UnitOfQuantity("kilogramm");
        String unitOfQuantityJson = objectMapper.writeValueAsString(unitOfQuantity);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/garden/addUnitOfQuantity" )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(unitOfQuantityJson)
        ).andExpect(MockMvcResultMatchers.status().isOk()
        );
    }

        @Test
        public void testThatUpdateUnitOfQuantitySuccessfullyReturnsHttp200Ok() throws Exception {
        UnitOfQuantity unitOfQuantity = new UnitOfQuantity("kilogramm");
        UnitOfQuantity savedUnitOfQuantity = unitOfQuantityService.createUnitOfQuantity(unitOfQuantity);
            savedUnitOfQuantity.setName("gramm");
        String savedUnitOfQuantityJson = objectMapper.writeValueAsString(savedUnitOfQuantity);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/garden/updateUnitOfQuantity" )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(savedUnitOfQuantityJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }


        @Test
        public void testThatDeleteUnitOfQuantitySuccessfullyReturnsHttp200Ok() throws Exception {
        UnitOfQuantity unitOfQuantity = new UnitOfQuantity("kilogramm");
        UnitOfQuantity savedUnitOfQuantity = unitOfQuantityService.createUnitOfQuantity(unitOfQuantity);
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/garden/deleteUnitOfQuantity/"+ savedUnitOfQuantity.getId())
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }


        @Test
        public void testThatFindAllUnitOfQuantitiesSuccessfullyReturnsHttp200Ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/garden/findAllUnitOfQuantities" )
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

  /*      @Test
        public void testThatFindUnitOfQuantityByItemIdSuccessfullyReturnsHttp200Ok() throws Exception {
        UnitOfQuantity unitOfQuantity = new UnitOfQuantity("kilogramm");
   //     Item item= new Item();
  //      item.setName("Test Item");
      //  item.addUnitOfQuantity(unitOfQuantity);

        UnitOfQuantity unitOfQuantity2 = unitOfQuantityService.createUnitOfQuantity(unitOfQuantity);
   //     Item item2=itemService.createItem(item);


        mockMvc.perform(
                MockMvcRequestBuilders.post("/garden/findUnitOfQuantityByItemId" )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item2.getId()))
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }*/


}
