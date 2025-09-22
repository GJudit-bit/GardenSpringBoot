package com.example.garden.controller;

import com.example.garden.model.Currency;
import com.example.garden.service.CurrencyService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class CurrencyTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final CurrencyService currencyService;

    @Autowired
    public CurrencyTest(MockMvc mockMvc, ObjectMapper objectMapper, CurrencyService currencyService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.currencyService = currencyService;
    }

    @Test
    public void addCurrency() throws Exception {
        Currency currency = new Currency("USD");
        mockMvc.perform(
                MockMvcRequestBuilders.post("/garden/addCurrency")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(currency))
        ).andExpect(MockMvcResultMatchers.jsonPath("$.name").value("USD"))
          .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void updateCurrency() throws Exception {
        Currency currency = new Currency("EUR");
        Currency savedCurrency=currencyService.createCurrency(currency);
        savedCurrency.setName("HUF");
        mockMvc.perform(
                MockMvcRequestBuilders.post("/garden/updateCurrency")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(savedCurrency))
        ).andExpect(MockMvcResultMatchers.jsonPath("$.name").value("HUF"))
          .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteCurrency() throws Exception {
        Currency currency = new Currency("GBP");
        Currency savedCurrency = currencyService.createCurrency(currency);
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/garden/deleteCurrency/" + savedCurrency.getId())
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
        Currency currency = new Currency("JPY");
        Currency savedCurrency = currencyService.createCurrency(currency);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/garden/findCurrencyById/" + savedCurrency.getId())
        ).andExpect(MockMvcResultMatchers.jsonPath("$.name").value("JPY"))
          .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void findAllCurrencies() throws Exception {
        Currency currency = new Currency("JPY");
        Currency savedCurrency = currencyService.createCurrency(currency);

        Currency currency2 = new Currency("HUF");
        Currency savedCurrency2 = currencyService.createCurrency(currency2);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/garden/findAllCurrencies")
        ).andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("HUF"))
          .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("JPY"))
          .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
