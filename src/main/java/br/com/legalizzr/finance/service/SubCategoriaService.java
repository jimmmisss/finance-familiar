package br.com.legalizzr.finance.service;

import br.com.legalizzr.finance.domain.SubCategoria;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link SubCategoria}.
 */
public interface SubCategoriaService {
    /**
     * Save a subCategoria.
     *
     * @param subCategoria the entity to save.
     * @return the persisted entity.
     */
    SubCategoria save(SubCategoria subCategoria);

    /**
     * Updates a subCategoria.
     *
     * @param subCategoria the entity to update.
     * @return the persisted entity.
     */
    SubCategoria update(SubCategoria subCategoria);

    /**
     * Partially updates a subCategoria.
     *
     * @param subCategoria the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SubCategoria> partialUpdate(SubCategoria subCategoria);

    /**
     * Get all the subCategorias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SubCategoria> findAll(Pageable pageable);

    /**
     * Get all the subCategorias with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SubCategoria> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" subCategoria.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubCategoria> findOne(Long id);

    /**
     * Delete the "id" subCategoria.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
