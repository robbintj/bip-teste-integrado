package com.example.backend.mapper;

import com.example.backend.dto.BeneficioRequestDTO;
import com.example.backend.dto.BeneficioResponseDTO;
import com.example.ejb.Beneficio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * Mapper para conversão entre Entity e DTOs.
 * Usa MapStruct para geração automática de código (zero reflection em runtime).
 * Segue princípio SOLID: SRP - responsabilidade única de mapeamento.
 *
 * @author Robert R Serra Java Fullstack Developer
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface BeneficioMapper {

    /**
     * Converte Entity para ResponseDTO.
     */
    BeneficioResponseDTO toResponseDTO(Beneficio beneficio);

    /**
     * Converte lista de Entities para lista de ResponseDTOs.
     */
    List<BeneficioResponseDTO> toResponseDTOList(List<Beneficio> beneficios);

    /**
     * Converte RequestDTO para Entity (para criação).
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    Beneficio toEntity(BeneficioRequestDTO requestDTO);

    /**
     * Atualiza Entity existente com dados do RequestDTO (para update).
     * Ignora ID e version para não sobrescrever.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntityFromDTO(BeneficioRequestDTO requestDTO, @MappingTarget Beneficio beneficio);
}

