package com.AcademicScope.controller;

import com.AcademicScope.auth.dto.LoginRequest;
import com.AcademicScope.enums.TipoAsistencia;
import com.AcademicScope.model.Asistencia;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AsistenciaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String docenteToken;
    private String estudianteToken;

    @BeforeEach
    void setUp() throws Exception {
        docenteToken = obtenerToken("profesor.mario@academicscope.com", "Docente2026");
        estudianteToken = obtenerToken("estudiante.pedro@academicscope.com", "Estudiante2026");
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
    void deberia_contar_faltas_como_estudiante() throws Exception {
        mockMvc.perform(get("/api/asistencias/faltas/11/curso/1")
                .header("Authorization", "Bearer " + estudianteToken))
                .andExpect(status().isOk());
    }

    @Test
    void deberia_listar_asistencias_por_estudiante_y_curso() throws Exception {
        mockMvc.perform(get("/api/asistencias/estudiante/11/curso/1")
                .header("Authorization", "Bearer " + estudianteToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void deberia_obtener_reporte_general() throws Exception {
        mockMvc.perform(get("/api/asistencias/reporte/general")
                .header("Authorization", "Bearer " + docenteToken))
                .andExpect(status().isOk());
    }
}
