package com.example.backend.service;

import com.example.backend.dto.BeneficioRequestDTO;
import com.example.backend.dto.BeneficioResponseDTO;
import com.example.backend.dto.TransferRequestDTO;
import com.example.backend.mapper.BeneficioMapper;
import com.example.ejb.Beneficio;
import com.example.ejb.BeneficioEjbService;
import com.example.ejb.exception.BeneficioNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service para gerenciamento de Benefícios.
 * Camada intermediária entre Controller e EJB.
 *
 * Responsabilidades (SOLID - SRP):
 * - Orquestração de lógica de negócio
 * - Integração com EJB
 * - Conversão DTO ↔ Entity
 *
 * @author Robert R Serra Java Fullstack Developer
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BeneficioService {

    private final BeneficioEjbService ejbService;
    private final BeneficioMapper mapper;

    /**
     * Lista todos os benefícios.
     *
     * @return lista de benefícios em formato DTO
     */
    public List<BeneficioResponseDTO> findAll() {
        log.info("Listando todos os benefícios");
        List<Beneficio> beneficios = ejbService.findAll();
        return mapper.toResponseDTOList(beneficios);
    }

    /**
     * Busca benefício por ID.
     *
     * @param id identificador do benefício
     * @return benefício encontrado
     * @throws BeneficioNotFoundException se não encontrado
     */
    public BeneficioResponseDTO findById(Long id) {
        log.info("Buscando benefício por ID: {}", id);
        Beneficio beneficio = ejbService.findById(id)
                .orElseThrow(() -> new BeneficioNotFoundException(id));
        return mapper.toResponseDTO(beneficio);
    }

    /**
     * Cria um novo benefício.
     *
     * @param requestDTO dados do benefício
     * @return benefício criado
     */
    @Transactional
    public BeneficioResponseDTO create(BeneficioRequestDTO requestDTO) {
        log.info("Criando novo benefício: {}", requestDTO.getNome());

        Beneficio beneficio = mapper.toEntity(requestDTO);

        // Garantir que ativo é true se não informado
        if (beneficio.getAtivo() == null) {
            beneficio.setAtivo(true);
        }

        Beneficio saved = ejbService.create(beneficio);
        log.info("Benefício criado com ID: {}", saved.getId());

        return mapper.toResponseDTO(saved);
    }

    /**
     * Atualiza um benefício existente.
     *
     * @param id identificador do benefício
     * @param requestDTO novos dados
     * @return benefício atualizado
     * @throws BeneficioNotFoundException se não encontrado
     */
    @Transactional
    public BeneficioResponseDTO update(Long id, BeneficioRequestDTO requestDTO) {
        log.info("Atualizando benefício ID: {}", id);

        // Buscar benefício existente
        Beneficio existing = ejbService.findById(id)
                .orElseThrow(() -> new BeneficioNotFoundException(id));

        // Atualizar campos
        mapper.updateEntityFromDTO(requestDTO, existing);

        // Salvar
        Beneficio updated = ejbService.update(existing);
        log.info("Benefício ID {} atualizado com sucesso", id);

        return mapper.toResponseDTO(updated);
    }

    /**
     * Remove um benefício por ID.
     *
     * @param id identificador do benefício
     * @throws BeneficioNotFoundException se não encontrado
     */
    @Transactional
    public void delete(Long id) {
        log.info("Removendo benefício ID: {}", id);
        ejbService.delete(id);
        log.info("Benefício ID {} removido com sucesso", id);
    }

    /**
     * Realiza transferência entre benefícios.
     * Delega para o EJB que possui a lógica transacional com locking.
     *
     * @param requestDTO dados da transferência
     */
    @Transactional
    public void transfer(TransferRequestDTO requestDTO) {
        log.info("Iniciando transferência: R$ {} de ID {} para ID {}",
                requestDTO.getAmount(), requestDTO.getFromId(), requestDTO.getToId());

        ejbService.transfer(
                requestDTO.getFromId(),
                requestDTO.getToId(),
                requestDTO.getAmount()
        );

        log.info("Transferência concluída com sucesso");
    }
}

