package com.example.backend;

import com.example.backend.dto.BeneficioRequestDTO;
import com.example.backend.dto.BeneficioResponseDTO;
import com.example.backend.dto.TransferRequestDTO;
import com.example.backend.exception.ErrorResponse;
import com.example.backend.service.BeneficioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para gerenciamento de Benefícios.
 *
 * Endpoints implementados (CRUD completo):
 * - GET    /api/v1/beneficios          - Lista todos
 * - GET    /api/v1/beneficios/{id}     - Busca por ID
 * - POST   /api/v1/beneficios          - Cria novo
 * - PUT    /api/v1/beneficios/{id}     - Atualiza
 * - DELETE /api/v1/beneficios/{id}     - Remove
 * - POST   /api/v1/beneficios/transfer - Transferência
 *
 * Segue princípios REST:
 * - Verbos HTTP corretos
 * - Status codes apropriados
 * - Idempotência (PUT, DELETE)
 * - Validação de entrada
 *
 * @author Robert R Serra Java Fullstack Developer
 */
@RestController
@RequestMapping("/api/v1/beneficios")
@RequiredArgsConstructor
@Tag(name = "Benefícios", description = "API para gerenciamento de benefícios")
public class BeneficioController {

    private final BeneficioService service;

    @Operation(summary = "Lista todos os benefícios", description = "Retorna lista completa de benefícios cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BeneficioResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<BeneficioResponseDTO>> findAll() {
        List<BeneficioResponseDTO> beneficios = service.findAll();
        return ResponseEntity.ok(beneficios);
    }

    @Operation(summary = "Busca benefício por ID", description = "Retorna um benefício específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Benefício encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BeneficioResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Benefício não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<BeneficioResponseDTO> findById(@PathVariable Long id) {
        BeneficioResponseDTO beneficio = service.findById(id);
        return ResponseEntity.ok(beneficio);
    }

    @Operation(summary = "Cria novo benefício", description = "Cadastra um novo benefício no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Benefício criado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BeneficioResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<BeneficioResponseDTO> create(@Valid @RequestBody BeneficioRequestDTO requestDTO) {
        BeneficioResponseDTO created = service.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Atualiza benefício", description = "Atualiza dados de um benefício existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Benefício atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BeneficioResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Benefício não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<BeneficioResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody BeneficioRequestDTO requestDTO) {
        BeneficioResponseDTO updated = service.update(id, requestDTO);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Remove benefício", description = "Remove um benefício do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Benefício removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Benefício não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Transfere valor entre benefícios",
            description = "Realiza transferência de valor entre dois benefícios. " +
                    "Operação atômica com validações de saldo e locking para evitar inconsistências."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transferência realizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Benefício não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Saldo insuficiente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@Valid @RequestBody TransferRequestDTO requestDTO) {
        service.transfer(requestDTO);
        return ResponseEntity.ok().build();
    }
}
