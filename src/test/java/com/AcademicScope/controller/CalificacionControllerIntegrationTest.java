package com.AcademicScope.controller;

import com.AcademicScope.auth.dto.LoginRequest;
import com.AcademicScope.model.Calificacion;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CalificacionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String docenteToken;
    private String estudianteToken;

    @BeforeEach
    void setUp() throws Exception {
        docenteToken = obtenerToken("mario@academicscope.com", "Profe@2026!");
        estudianteToken = obtenerToken("pedro@academicscope.com", "Estu@2026!");
    }

    private String obtenerToken(String username, String password) throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(password);
        String response = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response).get("token").asText();
    }

    @Test
    void deberia_calcular_color_azul() throws Exception {
        mockMvc.perform(post("/api/calificaciones/calcular-color")
                .header("Authorization", "Bearer " + docenteToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nota\": 19.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.color").value("AZUL"));
    }

    @Test
    void deberia_calcular_color_verde() throws Exception {
        mockMvc.perform(post("/api/calificaciones/calcular-color")
                .header("Authorization", "Bearer " + docenteToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nota\": 15.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.color").value("VERDE"));
    }

    @Test
    void deberia_calcular_color_rojo() throws Exception {
        mockMvc.perform(post("/api/calificaciones/calcular-color")
                .header("Authorization", "Bearer " + docenteToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nota\": 5.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.color").value("ROJO"));
    }

    @Test
    void deberia_calcular_color_gris_para_sin_nota() throws Exception {
        mockMvc.perform(post("/api/calificaciones/calcular-color")
                .header("Authorization", "Bearer " + docenteToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.color").value("GRIS"));
    }

    @Test
    void deberia_listar_calificaciones_por_estudiante() throws Exception {
        mockMvc.perform(get("/api/calificaciones/estudiante/11")
                .header("Authorization", "Bearer " + estudianteToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
