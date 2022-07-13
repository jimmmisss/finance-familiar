package br.com.legalizzr.finance.web.rest;

import br.com.legalizzr.finance.domain.SubCategoria;
import br.com.legalizzr.finance.repository.SubCategoriaRepository;
import br.com.legalizzr.finance.service.SubCategoriaService;
import br.com.legalizzr.finance.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.com.legalizzr.finance.domain.SubCategoria}.
 */
@RestController
@RequestMapping("/api")
public class SubCategoriaResource {

    private final Logger log = LoggerFactory.getLogger(SubCategoriaResource.class);

    private static final String ENTITY_NAME = "subCategoria";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubCategoriaService subCategoriaService;

    private final SubCategoriaRepository subCategoriaRepository;

    public SubCategoriaResource(SubCategoriaService subCategoriaService, SubCategoriaRepository subCategoriaRepository) {
        this.subCategoriaService = subCategoriaService;
        this.subCategoriaRepository = subCategoriaRepository;
    }

    /**
     * {@code POST  /sub-categorias} : Create a new subCategoria.
     *
     * @param subCategoria the subCategoria to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subCategoria, or with status {@code 400 (Bad Request)} if the subCategoria has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sub-categorias")
    public ResponseEntity<SubCategoria> createSubCategoria(@RequestBody SubCategoria subCategoria) throws URISyntaxException {
        log.debug("REST request to save SubCategoria : {}", subCategoria);
        if (subCategoria.getId() != null) {
            throw new BadRequestAlertException("A new subCategoria cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubCategoria result = subCategoriaService.save(subCategoria);
        return ResponseEntity
            .created(new URI("/api/sub-categorias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sub-categorias/:id} : Updates an existing subCategoria.
     *
     * @param id the id of the subCategoria to save.
     * @param subCategoria the subCategoria to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subCategoria,
     * or with status {@code 400 (Bad Request)} if the subCategoria is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subCategoria couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sub-categorias/{id}")
    public ResponseEntity<SubCategoria> updateSubCategoria(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SubCategoria subCategoria
    ) throws URISyntaxException {
        log.debug("REST request to update SubCategoria : {}, {}", id, subCategoria);
        if (subCategoria.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subCategoria.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subCategoriaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SubCategoria result = subCategoriaService.update(subCategoria);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, subCategoria.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sub-categorias/:id} : Partial updates given fields of an existing subCategoria, field will ignore if it is null
     *
     * @param id the id of the subCategoria to save.
     * @param subCategoria the subCategoria to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subCategoria,
     * or with status {@code 400 (Bad Request)} if the subCategoria is not valid,
     * or with status {@code 404 (Not Found)} if the subCategoria is not found,
     * or with status {@code 500 (Internal Server Error)} if the subCategoria couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sub-categorias/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SubCategoria> partialUpdateSubCategoria(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SubCategoria subCategoria
    ) throws URISyntaxException {
        log.debug("REST request to partial update SubCategoria partially : {}, {}", id, subCategoria);
        if (subCategoria.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subCategoria.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subCategoriaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubCategoria> result = subCategoriaService.partialUpdate(subCategoria);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, subCategoria.getId().toString())
        );
    }

    /**
     * {@code GET  /sub-categorias} : get all the subCategorias.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subCategorias in body.
     */
    @GetMapping("/sub-categorias")
    public ResponseEntity<List<SubCategoria>> getAllSubCategorias(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of SubCategorias");
        Page<SubCategoria> page;
        if (eagerload) {
            page = subCategoriaService.findAllWithEagerRelationships(pageable);
        } else {
            page = subCategoriaService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sub-categorias/:id} : get the "id" subCategoria.
     *
     * @param id the id of the subCategoria to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subCategoria, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sub-categorias/{id}")
    public ResponseEntity<SubCategoria> getSubCategoria(@PathVariable Long id) {
        log.debug("REST request to get SubCategoria : {}", id);
        Optional<SubCategoria> subCategoria = subCategoriaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subCategoria);
    }

    /**
     * {@code DELETE  /sub-categorias/:id} : delete the "id" subCategoria.
     *
     * @param id the id of the subCategoria to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sub-categorias/{id}")
    public ResponseEntity<Void> deleteSubCategoria(@PathVariable Long id) {
        log.debug("REST request to delete SubCategoria : {}", id);
        subCategoriaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
