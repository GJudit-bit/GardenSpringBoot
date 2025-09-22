package com.example.garden.controller;

import com.example.garden.model.*;
import com.example.garden.service.*;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@WithMockUser(username = "testuser", roles = {"USER"})
public class RegistrationControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final RegistrationService registrationService;
    private final UnitOfQuantityService unitOfQuantityService;
    private final CategoryService categoryService;
    private final ItemService itemService;
    private final CurrencyService currencyService;

    @Autowired
    public RegistrationControllerTest(MockMvc mockMvc, RegistrationService registrationService, UnitOfQuantityService unitOfQuantityService, CategoryService categoryService, ItemService itemService, CurrencyService currencyService) {
        this.mockMvc = mockMvc;
        this.unitOfQuantityService = unitOfQuantityService;
        this.categoryService = categoryService;
        this.itemService = itemService;
        this.currencyService = currencyService;
        this.objectMapper = new ObjectMapper();
        this.registrationService = registrationService;
    }


    @Test
    public void testAddRegistration() throws Exception {
        UnitOfQuantity unitOfQuantity = new UnitOfQuantity("kilogramm");
        Category category = new Category("Vegetables");
        Item item = new Item("Tomato", category, unitOfQuantity);
        itemService.createItem(item);
        Currency currency = new Currency("HUF");
        currencyService.createCurrency(currency);

        Registration registration = new Registration(item, currency, 100.0, 2.0, true);
        String registrationJson = objectMapper.writeValueAsString(registration);

        mockMvc.perform(MockMvcRequestBuilders.post("/garden/addRegistration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.name").value("Tomato"))
              /*  .andExpect(MockMvcResultMatchers.jsonPath("$.currency.code").value("HUF"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(100.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(2.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.expense").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdBy").value("testuser"))*/
        ;
    }

    @Test
    public void testUpdateRegistration() throws Exception {
        UnitOfQuantity unitOfQuantity = new UnitOfQuantity("kilogramm");
        Category category = new Category("Vegetables");
        Item item = new Item("Tomato", category, unitOfQuantity);
        itemService.createItem(item);


        UnitOfQuantity unitOfQuantity2 = new UnitOfQuantity("db");
        Category category2 = new Category("Fruits");
        Item item2 = new Item("Pear", category2, unitOfQuantity2);
        itemService.createItem(item2);

        Currency currency = new Currency("HUF");
        currencyService.createCurrency(currency);

        Registration registration = new Registration(item, currency, 100.0, 2.0, true);
        registration = registrationService.createRegistration(registration);
        registration.setPrice(120.0);
        registration.setItem(item2);

        String registrationJson = objectMapper.writeValueAsString(registration);

        mockMvc.perform(MockMvcRequestBuilders.post("/garden/updateRegistration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(120.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdBy").value("testuser"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.name").value("Pear"));
    }

    @Test
    public void testGetRegistrationById() throws Exception {
        UnitOfQuantity unitOfQuantity = new UnitOfQuantity("kilogramm");
        Category category = new Category("Vegetables");
        Item item = new Item("Tomato", category, unitOfQuantity);
        itemService.createItem(item);
        Currency currency = new Currency("HUF");
        currencyService.createCurrency(currency);

        Registration registration = new Registration(item, currency, 100.0, 2.0, true);
        registration = registrationService.createRegistration(registration);

        mockMvc.perform(MockMvcRequestBuilders.get("/garden/getRegistrationById/" + registration.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.item.name").value("Tomato"))
          /*      .andExpect(MockMvcResultMatchers.jsonPath("$.currency.code").value("HUF"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(100.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(2.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.expense").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdBy").value("testuser"))*/;
    }

    @Test
    public void testGetAllRegistrations() throws Exception {
        UnitOfQuantity unitOfQuantity = new UnitOfQuantity("kilogramm");
        Category category = new Category("Vegetables");
        Item item = new Item("Tomato", category, unitOfQuantity);
        itemService.createItem(item);
        Currency currency = new Currency("HUF");
        currencyService.createCurrency(currency);

        Registration registration1 = new Registration(item, currency, 100.0, 2.0, true);
        registrationService.createRegistration(registration1);

        Registration registration2 = new Registration(item, currency, 150.0, 3.0, false);
        registrationService.createRegistration(registration2);

        mockMvc.perform(MockMvcRequestBuilders.get("/garden/getAllRegistrations"))
                .andExpect(MockMvcResultMatchers.status().isOk())
        /*        .andExpect(MockMvcResultMatchers.jsonPath("$[0].item.name").value("Tomato"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].currency.code").value("HUF"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].price").value(100.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].quantity").value(2.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].expense").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].item.name").value("Tomato"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].currency.code").value("HUF"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].price").value(150.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].quantity").value(3.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].expense").value(false))*/;
    }

    @Test
    public void testDeleteRegistration() throws Exception {
        UnitOfQuantity unitOfQuantity = new UnitOfQuantity("kilogramm");
        Category category = new Category("Vegetables");
        Item item = new Item("Tomato", category, unitOfQuantity);
        itemService.createItem(item);
        Currency currency = new Currency("HUF");
        currencyService.createCurrency(currency);

        Registration registration = new Registration(item, currency, 100.0, 2.0, true);
        registration = registrationService.createRegistration(registration);

        mockMvc.perform(MockMvcRequestBuilders.delete("/garden/deleteRegistration/" + registration.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());


        mockMvc.perform(MockMvcRequestBuilders.get("/garden/getRegistrationById/" + registration.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(""));

        mockMvc.perform(MockMvcRequestBuilders.get("/garden/findItemById/" + item.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Tomato"));

        mockMvc.perform(MockMvcRequestBuilders.get("/garden/findCurrencyById/" + currency.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("HUF"));
    }


    @Test
    public void testThatDeleteRegistrationThrowsExceptionWhenRegistrationDoesNotExist() throws Exception {
        String nonExistentId = "9990";
        mockMvc.perform(MockMvcRequestBuilders.delete("/garden/deleteRegistration/" + nonExistentId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());}


    @Test
    public void testFindRegistrationsByDate() throws Exception {
        UnitOfQuantity unitOfQuantity = new UnitOfQuantity("kilogramm");
        Category category = new Category("Vegetables");
        Item item = new Item("Tomato", category, unitOfQuantity);
        itemService.createItem(item);
        Currency currency = new Currency("HUF");
        currencyService.createCurrency(currency);

        Registration registration = new Registration(item, currency, 100.0, 2.0, true);
        registrationService.createRegistration(registration);

        Registration registration2 = new Registration(item, currency, 150.0, 3.0, false);
        registrationService.createRegistration(registration2);

        List<Registration> registrations = registrationService.getAllRegistrations();

        Date date= new Date();
        DateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");// Use the current date for the created registration

        String dateString = dateFormat.format(date); // Use a date that matches the created registration's date

        mockMvc.perform(MockMvcRequestBuilders.get("/garden/findRegistrationsByDate/" + dateString))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].item.name").value("Tomato"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].currency.name").value("HUF"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].price").value(150.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].quantity").value(3.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].expense").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].price").value(100.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].quantity").value(2.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].expense").value(true));
    }

    @Test
    public void testFindRegistrationsByWeek() throws Exception {
        UnitOfQuantity unitOfQuantity = new UnitOfQuantity("kilogramm");
        Category category = new Category("Vegetables");
        Item item = new Item("Tomato", category, unitOfQuantity);
        itemService.createItem(item);
        Currency currency = new Currency("HUF");
        currencyService.createCurrency(currency);

        Registration registration1 = new Registration(item, currency, 100.0, 2.0, true);
        registrationService.createRegistration(registration1);

        Registration registration2 = new Registration(item, currency, 150.0, 3.0, false);
        registrationService.createRegistration(registration2);


       Calendar calendar= Calendar.getInstance();
       int year= calendar.get(Calendar.YEAR);
       int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);



        mockMvc.perform(MockMvcRequestBuilders.get("/garden/findRegistrationsByWeek/" + year + "/" + weekOfYear))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].item.name").value("Tomato"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].currency.name").value("HUF"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].price").value(150.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].quantity").value(3.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].expense").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].price").value(100.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].quantity").value(2.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].expense").value(true));
    }

    @Test
    public void testFindRegistrationsByMonth() throws Exception {
        UnitOfQuantity unitOfQuantity = new UnitOfQuantity("kilogramm");
        Category category = new Category("Vegetables");
        Item item = new Item("Tomato", category, unitOfQuantity);
        itemService.createItem(item);
        Currency currency = new Currency("HUF");
        currencyService.createCurrency(currency);

        Registration registration1 = new Registration(item, currency, 100.0, 2.0, true);
        registrationService.createRegistration(registration1);

        Registration registration2 = new Registration(item, currency, 150.0, 3.0, false);
        registrationService.createRegistration(registration2);

        Calendar calendar= Calendar.getInstance();
        int year= calendar.get(Calendar.YEAR);
        int month= calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH is zero-based

        mockMvc.perform(MockMvcRequestBuilders.get("/garden/findRegistrationsByMonth/" + year + "/" + month))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].item.name").value("Tomato"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].currency.name").value("HUF"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].price").value(150.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].quantity").value(3.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].expense").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].price").value(100.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].quantity").value(2.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].expense").value(true));
    }

    @Test
    public void testFindRegistrationsByYear() throws Exception {
        UnitOfQuantity unitOfQuantity = new UnitOfQuantity("kilogramm");
        Category category = new Category("Vegetables");
        Item item = new Item("Tomato", category, unitOfQuantity);
        itemService.createItem(item);
        Currency currency = new Currency("HUF");
        currencyService.createCurrency(currency);

        Registration registration1 = new Registration(item, currency, 100.0, 2.0, true);
        registrationService.createRegistration(registration1);

        Registration registration2 = new Registration(item, currency, 150.0, 3.0, false);
        registrationService.createRegistration(registration2);

        Calendar calendar= Calendar.getInstance();
        int year= calendar.get(Calendar.YEAR);

        mockMvc.perform(MockMvcRequestBuilders.get("/garden/findRegistrationsByYear/" + year))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].item.name").value("Tomato"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].currency.name").value("HUF"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].price").value(150.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].quantity").value(3.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].expense").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].price").value(100.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].quantity").value(2.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].expense").value(true));
    }

    @Test
    public void testFindSumRegistrationsByDate() throws Exception {
        UnitOfQuantity unitOfQuantity = new UnitOfQuantity("kilogramm");
        Category category = new Category("Vegetables");
        Item item = new Item("Tomato", category, unitOfQuantity);
        itemService.createItem(item);
        Currency currency = new Currency("HUF");
        currencyService.createCurrency(currency);

        Registration registration1 = new Registration(item, currency, 100.0, 2.0, true);
        registrationService.createRegistration(registration1);

        Registration registration2 = new Registration(item, currency, 150.0, 3.0, false);
        registrationService.createRegistration(registration2);

        Date date= new Date();
        DateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");// Use the current date for the created registration

        String dateString = dateFormat.format(date); // Use a date that matches the created registration's date

        mockMvc.perform(MockMvcRequestBuilders.get("/garden/findSumRegistrationsByDate/" + dateString))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testFindSumRegistrationsByWeek() throws Exception {
        UnitOfQuantity unitOfQuantity = new UnitOfQuantity("kilogramm");
        Category category = new Category("Vegetables");
        Item item = new Item("Tomato", category, unitOfQuantity);
        itemService.createItem(item);
        Currency currency = new Currency("HUF");
        currencyService.createCurrency(currency);

        Registration registration1 = new Registration(item, currency, 100.0, 2.0, true);
        registrationService.createRegistration(registration1);

        Registration registration2 = new Registration(item, currency, 150.0, 3.0, false);
        registrationService.createRegistration(registration2);

        Calendar calendar= Calendar.getInstance();
        int year= calendar.get(Calendar.YEAR);
        int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);

        mockMvc.perform(MockMvcRequestBuilders.get("/garden/findSumRegistrationsByWeek/" + year + "/" + weekOfYear))
                .andExpect(MockMvcResultMatchers.status().isOk())
              /*  .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("HUF"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].sumValue").value(150.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].expense").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].sumValue").value(100.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].expense").value(true))*/;
    }

    @Test
    public void testFindSumRegistrationsByMonth() throws Exception {
        UnitOfQuantity unitOfQuantity = new UnitOfQuantity("kilogramm");
        Category category = new Category("Vegetables");
        Item item = new Item("Tomato", category, unitOfQuantity);
        itemService.createItem(item);
        Currency currency = new Currency("HUF");
        currencyService.createCurrency(currency);

        Registration registration1 = new Registration(item, currency, 100.0, 2.0, true);
        registrationService.createRegistration(registration1);

        Registration registration2 = new Registration(item, currency, 150.0, 3.0, false);
        registrationService.createRegistration(registration2);

        Calendar calendar= Calendar.getInstance();
        int year= calendar.get(Calendar.YEAR);
        int month= calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH is zero-based

        mockMvc.perform(MockMvcRequestBuilders.get("/garden/findSumRegistrationsByMonth/" + year + "/" + month))
                .andExpect(MockMvcResultMatchers.status().isOk())
        /*        .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("HUF"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].sumValue").value(150.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].expense").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].sumValue").value(100.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].expense").value(true))*/;
    }

    @Test
    public void testFindSumRegistrationsByYear() throws Exception {
        UnitOfQuantity unitOfQuantity = new UnitOfQuantity("kilogramm");
        Category category = new Category("Vegetables");
        Item item = new Item("Tomato", category, unitOfQuantity);
        itemService.createItem(item);
        Currency currency = new Currency("HUF");
        currencyService.createCurrency(currency);

        Registration registration1 = new Registration(item, currency, 100.0, 2.0, true);
        registrationService.createRegistration(registration1);

        Registration registration2 = new Registration(item, currency, 150.0, 3.0, false);
        registrationService.createRegistration(registration2);

        Calendar calendar= Calendar.getInstance();
        int year= calendar.get(Calendar.YEAR);

        mockMvc.perform(MockMvcRequestBuilders.get("/garden/findSumRegistrationsByYear/" + year))
                .andExpect(MockMvcResultMatchers.status().isOk())
        /*        .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("HUF"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].sumValue").value(150.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].expense").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].sumValue").value(100.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].expense").value(true))*/;
    }
}
