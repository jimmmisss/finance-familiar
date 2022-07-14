package br.com.legalizzr.finance.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.legalizzr.finance.IntegrationTest;
import br.com.legalizzr.finance.domain.Despesa;
import br.com.legalizzr.finance.domain.enumeration.FormaPgto;
import br.com.legalizzr.finance.domain.enumeration.Mes;
import br.com.legalizzr.finance.domain.enumeration.Responsavel;
import br.com.legalizzr.finance.repository.DespesaRepository;
import br.com.legalizzr.finance.service.DespesaService;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link DespesaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DespesaResourceIT {

    private static final Mes DEFAULT_MES = Mes.JANEIRO;
    private static final Mes UPDATED_MES = Mes.FEVEREIRO;

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final Double DEFAULT_VALOR = 1D;
    private static final Double UPDATED_VALOR = 2D;

    private static final LocalDate DEFAULT_DT_VCTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DT_VCTO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DT_PGTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DT_PGTO = LocalDate.now(ZoneId.systemDefault());

    private static final FormaPgto DEFAULT_FORMA_PGTO = FormaPgto.BOLETO;
    private static final FormaPgto UPDATED_FORMA_PGTO = FormaPgto.DEBITO;

    private static final Responsavel DEFAULT_RESPONSAVEL = Responsavel.WESLEY;
    private static final Responsavel UPDATED_RESPONSAVEL = Responsavel.FADIA;

    private static final Instant DEFAULT_DT_CADASTRO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DT_CADASTRO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/despesas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DespesaRepository despesaRepository;

    @Mock
    private DespesaRepository despesaRepositoryMock;

    @Mock
    private DespesaService despesaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDespesaMockMvc;

    private Despesa despesa;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Despesa createEntity(EntityManager em) {
        Despesa despesa = new Despesa()
            .mes(DEFAULT_MES)
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO)
            .valor(DEFAULT_VALOR)
            .dtVcto(DEFAULT_DT_VCTO)
            .dtPgto(DEFAULT_DT_PGTO)
            .formaPgto(DEFAULT_FORMA_PGTO)
            .responsavel(DEFAULT_RESPONSAVEL)
            .dtCadastro(DEFAULT_DT_CADASTRO);
        return despesa;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Despesa createUpdatedEntity(EntityManager em) {
        Despesa despesa = new Despesa()
            .mes(UPDATED_MES)
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .valor(UPDATED_VALOR)
            .dtVcto(UPDATED_DT_VCTO)
            .dtPgto(UPDATED_DT_PGTO)
            .formaPgto(UPDATED_FORMA_PGTO)
            .responsavel(UPDATED_RESPONSAVEL)
            .dtCadastro(UPDATED_DT_CADASTRO);
        return despesa;
    }

    @BeforeEach
    public void initTest() {
        despesa = createEntity(em);
    }

    @Test
    @Transactional
    void createDespesa() throws Exception {
        int databaseSizeBeforeCreate = despesaRepository.findAll().size();
        // Create the Despesa
        restDespesaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(despesa)))
            .andExpect(status().isCreated());

        // Validate the Despesa in the database
        List<Despesa> despesaList = despesaRepository.findAll();
        assertThat(despesaList).hasSize(databaseSizeBeforeCreate + 1);
        Despesa testDespesa = despesaList.get(despesaList.size() - 1);
        assertThat(testDespesa.getMes()).isEqualTo(DEFAULT_MES);
        assertThat(testDespesa.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testDespesa.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testDespesa.getValor()).isEqualTo(DEFAULT_VALOR);
        assertThat(testDespesa.getDtVcto()).isEqualTo(DEFAULT_DT_VCTO);
        assertThat(testDespesa.getDtPgto()).isEqualTo(DEFAULT_DT_PGTO);
        assertThat(testDespesa.getFormaPgto()).isEqualTo(DEFAULT_FORMA_PGTO);
        assertThat(testDespesa.getResponsavel()).isEqualTo(DEFAULT_RESPONSAVEL);
        assertThat(testDespesa.getDtCadastro()).isEqualTo(DEFAULT_DT_CADASTRO);
    }

    @Test
    @Transactional
    void createDespesaWithExistingId() throws Exception {
        // Create the Despesa with an existing ID
        despesa.setId(1L);

        int databaseSizeBeforeCreate = despesaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDespesaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(despesa)))
            .andExpect(status().isBadRequest());

        // Validate the Despesa in the database
        List<Despesa> despesaList = despesaRepository.findAll();
        assertThat(despesaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDespesas() throws Exception {
        // Initialize the database
        despesaRepository.saveAndFlush(despesa);

        // Get all the despesaList
        restDespesaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(despesa.getId().intValue())))
            .andExpect(jsonPath("$.[*].mes").value(hasItem(DEFAULT_MES.toString())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.doubleValue())))
            .andExpect(jsonPath("$.[*].dtVcto").value(hasItem(DEFAULT_DT_VCTO.toString())))
            .andExpect(jsonPath("$.[*].dtPgto").value(hasItem(DEFAULT_DT_PGTO.toString())))
            .andExpect(jsonPath("$.[*].formaPgto").value(hasItem(DEFAULT_FORMA_PGTO.toString())))
            .andExpect(jsonPath("$.[*].responsavel").value(hasItem(DEFAULT_RESPONSAVEL.toString())))
            .andExpect(jsonPath("$.[*].dtCadastro").value(hasItem(DEFAULT_DT_CADASTRO.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDespesasWithEagerRelationshipsIsEnabled() throws Exception {
        when(despesaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDespesaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(despesaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDespesasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(despesaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDespesaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(despesaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getDespesa() throws Exception {
        // Initialize the database
        despesaRepository.saveAndFlush(despesa);

        // Get the despesa
        restDespesaMockMvc
            .perform(get(ENTITY_API_URL_ID, despesa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(despesa.getId().intValue()))
            .andExpect(jsonPath("$.mes").value(DEFAULT_MES.toString()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR.doubleValue()))
            .andExpect(jsonPath("$.dtVcto").value(DEFAULT_DT_VCTO.toString()))
            .andExpect(jsonPath("$.dtPgto").value(DEFAULT_DT_PGTO.toString()))
            .andExpect(jsonPath("$.formaPgto").value(DEFAULT_FORMA_PGTO.toString()))
            .andExpect(jsonPath("$.responsavel").value(DEFAULT_RESPONSAVEL.toString()))
            .andExpect(jsonPath("$.dtCadastro").value(DEFAULT_DT_CADASTRO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDespesa() throws Exception {
        // Get the despesa
        restDespesaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDespesa() throws Exception {
        // Initialize the database
        despesaRepository.saveAndFlush(despesa);

        int databaseSizeBeforeUpdate = despesaRepository.findAll().size();

        // Update the despesa
        Despesa updatedDespesa = despesaRepository.findById(despesa.getId()).get();
        // Disconnect from session so that the updates on updatedDespesa are not directly saved in db
        em.detach(updatedDespesa);
        updatedDespesa
            .mes(UPDATED_MES)
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .valor(UPDATED_VALOR)
            .dtVcto(UPDATED_DT_VCTO)
            .dtPgto(UPDATED_DT_PGTO)
            .formaPgto(UPDATED_FORMA_PGTO)
            .responsavel(UPDATED_RESPONSAVEL)
            .dtCadastro(UPDATED_DT_CADASTRO);

        restDespesaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDespesa.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDespesa))
            )
            .andExpect(status().isOk());

        // Validate the Despesa in the database
        List<Despesa> despesaList = despesaRepository.findAll();
        assertThat(despesaList).hasSize(databaseSizeBeforeUpdate);
        Despesa testDespesa = despesaList.get(despesaList.size() - 1);
        assertThat(testDespesa.getMes()).isEqualTo(UPDATED_MES);
        assertThat(testDespesa.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testDespesa.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testDespesa.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testDespesa.getDtVcto()).isEqualTo(UPDATED_DT_VCTO);
        assertThat(testDespesa.getDtPgto()).isEqualTo(UPDATED_DT_PGTO);
        assertThat(testDespesa.getFormaPgto()).isEqualTo(UPDATED_FORMA_PGTO);
        assertThat(testDespesa.getResponsavel()).isEqualTo(UPDATED_RESPONSAVEL);
        assertThat(testDespesa.getDtCadastro()).isEqualTo(UPDATED_DT_CADASTRO);
    }

    @Test
    @Transactional
    void putNonExistingDespesa() throws Exception {
        int databaseSizeBeforeUpdate = despesaRepository.findAll().size();
        despesa.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDespesaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, despesa.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(despesa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Despesa in the database
        List<Despesa> despesaList = despesaRepository.findAll();
        assertThat(despesaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDespesa() throws Exception {
        int databaseSizeBeforeUpdate = despesaRepository.findAll().size();
        despesa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDespesaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(despesa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Despesa in the database
        List<Despesa> despesaList = despesaRepository.findAll();
        assertThat(despesaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDespesa() throws Exception {
        int databaseSizeBeforeUpdate = despesaRepository.findAll().size();
        despesa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDespesaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(despesa)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Despesa in the database
        List<Despesa> despesaList = despesaRepository.findAll();
        assertThat(despesaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDespesaWithPatch() throws Exception {
        // Initialize the database
        despesaRepository.saveAndFlush(despesa);

        int databaseSizeBeforeUpdate = despesaRepository.findAll().size();

        // Update the despesa using partial update
        Despesa partialUpdatedDespesa = new Despesa();
        partialUpdatedDespesa.setId(despesa.getId());

        partialUpdatedDespesa
            .mes(UPDATED_MES)
            .nome(UPDATED_NOME)
            .valor(UPDATED_VALOR)
            .formaPgto(UPDATED_FORMA_PGTO)
            .dtCadastro(UPDATED_DT_CADASTRO);

        restDespesaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDespesa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDespesa))
            )
            .andExpect(status().isOk());

        // Validate the Despesa in the database
        List<Despesa> despesaList = despesaRepository.findAll();
        assertThat(despesaList).hasSize(databaseSizeBeforeUpdate);
        Despesa testDespesa = despesaList.get(despesaList.size() - 1);
        assertThat(testDespesa.getMes()).isEqualTo(UPDATED_MES);
        assertThat(testDespesa.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testDespesa.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testDespesa.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testDespesa.getDtVcto()).isEqualTo(DEFAULT_DT_VCTO);
        assertThat(testDespesa.getDtPgto()).isEqualTo(DEFAULT_DT_PGTO);
        assertThat(testDespesa.getFormaPgto()).isEqualTo(UPDATED_FORMA_PGTO);
        assertThat(testDespesa.getResponsavel()).isEqualTo(DEFAULT_RESPONSAVEL);
        assertThat(testDespesa.getDtCadastro()).isEqualTo(UPDATED_DT_CADASTRO);
    }

    @Test
    @Transactional
    void fullUpdateDespesaWithPatch() throws Exception {
        // Initialize the database
        despesaRepository.saveAndFlush(despesa);

        int databaseSizeBeforeUpdate = despesaRepository.findAll().size();

        // Update the despesa using partial update
        Despesa partialUpdatedDespesa = new Despesa();
        partialUpdatedDespesa.setId(despesa.getId());

        partialUpdatedDespesa
            .mes(UPDATED_MES)
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .valor(UPDATED_VALOR)
            .dtVcto(UPDATED_DT_VCTO)
            .dtPgto(UPDATED_DT_PGTO)
            .formaPgto(UPDATED_FORMA_PGTO)
            .responsavel(UPDATED_RESPONSAVEL)
            .dtCadastro(UPDATED_DT_CADASTRO);

        restDespesaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDespesa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDespesa))
            )
            .andExpect(status().isOk());

        // Validate the Despesa in the database
        List<Despesa> despesaList = despesaRepository.findAll();
        assertThat(despesaList).hasSize(databaseSizeBeforeUpdate);
        Despesa testDespesa = despesaList.get(despesaList.size() - 1);
        assertThat(testDespesa.getMes()).isEqualTo(UPDATED_MES);
        assertThat(testDespesa.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testDespesa.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testDespesa.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testDespesa.getDtVcto()).isEqualTo(UPDATED_DT_VCTO);
        assertThat(testDespesa.getDtPgto()).isEqualTo(UPDATED_DT_PGTO);
        assertThat(testDespesa.getFormaPgto()).isEqualTo(UPDATED_FORMA_PGTO);
        assertThat(testDespesa.getResponsavel()).isEqualTo(UPDATED_RESPONSAVEL);
        assertThat(testDespesa.getDtCadastro()).isEqualTo(UPDATED_DT_CADASTRO);
    }

    @Test
    @Transactional
    void patchNonExistingDespesa() throws Exception {
        int databaseSizeBeforeUpdate = despesaRepository.findAll().size();
        despesa.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDespesaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, despesa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(despesa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Despesa in the database
        List<Despesa> despesaList = despesaRepository.findAll();
        assertThat(despesaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDespesa() throws Exception {
        int databaseSizeBeforeUpdate = despesaRepository.findAll().size();
        despesa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDespesaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(despesa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Despesa in the database
        List<Despesa> despesaList = despesaRepository.findAll();
        assertThat(despesaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDespesa() throws Exception {
        int databaseSizeBeforeUpdate = despesaRepository.findAll().size();
        despesa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDespesaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(despesa)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Despesa in the database
        List<Despesa> despesaList = despesaRepository.findAll();
        assertThat(despesaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDespesa() throws Exception {
        // Initialize the database
        despesaRepository.saveAndFlush(despesa);

        int databaseSizeBeforeDelete = despesaRepository.findAll().size();

        // Delete the despesa
        restDespesaMockMvc
            .perform(delete(ENTITY_API_URL_ID, despesa.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Despesa> despesaList = despesaRepository.findAll();
        assertThat(despesaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
