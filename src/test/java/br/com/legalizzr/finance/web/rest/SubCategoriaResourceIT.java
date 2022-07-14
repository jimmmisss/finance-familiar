package br.com.legalizzr.finance.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.legalizzr.finance.IntegrationTest;
import br.com.legalizzr.finance.domain.SubCategoria;
import br.com.legalizzr.finance.repository.SubCategoriaRepository;
import br.com.legalizzr.finance.service.SubCategoriaService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SubCategoriaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SubCategoriaResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final Instant DEFAULT_DT_CADASTRO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DT_CADASTRO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/sub-categorias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SubCategoriaRepository subCategoriaRepository;

    @Mock
    private SubCategoriaRepository subCategoriaRepositoryMock;

    @Mock
    private SubCategoriaService subCategoriaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubCategoriaMockMvc;

    private SubCategoria subCategoria;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubCategoria createEntity(EntityManager em) {
        SubCategoria subCategoria = new SubCategoria().nome(DEFAULT_NOME).descricao(DEFAULT_DESCRICAO).dtCadastro(DEFAULT_DT_CADASTRO);
        return subCategoria;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubCategoria createUpdatedEntity(EntityManager em) {
        SubCategoria subCategoria = new SubCategoria().nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO).dtCadastro(UPDATED_DT_CADASTRO);
        return subCategoria;
    }

    @BeforeEach
    public void initTest() {
        subCategoria = createEntity(em);
    }

    @Test
    @Transactional
    void createSubCategoria() throws Exception {
        int databaseSizeBeforeCreate = subCategoriaRepository.findAll().size();
        // Create the SubCategoria
        restSubCategoriaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subCategoria)))
            .andExpect(status().isCreated());

        // Validate the SubCategoria in the database
        List<SubCategoria> subCategoriaList = subCategoriaRepository.findAll();
        assertThat(subCategoriaList).hasSize(databaseSizeBeforeCreate + 1);
        SubCategoria testSubCategoria = subCategoriaList.get(subCategoriaList.size() - 1);
        assertThat(testSubCategoria.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testSubCategoria.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testSubCategoria.getDtCadastro()).isEqualTo(DEFAULT_DT_CADASTRO);
    }

    @Test
    @Transactional
    void createSubCategoriaWithExistingId() throws Exception {
        // Create the SubCategoria with an existing ID
        subCategoria.setId(1L);

        int databaseSizeBeforeCreate = subCategoriaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubCategoriaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subCategoria)))
            .andExpect(status().isBadRequest());

        // Validate the SubCategoria in the database
        List<SubCategoria> subCategoriaList = subCategoriaRepository.findAll();
        assertThat(subCategoriaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSubCategorias() throws Exception {
        // Initialize the database
        subCategoriaRepository.saveAndFlush(subCategoria);

        // Get all the subCategoriaList
        restSubCategoriaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subCategoria.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].dtCadastro").value(hasItem(DEFAULT_DT_CADASTRO.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSubCategoriasWithEagerRelationshipsIsEnabled() throws Exception {
        when(subCategoriaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSubCategoriaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(subCategoriaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSubCategoriasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(subCategoriaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSubCategoriaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(subCategoriaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getSubCategoria() throws Exception {
        // Initialize the database
        subCategoriaRepository.saveAndFlush(subCategoria);

        // Get the subCategoria
        restSubCategoriaMockMvc
            .perform(get(ENTITY_API_URL_ID, subCategoria.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subCategoria.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.dtCadastro").value(DEFAULT_DT_CADASTRO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSubCategoria() throws Exception {
        // Get the subCategoria
        restSubCategoriaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSubCategoria() throws Exception {
        // Initialize the database
        subCategoriaRepository.saveAndFlush(subCategoria);

        int databaseSizeBeforeUpdate = subCategoriaRepository.findAll().size();

        // Update the subCategoria
        SubCategoria updatedSubCategoria = subCategoriaRepository.findById(subCategoria.getId()).get();
        // Disconnect from session so that the updates on updatedSubCategoria are not directly saved in db
        em.detach(updatedSubCategoria);
        updatedSubCategoria.nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO).dtCadastro(UPDATED_DT_CADASTRO);

        restSubCategoriaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSubCategoria.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSubCategoria))
            )
            .andExpect(status().isOk());

        // Validate the SubCategoria in the database
        List<SubCategoria> subCategoriaList = subCategoriaRepository.findAll();
        assertThat(subCategoriaList).hasSize(databaseSizeBeforeUpdate);
        SubCategoria testSubCategoria = subCategoriaList.get(subCategoriaList.size() - 1);
        assertThat(testSubCategoria.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testSubCategoria.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testSubCategoria.getDtCadastro()).isEqualTo(UPDATED_DT_CADASTRO);
    }

    @Test
    @Transactional
    void putNonExistingSubCategoria() throws Exception {
        int databaseSizeBeforeUpdate = subCategoriaRepository.findAll().size();
        subCategoria.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubCategoriaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subCategoria.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subCategoria))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubCategoria in the database
        List<SubCategoria> subCategoriaList = subCategoriaRepository.findAll();
        assertThat(subCategoriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubCategoria() throws Exception {
        int databaseSizeBeforeUpdate = subCategoriaRepository.findAll().size();
        subCategoria.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubCategoriaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(subCategoria))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubCategoria in the database
        List<SubCategoria> subCategoriaList = subCategoriaRepository.findAll();
        assertThat(subCategoriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubCategoria() throws Exception {
        int databaseSizeBeforeUpdate = subCategoriaRepository.findAll().size();
        subCategoria.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubCategoriaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(subCategoria)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubCategoria in the database
        List<SubCategoria> subCategoriaList = subCategoriaRepository.findAll();
        assertThat(subCategoriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubCategoriaWithPatch() throws Exception {
        // Initialize the database
        subCategoriaRepository.saveAndFlush(subCategoria);

        int databaseSizeBeforeUpdate = subCategoriaRepository.findAll().size();

        // Update the subCategoria using partial update
        SubCategoria partialUpdatedSubCategoria = new SubCategoria();
        partialUpdatedSubCategoria.setId(subCategoria.getId());

        partialUpdatedSubCategoria.descricao(UPDATED_DESCRICAO).dtCadastro(UPDATED_DT_CADASTRO);

        restSubCategoriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubCategoria.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubCategoria))
            )
            .andExpect(status().isOk());

        // Validate the SubCategoria in the database
        List<SubCategoria> subCategoriaList = subCategoriaRepository.findAll();
        assertThat(subCategoriaList).hasSize(databaseSizeBeforeUpdate);
        SubCategoria testSubCategoria = subCategoriaList.get(subCategoriaList.size() - 1);
        assertThat(testSubCategoria.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testSubCategoria.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testSubCategoria.getDtCadastro()).isEqualTo(UPDATED_DT_CADASTRO);
    }

    @Test
    @Transactional
    void fullUpdateSubCategoriaWithPatch() throws Exception {
        // Initialize the database
        subCategoriaRepository.saveAndFlush(subCategoria);

        int databaseSizeBeforeUpdate = subCategoriaRepository.findAll().size();

        // Update the subCategoria using partial update
        SubCategoria partialUpdatedSubCategoria = new SubCategoria();
        partialUpdatedSubCategoria.setId(subCategoria.getId());

        partialUpdatedSubCategoria.nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO).dtCadastro(UPDATED_DT_CADASTRO);

        restSubCategoriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubCategoria.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubCategoria))
            )
            .andExpect(status().isOk());

        // Validate the SubCategoria in the database
        List<SubCategoria> subCategoriaList = subCategoriaRepository.findAll();
        assertThat(subCategoriaList).hasSize(databaseSizeBeforeUpdate);
        SubCategoria testSubCategoria = subCategoriaList.get(subCategoriaList.size() - 1);
        assertThat(testSubCategoria.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testSubCategoria.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testSubCategoria.getDtCadastro()).isEqualTo(UPDATED_DT_CADASTRO);
    }

    @Test
    @Transactional
    void patchNonExistingSubCategoria() throws Exception {
        int databaseSizeBeforeUpdate = subCategoriaRepository.findAll().size();
        subCategoria.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubCategoriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subCategoria.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subCategoria))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubCategoria in the database
        List<SubCategoria> subCategoriaList = subCategoriaRepository.findAll();
        assertThat(subCategoriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubCategoria() throws Exception {
        int databaseSizeBeforeUpdate = subCategoriaRepository.findAll().size();
        subCategoria.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubCategoriaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(subCategoria))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubCategoria in the database
        List<SubCategoria> subCategoriaList = subCategoriaRepository.findAll();
        assertThat(subCategoriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubCategoria() throws Exception {
        int databaseSizeBeforeUpdate = subCategoriaRepository.findAll().size();
        subCategoria.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubCategoriaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(subCategoria))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubCategoria in the database
        List<SubCategoria> subCategoriaList = subCategoriaRepository.findAll();
        assertThat(subCategoriaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubCategoria() throws Exception {
        // Initialize the database
        subCategoriaRepository.saveAndFlush(subCategoria);

        int databaseSizeBeforeDelete = subCategoriaRepository.findAll().size();

        // Delete the subCategoria
        restSubCategoriaMockMvc
            .perform(delete(ENTITY_API_URL_ID, subCategoria.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SubCategoria> subCategoriaList = subCategoriaRepository.findAll();
        assertThat(subCategoriaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
