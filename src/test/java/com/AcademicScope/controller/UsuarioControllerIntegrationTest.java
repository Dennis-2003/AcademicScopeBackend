package com.AcademicScope.controller;

import com.AcademicScope.auth.dto.LoginRequest;
import com.AcademicScope.dto.CambioPasswordDTO;
import com.AcademicScope.enums.RolUsuario;
import com.AcademicScope.model.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UsuarioControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String adminToken;
    private String estudianteToken;
    private long adminId;

    @BeforeAll
    void setUp() throws Exception {
        adminToken = loginAndGetToken("dennis@academicscope.com", "Dennis@2026!");
        estudianteToken = loginAndGetToken("pedro@academicscope.com", "Estu@2026!");
    }

    private String loginAndGetToken(String username, String password) throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername(username);
        request.setPassword(password);
        String response = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        var tree = objectMapper.readTree(response);
        if (adminId == 0) {
            adminId = tree.get("id").asLong();
        }
        return tree.get("token").asText();
    }

    @Test
    void deberia_listar_usuarios_como_admin() throws Exception {
        mockMvc.perform(get("/api/usuarios")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(org.hamcrest.Matchers.greaterThanOrEqualTo(14)));
    }

    @Test
    void deberia_listar_usuarios_como_estudiante() throws Exception {
        mockMvc.perform(get("/api/usuarios")
                .header("Authorization", "Bearer " + estudianteToken))
                .andExpect(status().isOk());
    }

    @Test
    void deberia_obtener_usuario_por_id() throws Exception {
        mockMvc.perform(get("/api/usuarios/" + adminId)
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Dennis"));
    }

    @Test
    void deberia_crear_nuevo_usuario_como_admin() throws Exception {
        Usuario nuevo = Usuario.builder()
                .nombre("Nuevo").apellido("Usuario")
                .email("nuevoit@test.com").dni("99999999")
                .password("ignorado").rol(RolUsuario.ESTUDIANTE)
                .build();

        mockMvc.perform(post("/api/usuarios")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nombre").value("Nuevo"))
                .andExpect(jsonPath("$.primerIngreso").value(true));
    }

    @Test
    void deberia_rechazar_crear_usuario_sin_rol_admin() throws Exception {
        Usuario nuevo = Usuario.builder()
                .nombre("Nuevo2").apellido("User")
                .email("sinadminit2@test.com").dni("77777770")
                .password("pass").rol(RolUsuario.ESTUDIANTE)
                .build();

        mockMvc.perform(post("/api/usuarios")
                .header("Authorization", "Bearer " + estudianteToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deberia_cambiar_password_exitosamente() throws Exception {
        CambioPasswordDTO cambio = CambioPasswordDTO.builder()
                .dni("11111111").passwordActual("Estu@2026!")
                .passwordNuevo("Nueva@2026!").build();

        mockMvc.perform(put("/api/usuarios/cambiar-password")
                .header("Authorization", "Bearer " + estudianteToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cambio)))
                .andExpect(status().isOk());

        // Revertir el cambio para no afectar a otras clases de prueba que usan el mismo contexto de Spring
        CambioPasswordDTO revertir = CambioPasswordDTO.builder()
                .dni("11111111").passwordActual("Nueva@2026!")
                .passwordNuevo("Estu@2026!").build();

        mockMvc.perform(put("/api/usuarios/cambiar-password")
                .header("Authorization", "Bearer " + estudianteToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(revertir)))
                .andExpect(status().isOk());
    }

    @Test
    void deberia_fallar_cambio_password_con_password_actual_incorrecta() throws Exception {
        CambioPasswordDTO cambio = CambioPasswordDTO.builder()
                .dni("11111111").passwordActual("wrongpass")
                .passwordNuevo("nuevaPass123").build();

        mockMvc.perform(put("/api/usuarios/cambiar-password")
                .header("Authorization", "Bearer " + estudianteToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cambio)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deberia_buscar_usuario_inexistente_por_id_devuelve_404() throws Exception {
        mockMvc.perform(get("/api/usuarios/99999")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().is4xxClientError());
    }
}
