package com.example.garden.controller;

import com.example.garden.dto.CurrencyDto;
import com.example.garden.service.CurrencyService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@WithMockUser(username = "testuser", roles = {"USER_ROLE"})
public class CurrencyControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final CurrencyService currencyService;
    private static CurrencyDto savedCurrencyDto;

    @Autowired
    public CurrencyControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, CurrencyService currencyService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.currencyService = currencyService;
    }

    @BeforeEach
    public void init() {
        CurrencyDto currencyDto = new CurrencyDto(null,"USD");
        savedCurrencyDto=currencyService.createCurrency(currencyDto);
    }

    @Test
    public void addCurrency() throws Exception {
        CurrencyDto currencyDto = new CurrencyDto(null,"HUF");
        mockMvc.perform(
                MockMvcRequestBuilders.post("/garden/addCurrency")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(currencyDto))
        ).andExpect(MockMvcResultMatchers.jsonPath("$.name").value("HUF"))
          .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void updateCurrency() throws Exception {
        CurrencyDto currencyDto=new CurrencyDto(savedCurrencyDto.id(),"HUF");
        mockMvc.perform(
                MockMvcRequestBuilders.post("/garden/updateCurrency")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(currencyDto))
        ).andExpect(MockMvcResultMatchers.jsonPath("$.name").value("HUF"))
          .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteCurrency() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/garden/deleteCurrency/" + savedCurrencyDto.id())
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteCurrencyThrowsExceptionWhenCurrencyDoesNotExist() throws Exception {
        String nonExistentId = "9990";
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/garden/deleteCurrency/" + nonExistentId)
                ).andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Currency with id " + nonExistentId+ " does not exist."));
    }

    @Test
    public void findCurrencyById() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/garden/findCurrencyById/" + savedCurrencyDto.id())
        ).andExpect(MockMvcResultMatchers.jsonPath("$.name").value("USD"))
          .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void findAllCurrencies() throws Exception {
        CurrencyDto currencyDto2 = new CurrencyDto(null,"JPY");
        CurrencyDto savedCurrencyDto2=currencyService.createCurrency(currencyDto2);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/garden/findAllCurrencies")
        ).andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("JPY"))
          .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("USD"))
          .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
