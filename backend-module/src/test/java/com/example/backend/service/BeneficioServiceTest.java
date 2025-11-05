package com.example.backend.service;

import com.example.backend.dto.BeneficioRequestDTO;
import com.example.backend.dto.BeneficioResponseDTO;
import com.example.backend.dto.TransferRequestDTO;
import com.example.backend.mapper.BeneficioMapper;
import com.example.ejb.Beneficio;
import com.example.ejb.BeneficioEjbService;
import com.example.ejb.exception.BeneficioNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para BeneficioService.
 * Utiliza Mockito para isolar a camada de serviço.
 *
 * @author Robert R Serra Java Fullstack Developer
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BeneficioService Tests")
class BeneficioServiceTest {

    @Mock
    private BeneficioEjbService ejbService;

    @Mock
    private BeneficioMapper mapper;

    @InjectMocks
    private BeneficioService service;

    private Beneficio beneficio;
    private BeneficioRequestDTO requestDTO;
    private BeneficioResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        beneficio = new Beneficio("Beneficio A", new BigDecimal("1000.00"));
        beneficio.setId(1L);
        beneficio.setAtivo(true);
        beneficio.setVersion(0L);

        requestDTO = BeneficioRequestDTO.builder()
                .nome("Beneficio A")
                .descricao("Descrição A")
                .valor(new BigDecimal("1000.00"))
                .ativo(true)
                .build();

        responseDTO = BeneficioResponseDTO.builder()
                .id(1L)
                .nome("Beneficio A")
                .descricao("Descrição A")
                .valor(new BigDecimal("1000.00"))
                .ativo(true)
                .version(0L)
                .build();
    }

    @Test
    @DisplayName("Deve listar todos os benefícios")
    void shouldFindAll() {
        // Given
        List<Beneficio> beneficios = Arrays.asList(beneficio);
        List<BeneficioResponseDTO> expectedResponse = Arrays.asList(responseDTO);

        when(ejbService.findAll()).thenReturn(beneficios);
        when(mapper.toResponseDTOList(beneficios)).thenReturn(expectedResponse);

        // When
        List<BeneficioResponseDTO> result = service.findAll();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNome()).isEqualTo("Beneficio A");

        verify(ejbService).findAll();
        verify(mapper).toResponseDTOList(beneficios);
    }

    @Test
    @DisplayName("Deve buscar benefício por ID com sucesso")
    void shouldFindById() {
        // Given
        when(ejbService.findById(1L)).thenReturn(Optional.of(beneficio));
        when(mapper.toResponseDTO(beneficio)).thenReturn(responseDTO);

        // When
        BeneficioResponseDTO result = service.findById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNome()).isEqualTo("Beneficio A");

        verify(ejbService).findById(1L);
        verify(mapper).toResponseDTO(beneficio);
    }

    @Test
    @DisplayName("Deve lançar exceção quando benefício não encontrado")
    void shouldThrowExceptionWhenNotFound() {
        // Given
        when(ejbService.findById(999L)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> service.findById(999L))
                .isInstanceOf(BeneficioNotFoundException.class);

        verify(ejbService).findById(999L);
        verify(mapper, never()).toResponseDTO(any());
    }

    @Test
    @DisplayName("Deve criar benefício com sucesso")
    void shouldCreate() {
        // Given
        when(mapper.toEntity(requestDTO)).thenReturn(beneficio);
        when(ejbService.create(any(Beneficio.class))).thenReturn(beneficio);
        when(mapper.toResponseDTO(beneficio)).thenReturn(responseDTO);

        // When
        BeneficioResponseDTO result = service.create(requestDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNome()).isEqualTo("Beneficio A");

        verify(mapper).toEntity(requestDTO);
        verify(ejbService).create(any(Beneficio.class));
        verify(mapper).toResponseDTO(beneficio);
    }

    @Test
    @DisplayName("Deve atualizar benefício com sucesso")
    void shouldUpdate() {
        // Given
        when(ejbService.findById(1L)).thenReturn(Optional.of(beneficio));
        when(ejbService.update(any(Beneficio.class))).thenReturn(beneficio);
        when(mapper.toResponseDTO(beneficio)).thenReturn(responseDTO);

        // When
        BeneficioResponseDTO result = service.update(1L, requestDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);

        verify(ejbService).findById(1L);
        verify(mapper).updateEntityFromDTO(eq(requestDTO), any(Beneficio.class));
        verify(ejbService).update(any(Beneficio.class));
        verify(mapper).toResponseDTO(beneficio);
    }

    @Test
    @DisplayName("Deve remover benefício com sucesso")
    void shouldDelete() {
        // Given
        doNothing().when(ejbService).delete(1L);

        // When
        service.delete(1L);

        // Then
        verify(ejbService).delete(1L);
    }

    @Test
    @DisplayName("Deve realizar transferência com sucesso")
    void shouldTransfer() {
        // Given
        TransferRequestDTO transferDTO = TransferRequestDTO.builder()
                .fromId(1L)
                .toId(2L)
                .amount(new BigDecimal("100.00"))
                .build();

        doNothing().when(ejbService).transfer(1L, 2L, new BigDecimal("100.00"));

        // When
        service.transfer(transferDTO);

        // Then
        verify(ejbService).transfer(1L, 2L, new BigDecimal("100.00"));
    }
}

