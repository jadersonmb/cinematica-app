package com.cinematica.web.rest;

import com.cinematica.CinematicaApp;
import com.cinematica.domain.FluxoCaixa;
import com.cinematica.repository.FluxoCaixaRepository;
import com.cinematica.repository.search.FluxoCaixaSearchRepository;
import com.cinematica.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static com.cinematica.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cinematica.domain.enumeration.TipoLancamento;
/**
 * Integration tests for the {@link FluxoCaixaResource} REST controller.
 */
@SpringBootTest(classes = CinematicaApp.class)
public class FluxoCaixaResourceIT {

    private static final Instant DEFAULT_DATA_LANCAMENTO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_LANCAMENTO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final Double DEFAULT_VALOR = 1D;
    private static final Double UPDATED_VALOR = 2D;

    private static final TipoLancamento DEFAULT_TIPO_LANCAMENTO = TipoLancamento.Receita;
    private static final TipoLancamento UPDATED_TIPO_LANCAMENTO = TipoLancamento.Despesa;

    private static final String DEFAULT_NUMERO_RECIBO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_RECIBO = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTIDADE_PARCELA = 1;
    private static final Integer UPDATED_QUANTIDADE_PARCELA = 2;

    @Autowired
    private FluxoCaixaRepository fluxoCaixaRepository;

    /**
     * This repository is mocked in the com.cinematica.repository.search test package.
     *
     * @see com.cinematica.repository.search.FluxoCaixaSearchRepositoryMockConfiguration
     */
    @Autowired
    private FluxoCaixaSearchRepository mockFluxoCaixaSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restFluxoCaixaMockMvc;

    private FluxoCaixa fluxoCaixa;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FluxoCaixaResource fluxoCaixaResource = new FluxoCaixaResource(fluxoCaixaRepository, mockFluxoCaixaSearchRepository);
        this.restFluxoCaixaMockMvc = MockMvcBuilders.standaloneSetup(fluxoCaixaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FluxoCaixa createEntity(EntityManager em) {
        FluxoCaixa fluxoCaixa = new FluxoCaixa()
            .dataLancamento(DEFAULT_DATA_LANCAMENTO)
            .descricao(DEFAULT_DESCRICAO)
            .valor(DEFAULT_VALOR)
            .tipoLancamento(DEFAULT_TIPO_LANCAMENTO)
            .numeroRecibo(DEFAULT_NUMERO_RECIBO)
            .quantidadeParcela(DEFAULT_QUANTIDADE_PARCELA);
        return fluxoCaixa;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FluxoCaixa createUpdatedEntity(EntityManager em) {
        FluxoCaixa fluxoCaixa = new FluxoCaixa()
            .dataLancamento(UPDATED_DATA_LANCAMENTO)
            .descricao(UPDATED_DESCRICAO)
            .valor(UPDATED_VALOR)
            .tipoLancamento(UPDATED_TIPO_LANCAMENTO)
            .numeroRecibo(UPDATED_NUMERO_RECIBO)
            .quantidadeParcela(UPDATED_QUANTIDADE_PARCELA);
        return fluxoCaixa;
    }

    @BeforeEach
    public void initTest() {
        fluxoCaixa = createEntity(em);
    }

    @Test
    @Transactional
    public void createFluxoCaixa() throws Exception {
        int databaseSizeBeforeCreate = fluxoCaixaRepository.findAll().size();

        // Create the FluxoCaixa
        restFluxoCaixaMockMvc.perform(post("/api/fluxo-caixas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fluxoCaixa)))
            .andExpect(status().isCreated());

        // Validate the FluxoCaixa in the database
        List<FluxoCaixa> fluxoCaixaList = fluxoCaixaRepository.findAll();
        assertThat(fluxoCaixaList).hasSize(databaseSizeBeforeCreate + 1);
        FluxoCaixa testFluxoCaixa = fluxoCaixaList.get(fluxoCaixaList.size() - 1);
        assertThat(testFluxoCaixa.getDataLancamento()).isEqualTo(DEFAULT_DATA_LANCAMENTO);
        assertThat(testFluxoCaixa.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testFluxoCaixa.getValor()).isEqualTo(DEFAULT_VALOR);
        assertThat(testFluxoCaixa.getTipoLancamento()).isEqualTo(DEFAULT_TIPO_LANCAMENTO);
        assertThat(testFluxoCaixa.getNumeroRecibo()).isEqualTo(DEFAULT_NUMERO_RECIBO);
        assertThat(testFluxoCaixa.getQuantidadeParcela()).isEqualTo(DEFAULT_QUANTIDADE_PARCELA);

        // Validate the FluxoCaixa in Elasticsearch
        verify(mockFluxoCaixaSearchRepository, times(1)).save(testFluxoCaixa);
    }

    @Test
    @Transactional
    public void createFluxoCaixaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fluxoCaixaRepository.findAll().size();

        // Create the FluxoCaixa with an existing ID
        fluxoCaixa.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFluxoCaixaMockMvc.perform(post("/api/fluxo-caixas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fluxoCaixa)))
            .andExpect(status().isBadRequest());

        // Validate the FluxoCaixa in the database
        List<FluxoCaixa> fluxoCaixaList = fluxoCaixaRepository.findAll();
        assertThat(fluxoCaixaList).hasSize(databaseSizeBeforeCreate);

        // Validate the FluxoCaixa in Elasticsearch
        verify(mockFluxoCaixaSearchRepository, times(0)).save(fluxoCaixa);
    }


    @Test
    @Transactional
    public void getAllFluxoCaixas() throws Exception {
        // Initialize the database
        fluxoCaixaRepository.saveAndFlush(fluxoCaixa);

        // Get all the fluxoCaixaList
        restFluxoCaixaMockMvc.perform(get("/api/fluxo-caixas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fluxoCaixa.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataLancamento").value(hasItem(DEFAULT_DATA_LANCAMENTO.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.doubleValue())))
            .andExpect(jsonPath("$.[*].tipoLancamento").value(hasItem(DEFAULT_TIPO_LANCAMENTO.toString())))
            .andExpect(jsonPath("$.[*].numeroRecibo").value(hasItem(DEFAULT_NUMERO_RECIBO)))
            .andExpect(jsonPath("$.[*].quantidadeParcela").value(hasItem(DEFAULT_QUANTIDADE_PARCELA)));
    }
    
    @Test
    @Transactional
    public void getFluxoCaixa() throws Exception {
        // Initialize the database
        fluxoCaixaRepository.saveAndFlush(fluxoCaixa);

        // Get the fluxoCaixa
        restFluxoCaixaMockMvc.perform(get("/api/fluxo-caixas/{id}", fluxoCaixa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(fluxoCaixa.getId().intValue()))
            .andExpect(jsonPath("$.dataLancamento").value(DEFAULT_DATA_LANCAMENTO.toString()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR.doubleValue()))
            .andExpect(jsonPath("$.tipoLancamento").value(DEFAULT_TIPO_LANCAMENTO.toString()))
            .andExpect(jsonPath("$.numeroRecibo").value(DEFAULT_NUMERO_RECIBO))
            .andExpect(jsonPath("$.quantidadeParcela").value(DEFAULT_QUANTIDADE_PARCELA));
    }

    @Test
    @Transactional
    public void getNonExistingFluxoCaixa() throws Exception {
        // Get the fluxoCaixa
        restFluxoCaixaMockMvc.perform(get("/api/fluxo-caixas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFluxoCaixa() throws Exception {
        // Initialize the database
        fluxoCaixaRepository.saveAndFlush(fluxoCaixa);

        int databaseSizeBeforeUpdate = fluxoCaixaRepository.findAll().size();

        // Update the fluxoCaixa
        FluxoCaixa updatedFluxoCaixa = fluxoCaixaRepository.findById(fluxoCaixa.getId()).get();
        // Disconnect from session so that the updates on updatedFluxoCaixa are not directly saved in db
        em.detach(updatedFluxoCaixa);
        updatedFluxoCaixa
            .dataLancamento(UPDATED_DATA_LANCAMENTO)
            .descricao(UPDATED_DESCRICAO)
            .valor(UPDATED_VALOR)
            .tipoLancamento(UPDATED_TIPO_LANCAMENTO)
            .numeroRecibo(UPDATED_NUMERO_RECIBO)
            .quantidadeParcela(UPDATED_QUANTIDADE_PARCELA);

        restFluxoCaixaMockMvc.perform(put("/api/fluxo-caixas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFluxoCaixa)))
            .andExpect(status().isOk());

        // Validate the FluxoCaixa in the database
        List<FluxoCaixa> fluxoCaixaList = fluxoCaixaRepository.findAll();
        assertThat(fluxoCaixaList).hasSize(databaseSizeBeforeUpdate);
        FluxoCaixa testFluxoCaixa = fluxoCaixaList.get(fluxoCaixaList.size() - 1);
        assertThat(testFluxoCaixa.getDataLancamento()).isEqualTo(UPDATED_DATA_LANCAMENTO);
        assertThat(testFluxoCaixa.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testFluxoCaixa.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testFluxoCaixa.getTipoLancamento()).isEqualTo(UPDATED_TIPO_LANCAMENTO);
        assertThat(testFluxoCaixa.getNumeroRecibo()).isEqualTo(UPDATED_NUMERO_RECIBO);
        assertThat(testFluxoCaixa.getQuantidadeParcela()).isEqualTo(UPDATED_QUANTIDADE_PARCELA);

        // Validate the FluxoCaixa in Elasticsearch
        verify(mockFluxoCaixaSearchRepository, times(1)).save(testFluxoCaixa);
    }

    @Test
    @Transactional
    public void updateNonExistingFluxoCaixa() throws Exception {
        int databaseSizeBeforeUpdate = fluxoCaixaRepository.findAll().size();

        // Create the FluxoCaixa

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFluxoCaixaMockMvc.perform(put("/api/fluxo-caixas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fluxoCaixa)))
            .andExpect(status().isBadRequest());

        // Validate the FluxoCaixa in the database
        List<FluxoCaixa> fluxoCaixaList = fluxoCaixaRepository.findAll();
        assertThat(fluxoCaixaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FluxoCaixa in Elasticsearch
        verify(mockFluxoCaixaSearchRepository, times(0)).save(fluxoCaixa);
    }

    @Test
    @Transactional
    public void deleteFluxoCaixa() throws Exception {
        // Initialize the database
        fluxoCaixaRepository.saveAndFlush(fluxoCaixa);

        int databaseSizeBeforeDelete = fluxoCaixaRepository.findAll().size();

        // Delete the fluxoCaixa
        restFluxoCaixaMockMvc.perform(delete("/api/fluxo-caixas/{id}", fluxoCaixa.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FluxoCaixa> fluxoCaixaList = fluxoCaixaRepository.findAll();
        assertThat(fluxoCaixaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FluxoCaixa in Elasticsearch
        verify(mockFluxoCaixaSearchRepository, times(1)).deleteById(fluxoCaixa.getId());
    }

    @Test
    @Transactional
    public void searchFluxoCaixa() throws Exception {
        // Initialize the database
        fluxoCaixaRepository.saveAndFlush(fluxoCaixa);
        when(mockFluxoCaixaSearchRepository.search(queryStringQuery("id:" + fluxoCaixa.getId())))
            .thenReturn(Collections.singletonList(fluxoCaixa));
        // Search the fluxoCaixa
        restFluxoCaixaMockMvc.perform(get("/api/_search/fluxo-caixas?query=id:" + fluxoCaixa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fluxoCaixa.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataLancamento").value(hasItem(DEFAULT_DATA_LANCAMENTO.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.doubleValue())))
            .andExpect(jsonPath("$.[*].tipoLancamento").value(hasItem(DEFAULT_TIPO_LANCAMENTO.toString())))
            .andExpect(jsonPath("$.[*].numeroRecibo").value(hasItem(DEFAULT_NUMERO_RECIBO)))
            .andExpect(jsonPath("$.[*].quantidadeParcela").value(hasItem(DEFAULT_QUANTIDADE_PARCELA)));
    }
}
