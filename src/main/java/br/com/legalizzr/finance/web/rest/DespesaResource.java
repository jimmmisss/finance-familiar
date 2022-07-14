package br.com.legalizzr.finance.web.rest;

import br.com.legalizzr.finance.domain.Despesa;
import br.com.legalizzr.finance.repository.DespesaRepository;
import br.com.legalizzr.finance.service.DespesaService;
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
 * REST controller for managing {@link br.com.legalizzr.finance.domain.Despesa}.
 */
@RestController
@RequestMapping("/api")
public class DespesaResource {

    private final Logger log = LoggerFactory.getLogger(DespesaResource.class);

    private static final String ENTITY_NAME = "despesa";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DespesaService despesaService;

    private final DespesaRepository despesaRepository;

    public DespesaResource(DespesaService despesaService, DespesaRepository despesaRepository) {
        this.despesaService = despesaService;
        this.despesaRepository = despesaRepository;
    }

    /**
     * {@code POST  /despesas} : Create a new despesa.
     *
     * @param despesa the despesa to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new despesa, or with status {@code 400 (Bad Request)} if the despesa has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/despesas")
    public ResponseEntity<Despesa> createDespesa(@RequestBody Despesa despesa) throws URISyntaxException {
        log.debug("REST request to save Despesa : {}", despesa);
        if (despesa.getId() != null) {
            throw new BadRequestAlertException("A new despesa cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Despesa result = despesaService.save(despesa);
        return ResponseEntity
            .created(new URI("/api/despesas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /despesas/:id} : Updates an existing despesa.
     *
     * @param id the id of the despesa to save.
     * @param despesa the despesa to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated despesa,
     * or with status {@code 400 (Bad Request)} if the despesa is not valid,
     * or with status {@code 500 (Internal Server Error)} if the despesa couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/despesas/{id}")
    public ResponseEntity<Despesa> updateDespesa(@PathVariable(value = "id", required = false) final Long id, @RequestBody Despesa despesa)
        throws URISyntaxException {
        log.debug("REST request to update Despesa : {}, {}", id, despesa);
        if (despesa.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, despesa.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!despesaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Despesa result = despesaService.update(despesa);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, despesa.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /despesas/:id} : Partial updates given fields of an existing despesa, field will ignore if it is null
     *
     * @param id the id of the despesa to save.
     * @param despesa the despesa to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated despesa,
     * or with status {@code 400 (Bad Request)} if the despesa is not valid,
     * or with status {@code 404 (Not Found)} if the despesa is not found,
     * or with status {@code 500 (Internal Server Error)} if the despesa couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/despesas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Despesa> partialUpdateDespesa(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Despesa despesa
    ) throws URISyntaxException {
        log.debug("REST request to partial update Despesa partially : {}, {}", id, despesa);
        if (despesa.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, despesa.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!despesaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Despesa> result = despesaService.partialUpdate(despesa);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, despesa.getId().toString())
        );
    }

    /**
     * {@code GET  /despesas} : get all the despesas.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of despesas in body.
     */
    @GetMapping("/despesas")
    public ResponseEntity<List<Despesa>> getAllDespesas(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Despesas");
        Page<Despesa> page;
        if (eagerload) {
            page = despesaService.findAllWithEagerRelationships(pageable);
        } else {
            page = despesaService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /despesas/:id} : get the "id" despesa.
     *
     * @param id the id of the despesa to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the despesa, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/despesas/{id}")
    public ResponseEntity<Despesa> getDespesa(@PathVariable Long id) {
        log.debug("REST request to get Despesa : {}", id);
        Optional<Despesa> despesa = despesaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(despesa);
    }

    /**
     * {@code DELETE  /despesas/:id} : delete the "id" despesa.
     *
     * @param id the id of the despesa to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/despesas/{id}")
    public ResponseEntity<Void> deleteDespesa(@PathVariable Long id) {
        log.debug("REST request to delete Despesa : {}", id);
        despesaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
