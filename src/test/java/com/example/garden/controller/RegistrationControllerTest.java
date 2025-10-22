package com.example.garden.controller;

import com.example.garden.dto.*;
import com.example.garden.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@WithMockUser(username = "testuser", roles = {"USER"})
public class RegistrationControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final RegistrationService registrationService;
    private final ItemService itemService;
    private final CurrencyService currencyService;
    private final CategoryService categoryService;
    private final UnitOfQuantityService unitOfQuantityService;

    private CurrencyDto savedCurrencyDto;
    private ItemDto savedItemDto;
    private RegistrationDto savedRegistrationDto;


    @Autowired
    public RegistrationControllerTest(MockMvc mockMvc, RegistrationService registrationService, ItemService itemService, CurrencyService currencyService, CategoryService categoryService, UnitOfQuantityService unitOfQuantityService) {
        this.mockMvc = mockMvc;
        this.itemService = itemService;
        this.currencyService = currencyService;
        this.categoryService = categoryService;
        this.unitOfQuantityService = unitOfQuantityService;
        this.objectMapper = new ObjectMapper();
        this.registrationService = registrationService;
    }

    @BeforeEach
    public void initialize() {
        CurrencyDto currencyDto = new CurrencyDto(null, "HUF");
        savedCurrencyDto = currencyService.createCurrency(currencyDto);

        UnitOfQuantityDto unitOfQuantityDto = new UnitOfQuantityDto(null, "kilogramm");
        UnitOfQuantityDto savedUnitOfQuantityDto = unitOfQuantityService.createUnitOfQuantity(unitOfQuantityDto);

        CategoryDto categoryDto = new CategoryDto(null, "Vegetables");
        CategoryDto savedCategoryDto = categoryService.createCategory(categoryDto);

        ItemDto itemDto = new ItemDto(null, "Tomato", savedCategoryDto, savedUnitOfQuantityDto);
        savedItemDto = itemService.createItem(itemDto);

        RegistrationDto registrationDto = new RegistrationDto(null, savedItemDto.id(), savedCurrencyDto.id(), 100.0, 2.0, true);
        savedRegistrationDto = registrationService.createRegistration(registrationDto);
    }


    @Test
    public void testAddRegistration() throws Exception {
        RegistrationDto registrationDto = new RegistrationDto(null, savedItemDto.id(), savedCurrencyDto.id(), 100.0, 2.0, true);
        String registrationJson = objectMapper.writeValueAsString(registrationDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/garden/addRegistration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.itemId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currencyId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(100.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(2.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.expense").value(true))
        ;
    }

    @Test
    public void testUpdateRegistration() throws Exception {
        RegistrationDto registrationDto2 = new RegistrationDto(savedRegistrationDto.id(), savedItemDto.id(), savedCurrencyDto.id(), 120.0, 2.0, true);
        String registrationJson = objectMapper.writeValueAsString(registrationDto2);

        mockMvc.perform(MockMvcRequestBuilders.post("/garden/updateRegistration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(120.0));
    }

    @Test
    public void testGetRegistrationById() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/garden/getRegistrationById/" + savedRegistrationDto.id()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.itemId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currencyId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(100.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(2.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.expense").value(true));
    }

    @Test
    public void testGetAllRegistrations() throws Exception {

        RegistrationDto registrationDto2 = new RegistrationDto(null, savedItemDto.id(), savedCurrencyDto.id(), 1500.0, 2.0, false);
        registrationService.createRegistration(registrationDto2);

        mockMvc.perform(MockMvcRequestBuilders.get("/garden/getAllRegistrations"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2));
    }

    @Test
    public void testDeleteRegistration() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/garden/deleteRegistration/" + savedRegistrationDto.id()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/garden/findItemById/" + savedItemDto.id()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Tomato"));

        mockMvc.perform(MockMvcRequestBuilders.get("/garden/findCurrencyById/" + savedCurrencyDto.id()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("HUF"));
    }


    @Test
    public void testThatDeleteRegistrationThrowsExceptionWhenRegistrationDoesNotExist() throws Exception {
        String nonExistentId = "9990";
        mockMvc.perform(MockMvcRequestBuilders.delete("/garden/deleteRegistration/" + nonExistentId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @Test
    public void testFindRegistrationsByDate() throws Exception {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// Use the current date for the created registration

        String dateString = dateFormat.format(date); // Use a date that matches the created registration's date

        mockMvc.perform(MockMvcRequestBuilders.get("/garden/findRegistrationsByDate/" + dateString))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1));
    }

    @Test
    public void testFindRegistrationsByWeek() throws Exception {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);

        mockMvc.perform(MockMvcRequestBuilders.get("/garden/findRegistrationsByWeek/" + year + "/" + weekOfYear))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1));
    }

    @Test
    public void testFindRegistrationsByMonth() throws Exception {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH is zero-based

        mockMvc.perform(MockMvcRequestBuilders.get("/garden/findRegistrationsByMonth/" + year + "/" + month))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1));
    }

    @Test
    public void testFindRegistrationsByYear() throws Exception {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        mockMvc.perform(MockMvcRequestBuilders.get("/garden/findRegistrationsByYear/" + year))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1));
    }

    @Test
    public void testFindSumRegistrationsByDate() throws Exception {

        RegistrationDto registrationDto2 = new RegistrationDto(null, savedItemDto.id(), savedCurrencyDto.id(), 150.0, 3.0, false);
        registrationService.createRegistration(registrationDto2);

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// Use the current date for the created registration

        String dateString = dateFormat.format(date); // Use a date that matches the created registration's date

        mockMvc.perform(MockMvcRequestBuilders.get("/garden/findSumRegistrationsByDate/" + dateString))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testFindSumRegistrationsByWeek() throws Exception {
        RegistrationDto registrationDto2 = new RegistrationDto(null, savedItemDto.id(), savedCurrencyDto.id(), 150.0, 3.0, false);
        registrationService.createRegistration(registrationDto2);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);

        mockMvc.perform(MockMvcRequestBuilders.get("/garden/findSumRegistrationsByWeek/" + year + "/" + weekOfYear))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].sumValue").value(150.0));
    }

    @Test
    public void testFindSumRegistrationsByMonth() throws Exception {
        RegistrationDto registrationDto2 = new RegistrationDto(null, savedItemDto.id(), savedCurrencyDto.id(), 150.0, 3.0, false);
        registrationService.createRegistration(registrationDto2);


        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH is zero-based

        mockMvc.perform(MockMvcRequestBuilders.get("/garden/findSumRegistrationsByMonth/" + year + "/" + month))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].sumValue").value(150.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].expense").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].sumValue").value(100.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].expense").value(true));
    }

    @Test
    public void testFindSumRegistrationsByYear() throws Exception {
        RegistrationDto registrationDto2 = new RegistrationDto(null, savedItemDto.id(), savedCurrencyDto.id(), 150.0, 3.0, false);
        registrationService.createRegistration(registrationDto2);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        mockMvc.perform(MockMvcRequestBuilders.get("/garden/findSumRegistrationsByYear/" + year))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].sumValue").value(150.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].expense").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].sumValue").value(100.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].expense").value(true));
    }
}
