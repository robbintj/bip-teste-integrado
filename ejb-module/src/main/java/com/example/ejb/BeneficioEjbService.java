package com.example.ejb;

import com.example.ejb.exception.BeneficioNotFoundException;
import com.example.ejb.exception.InsufficientBalanceException;
import com.example.ejb.exception.InvalidTransferException;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Serviço EJB para gerenciamento de Benefícios.
 * Implementa CRUD completo e operação de transferência com locking pessimista.
 *
 * @author Robert R Serra Java Fullstack Developer
 * @version 2.0
 */
@Stateless
public class BeneficioEjbService {

    @PersistenceContext
    private EntityManager em;

    /**
     * Cria um novo benefício.
     *
     * @param beneficio entidade a ser persistida
     * @return benefício criado com ID gerado
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Beneficio create(@NotNull Beneficio beneficio) {
        validateBeneficio(beneficio);
        em.persist(beneficio);
        em.flush();
        return beneficio;
    }

    /**
     * Busca benefício por ID.
     *
     * @param id identificador do benefício
     * @return Optional contendo o benefício se encontrado
     */
    public Optional<Beneficio> findById(@NotNull Long id) {
        Beneficio beneficio = em.find(Beneficio.class, id);
        return Optional.ofNullable(beneficio);
    }

    /**
     * Lista todos os benefícios ativos.
     *
     * @return lista de benefícios
     */
    public List<Beneficio> findAll() {
        TypedQuery<Beneficio> query = em.createQuery(
            "SELECT b FROM Beneficio b ORDER BY b.id",
            Beneficio.class
        );
        return query.getResultList();
    }

    /**
     * Atualiza um benefício existente.
     *
     * @param beneficio entidade com dados atualizados
     * @return benefício atualizado
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Beneficio update(@NotNull Beneficio beneficio) {
        if (beneficio.getId() == null) {
            throw new IllegalArgumentException("ID do benefício não pode ser nulo para atualização");
        }

        Beneficio existing = em.find(Beneficio.class, beneficio.getId());
        if (existing == null) {
            throw new BeneficioNotFoundException(beneficio.getId());
        }

        validateBeneficio(beneficio);
        return em.merge(beneficio);
    }

    /**
     * Remove um benefício por ID.
     *
     * @param id identificador do benefício
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void delete(@NotNull Long id) {
        Beneficio beneficio = em.find(Beneficio.class, id);
        if (beneficio == null) {
            throw new BeneficioNotFoundException(id);
        }
        em.remove(beneficio);
    }

    /**
     * Realiza transferência de valor entre dois benefícios.
     *
     * CORREÇÃO DO BUG:
     * - Validações de negócio (IDs distintos, valor positivo)
     * - Pessimistic Locking (PESSIMISTIC_WRITE) para evitar lost updates
     * - Verificação de saldo suficiente
     * - Rollback automático em caso de exceção (@ApplicationException)
     * - Operação atômica garantida pela transação EJB
     *
     * @param fromId ID do benefício origem
     * @param toId ID do benefício destino
     * @param amount valor a ser transferido
     * @throws InvalidTransferException se os parâmetros forem inválidos
     * @throws BeneficioNotFoundException se algum benefício não for encontrado
     * @throws InsufficientBalanceException se não houver saldo suficiente
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void transfer(@NotNull Long fromId, @NotNull Long toId, @NotNull BigDecimal amount) {
        // 1. Validações de entrada
        validateTransferParameters(fromId, toId, amount);

        // 2. Buscar benefícios com PESSIMISTIC LOCK (resolve race condition)
        // Lock garante que nenhuma outra transação pode modificar os registros
        Beneficio from = findWithLock(fromId);
        Beneficio to = findWithLock(toId);

        // 3. Validar existência
        if (from == null) {
            throw new BeneficioNotFoundException(fromId);
        }
        if (to == null) {
            throw new BeneficioNotFoundException(toId);
        }

        // 4. Validar saldo suficiente (previne saldo negativo)
        if (from.getValor().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(fromId, from.getValor(), amount);
        }

        // 5. Realizar transferência
        from.setValor(from.getValor().subtract(amount));
        to.setValor(to.getValor().add(amount));

        // 6. Persistir mudanças (JPA flush automático no commit da transação)
        em.merge(from);
        em.merge(to);
        em.flush(); // Força sincronização imediata com o banco
    }

    /**
     * Busca benefício com lock pessimista.
     */
    private Beneficio findWithLock(Long id) {
        return em.find(Beneficio.class, id, LockModeType.PESSIMISTIC_WRITE);
    }

    /**
     * Valida parâmetros da transferência.
     */
    private void validateTransferParameters(Long fromId, Long toId, BigDecimal amount) {
        if (fromId == null || toId == null) {
            throw new InvalidTransferException("IDs de origem e destino não podem ser nulos");
        }

        if (fromId.equals(toId)) {
            throw new InvalidTransferException("Não é permitido transferir para o mesmo benefício");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransferException("Valor da transferência deve ser maior que zero");
        }
    }

    /**
     * Valida regras de negócio do benefício.
     */
    private void validateBeneficio(Beneficio beneficio) {
        if (beneficio.getNome() == null || beneficio.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do benefício é obrigatório");
        }

        if (beneficio.getValor() == null || beneficio.getValor().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor do benefício não pode ser negativo");
        }
    }
}
