package com.AcademicScope.controller;

import com.AcademicScope.auth.dto.LoginRequest;
import com.AcademicScope.model.Grado;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GradoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String adminToken;
    private String estudianteToken;

    @BeforeEach
    void setUp() throws Exception {
        adminToken = obtenerToken("dennis@academicscope.com", "Admin2026");
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
    void deberia_listar_grados_como_admin() throws Exception {
        mockMvc.perform(get("/api/grados")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(org.hamcrest.Matchers.greaterThanOrEqualTo(5)));
    }

    @Test
    void deberia_listar_grados_por_nivel() throws Exception {
        mockMvc.perform(get("/api/grados/nivel/Primaria")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(org.hamcrest.Matchers.greaterThanOrEqualTo(5)));
    }

    @Test
    void deberia_obtener_grado_por_id() throws Exception {
        mockMvc.perform(get("/api/grados/1")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").exists());
    }

    @Test
    void deberia_crear_grado_como_admin() throws Exception {
        Grado nuevo = Grado.builder().nombre("6° Primaria").nivel("Primaria").build();

        mockMvc.perform(post("/api/grados")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nombre").value("6° Primaria"));
    }

    @Test
    void deberia_actualizar_grado_como_admin() throws Exception {
        Grado actualizado = Grado.builder().nombre("1° Primaria (Actualizado)").nivel("Primaria").build();

        mockMvc.perform(put("/api/grados/1")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("1° Primaria (Actualizado)"));
    }

    @Test
    void deberia_rechazar_crear_grado_sin_rol_admin() throws Exception {
        Grado nuevo = Grado.builder().nombre("Test").nivel("Primaria").build();

        mockMvc.perform(post("/api/grados")
                .header("Authorization", "Bearer " + estudianteToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deberia_eliminar_grado_como_admin() throws Exception {
        Grado temp = Grado.builder().nombre("Temp").nivel("Primaria").build();
        String response = mockMvc.perform(post("/api/grados")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(temp)))
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();
        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/grados/" + id)
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().is2xxSuccessful());
    }
}
