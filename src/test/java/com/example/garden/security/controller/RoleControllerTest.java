package com.example.garden.security.controller;

import com.example.garden.security.model.Role;
import com.example.garden.security.service.RoleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@WithMockUser(username = "admin", roles = {"ADMIN"})

public class RoleControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final RoleService roleService;

    @Autowired
    public RoleControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, RoleService roleService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.roleService = roleService;
    }

    @Test
    public void testThatCreateRoleSuccessfullyReturnsSaveRole() throws Exception {
        Role role = new Role("TEST_ROLE");
        String roleJson = objectMapper.writeValueAsString(role);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/garden/addRole")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(roleJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.name").value("TEST_ROLE"));
    }

    @Test
    public void testThatUpdateRoleSuccessfullyReturnsUpdatedRole() throws Exception {
        Role role = new Role("TEST_ROLE");
        Role savedRole = roleService.createRole(role);
        savedRole.setName("TEST_USER");
        String roleJson = objectMapper.writeValueAsString(savedRole);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/garden/updateRole")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(roleJson)
        ).andExpect(MockMvcResultMatchers.jsonPath("$.name").value("TEST_USER"));
    }

    @Test
    public void testThatFindRoleByIdSuccessfullyReturnsRole() throws Exception {
        Role role = new Role("TEST_ROLE");
        Role savedRole = roleService.createRole(role);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/garden/findRoleById/" + savedRole.getId())
        ).andExpect(MockMvcResultMatchers.jsonPath("$.name").value("TEST_ROLE"));
    }

    @Test
    public void testThatFindAllCategoriesSuccessfullyReturnsListOfCategories() throws Exception {
        Role role1 = new Role("TEST_ROLE");
        Role role2 = new Role("TEST_USER");
        roleService.createRole(role1);
        roleService.createRole(role2);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/garden/findAllRole")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("TEST_ROLE"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("TEST_USER"));
    }

    @Test
    public void testThatDeleteRoleSuccessfullyDeletesRole() throws Exception {
        Role role = new Role("TEST_ROLE");
        Role savedRole = roleService.createRole(role);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/garden/deleteRole/" + savedRole.getId())
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testThatNonAdminUserCannotAccessRoleEndpoints() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/garden/findAllRole")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is5xxServerError())
                .andExpect(MockMvcResultMatchers.content().string(
                        org.hamcrest.Matchers.containsString("Access Denied")
                ))
                .andReturn();;
    }
}
