package com.example.garden.security.controller;

import com.example.garden.model.Currency;
import com.example.garden.security.model.Role;
import com.example.garden.security.model.User;
import com.example.garden.security.repository.RoleRepository;
import com.example.garden.service.CurrencyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AuthControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final RoleRepository roleRepository;

    @Autowired
    public AuthControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, RoleRepository roleRepository) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.roleRepository = roleRepository;
    }

    @Test
    public void registerTest() throws Exception {
        Role role= new Role("ROLE_USER");
        roleRepository.save(role);
        User user = new User("testuser", "password123");
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/garden/register")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(user))
                ).andExpect(MockMvcResultMatchers.status().isOk());
    }

}
