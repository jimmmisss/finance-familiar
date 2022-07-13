package br.com.legalizzr.finance.service;

import br.com.legalizzr.finance.domain.Categoria;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Categoria}.
 */
public interface CategoriaService {
    /**
     * Save a categoria.
     *
     * @param categoria the entity to save.
     * @return the persisted entity.
     */
    Categoria save(Categoria categoria);

    /**
     * Updates a categoria.
     *
     * @param categoria the entity to update.
     * @return the persisted entity.
     */
    Categoria update(Categoria categoria);

    /**
     * Partially updates a categoria.
     *
     * @param categoria the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Categoria> partialUpdate(Categoria categoria);

    /**
     * Get all the categorias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Categoria> findAll(Pageable pageable);

    /**
     * Get the "id" categoria.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Categoria> findOne(Long id);

    /**
     * Delete the "id" categoria.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
