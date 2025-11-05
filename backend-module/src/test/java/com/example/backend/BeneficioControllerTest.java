package com.example.backend;

import com.example.backend.dto.BeneficioRequestDTO;
import com.example.backend.dto.BeneficioResponseDTO;
import com.example.backend.dto.TransferRequestDTO;
import com.example.backend.service.BeneficioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração para BeneficioController.
 * Utiliza MockMvc para testar endpoints REST.
 *
 * @author Robert R Serra Java Fullstack Developer
 */
@WebMvcTest(BeneficioController.class)
@DisplayName("BeneficioController Integration Tests")
class BeneficioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BeneficioService service;

    @Test
    @DisplayName("GET /api/v1/beneficios - Deve listar todos os benefícios")
    void shouldGetAllBeneficios() throws Exception {
        // Given
        BeneficioResponseDTO dto = BeneficioResponseDTO.builder()
                .id(1L)
                .nome("Beneficio A")
                .valor(new BigDecimal("1000.00"))
                .ativo(true)
                .build();

        when(service.findAll()).thenReturn(Arrays.asList(dto));

        // When/Then
        mockMvc.perform(get("/api/v1/beneficios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("Beneficio A"))
                .andExpect(jsonPath("$[0].valor").value(1000.00));

        verify(service).findAll();
    }

    @Test
    @DisplayName("GET /api/v1/beneficios/{id} - Deve buscar benefício por ID")
    void shouldGetBeneficioById() throws Exception {
        // Given
        BeneficioResponseDTO dto = BeneficioResponseDTO.builder()
                .id(1L)
                .nome("Beneficio A")
                .valor(new BigDecimal("1000.00"))
                .build();

        when(service.findById(1L)).thenReturn(dto);

        // When/Then
        mockMvc.perform(get("/api/v1/beneficios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Beneficio A"));

        verify(service).findById(1L);
    }

    @Test
    @DisplayName("POST /api/v1/beneficios - Deve criar benefício")
    void shouldCreateBeneficio() throws Exception {
        // Given
        BeneficioRequestDTO requestDTO = BeneficioRequestDTO.builder()
                .nome("Novo Beneficio")
                .descricao("Descrição")
                .valor(new BigDecimal("500.00"))
                .ativo(true)
                .build();

        BeneficioResponseDTO responseDTO = BeneficioResponseDTO.builder()
                .id(1L)
                .nome("Novo Beneficio")
                .descricao("Descrição")
                .valor(new BigDecimal("500.00"))
                .ativo(true)
                .build();

        when(service.create(any(BeneficioRequestDTO.class))).thenReturn(responseDTO);

        // When/Then
        mockMvc.perform(post("/api/v1/beneficios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Novo Beneficio"));

        verify(service).create(any(BeneficioRequestDTO.class));
    }

    @Test
    @DisplayName("POST /api/v1/beneficios - Deve retornar 400 para dados inválidos")
    void shouldReturn400ForInvalidData() throws Exception {
        // Given
        BeneficioRequestDTO invalidDTO = BeneficioRequestDTO.builder()
                .nome("") // Nome vazio (inválido)
                .valor(new BigDecimal("-100.00")) // Valor negativo (inválido)
                .build();

        // When/Then
        mockMvc.perform(post("/api/v1/beneficios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());

        verify(service, never()).create(any());
    }

    @Test
    @DisplayName("PUT /api/v1/beneficios/{id} - Deve atualizar benefício")
    void shouldUpdateBeneficio() throws Exception {
        // Given
        BeneficioRequestDTO requestDTO = BeneficioRequestDTO.builder()
                .nome("Beneficio Atualizado")
                .valor(new BigDecimal("1500.00"))
                .build();

        BeneficioResponseDTO responseDTO = BeneficioResponseDTO.builder()
                .id(1L)
                .nome("Beneficio Atualizado")
                .valor(new BigDecimal("1500.00"))
                .build();

        when(service.update(eq(1L), any(BeneficioRequestDTO.class))).thenReturn(responseDTO);

        // When/Then
        mockMvc.perform(put("/api/v1/beneficios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Beneficio Atualizado"));

        verify(service).update(eq(1L), any(BeneficioRequestDTO.class));
    }

    @Test
    @DisplayName("DELETE /api/v1/beneficios/{id} - Deve remover benefício")
    void shouldDeleteBeneficio() throws Exception {
        // Given
        doNothing().when(service).delete(1L);

        // When/Then
        mockMvc.perform(delete("/api/v1/beneficios/1"))
                .andExpect(status().isNoContent());

        verify(service).delete(1L);
    }

    @Test
    @DisplayName("POST /api/v1/beneficios/transfer - Deve realizar transferência")
    void shouldTransfer() throws Exception {
        // Given
        TransferRequestDTO transferDTO = TransferRequestDTO.builder()
                .fromId(1L)
                .toId(2L)
                .amount(new BigDecimal("100.00"))
                .build();

        doNothing().when(service).transfer(any(TransferRequestDTO.class));

        // When/Then
        mockMvc.perform(post("/api/v1/beneficios/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferDTO)))
                .andExpect(status().isOk());

        verify(service).transfer(any(TransferRequestDTO.class));
    }
}

