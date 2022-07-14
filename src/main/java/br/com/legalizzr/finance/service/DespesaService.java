package br.com.legalizzr.finance.service;

import br.com.legalizzr.finance.domain.Despesa;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Despesa}.
 */
public interface DespesaService {
    /**
     * Save a despesa.
     *
     * @param despesa the entity to save.
     * @return the persisted entity.
     */
    Despesa save(Despesa despesa);

    /**
     * Updates a despesa.
     *
     * @param despesa the entity to update.
     * @return the persisted entity.
     */
    Despesa update(Despesa despesa);

    /**
     * Partially updates a despesa.
     *
     * @param despesa the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Despesa> partialUpdate(Despesa despesa);

    /**
     * Get all the despesas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Despesa> findAll(Pageable pageable);

    /**
     * Get all the despesas with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Despesa> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" despesa.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Despesa> findOne(Long id);

    /**
     * Delete the "id" despesa.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
