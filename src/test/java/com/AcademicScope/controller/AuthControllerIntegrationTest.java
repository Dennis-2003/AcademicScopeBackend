package com.AcademicScope.controller;

import com.AcademicScope.auth.dto.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deberia_login_exitoso_y_devolver_token() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("dennis@academicscope.com");
        request.setPassword("Admin2026");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.nombre").value("Dennis"))
                .andExpect(jsonPath("$.rol").value("ADMIN"));
    }

    @Test
    void deberia_fallar_login_con_credenciales_incorrectas() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("dennis@academicscope.com");
        request.setPassword("wrongpassword");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deberia_acceder_a_endpoint_protegido_con_token() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("dennis@academicscope.com");
        request.setPassword("Admin2026");

        String response = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String token = objectMapper.readTree(response).get("token").asText();
        long adminId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/api/usuarios/" + adminId)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Dennis"));
    }

    @Test
    void deberia_rechazar_solicitud_sin_token() throws Exception {
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isForbidden());
    }
}
