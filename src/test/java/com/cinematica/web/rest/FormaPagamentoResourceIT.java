package com.cinematica.web.rest;

import com.cinematica.CinematicaApp;
import com.cinematica.domain.FormaPagamento;
import com.cinematica.repository.FormaPagamentoRepository;
import com.cinematica.repository.search.FormaPagamentoSearchRepository;
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
import java.util.Collections;
import java.util.List;

import static com.cinematica.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link FormaPagamentoResource} REST controller.
 */
@SpringBootTest(classes = CinematicaApp.class)
public class FormaPagamentoResourceIT {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    /**
     * This repository is mocked in the com.cinematica.repository.search test package.
     *
     * @see com.cinematica.repository.search.FormaPagamentoSearchRepositoryMockConfiguration
     */
    @Autowired
    private FormaPagamentoSearchRepository mockFormaPagamentoSearchRepository;

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

    private MockMvc restFormaPagamentoMockMvc;

    private FormaPagamento formaPagamento;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FormaPagamentoResource formaPagamentoResource = new FormaPagamentoResource(formaPagamentoRepository, mockFormaPagamentoSearchRepository);
        this.restFormaPagamentoMockMvc = MockMvcBuilders.standaloneSetup(formaPagamentoResource)
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
    public static FormaPagamento createEntity(EntityManager em) {
        FormaPagamento formaPagamento = new FormaPagamento()
            .descricao(DEFAULT_DESCRICAO);
        return formaPagamento;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormaPagamento createUpdatedEntity(EntityManager em) {
        FormaPagamento formaPagamento = new FormaPagamento()
            .descricao(UPDATED_DESCRICAO);
        return formaPagamento;
    }

    @BeforeEach
    public void initTest() {
        formaPagamento = createEntity(em);
    }

    @Test
    @Transactional
    public void createFormaPagamento() throws Exception {
        int databaseSizeBeforeCreate = formaPagamentoRepository.findAll().size();

        // Create the FormaPagamento
        restFormaPagamentoMockMvc.perform(post("/api/forma-pagamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formaPagamento)))
            .andExpect(status().isCreated());

        // Validate the FormaPagamento in the database
        List<FormaPagamento> formaPagamentoList = formaPagamentoRepository.findAll();
        assertThat(formaPagamentoList).hasSize(databaseSizeBeforeCreate + 1);
        FormaPagamento testFormaPagamento = formaPagamentoList.get(formaPagamentoList.size() - 1);
        assertThat(testFormaPagamento.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);

        // Validate the FormaPagamento in Elasticsearch
        verify(mockFormaPagamentoSearchRepository, times(1)).save(testFormaPagamento);
    }

    @Test
    @Transactional
    public void createFormaPagamentoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = formaPagamentoRepository.findAll().size();

        // Create the FormaPagamento with an existing ID
        formaPagamento.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormaPagamentoMockMvc.perform(post("/api/forma-pagamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formaPagamento)))
            .andExpect(status().isBadRequest());

        // Validate the FormaPagamento in the database
        List<FormaPagamento> formaPagamentoList = formaPagamentoRepository.findAll();
        assertThat(formaPagamentoList).hasSize(databaseSizeBeforeCreate);

        // Validate the FormaPagamento in Elasticsearch
        verify(mockFormaPagamentoSearchRepository, times(0)).save(formaPagamento);
    }


    @Test
    @Transactional
    public void getAllFormaPagamentos() throws Exception {
        // Initialize the database
        formaPagamentoRepository.saveAndFlush(formaPagamento);

        // Get all the formaPagamentoList
        restFormaPagamentoMockMvc.perform(get("/api/forma-pagamentos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formaPagamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }
    
    @Test
    @Transactional
    public void getFormaPagamento() throws Exception {
        // Initialize the database
        formaPagamentoRepository.saveAndFlush(formaPagamento);

        // Get the formaPagamento
        restFormaPagamentoMockMvc.perform(get("/api/forma-pagamentos/{id}", formaPagamento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(formaPagamento.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO));
    }

    @Test
    @Transactional
    public void getNonExistingFormaPagamento() throws Exception {
        // Get the formaPagamento
        restFormaPagamentoMockMvc.perform(get("/api/forma-pagamentos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFormaPagamento() throws Exception {
        // Initialize the database
        formaPagamentoRepository.saveAndFlush(formaPagamento);

        int databaseSizeBeforeUpdate = formaPagamentoRepository.findAll().size();

        // Update the formaPagamento
        FormaPagamento updatedFormaPagamento = formaPagamentoRepository.findById(formaPagamento.getId()).get();
        // Disconnect from session so that the updates on updatedFormaPagamento are not directly saved in db
        em.detach(updatedFormaPagamento);
        updatedFormaPagamento
            .descricao(UPDATED_DESCRICAO);

        restFormaPagamentoMockMvc.perform(put("/api/forma-pagamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFormaPagamento)))
            .andExpect(status().isOk());

        // Validate the FormaPagamento in the database
        List<FormaPagamento> formaPagamentoList = formaPagamentoRepository.findAll();
        assertThat(formaPagamentoList).hasSize(databaseSizeBeforeUpdate);
        FormaPagamento testFormaPagamento = formaPagamentoList.get(formaPagamentoList.size() - 1);
        assertThat(testFormaPagamento.getDescricao()).isEqualTo(UPDATED_DESCRICAO);

        // Validate the FormaPagamento in Elasticsearch
        verify(mockFormaPagamentoSearchRepository, times(1)).save(testFormaPagamento);
    }

    @Test
    @Transactional
    public void updateNonExistingFormaPagamento() throws Exception {
        int databaseSizeBeforeUpdate = formaPagamentoRepository.findAll().size();

        // Create the FormaPagamento

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormaPagamentoMockMvc.perform(put("/api/forma-pagamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formaPagamento)))
            .andExpect(status().isBadRequest());

        // Validate the FormaPagamento in the database
        List<FormaPagamento> formaPagamentoList = formaPagamentoRepository.findAll();
        assertThat(formaPagamentoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FormaPagamento in Elasticsearch
        verify(mockFormaPagamentoSearchRepository, times(0)).save(formaPagamento);
    }

    @Test
    @Transactional
    public void deleteFormaPagamento() throws Exception {
        // Initialize the database
        formaPagamentoRepository.saveAndFlush(formaPagamento);

        int databaseSizeBeforeDelete = formaPagamentoRepository.findAll().size();

        // Delete the formaPagamento
        restFormaPagamentoMockMvc.perform(delete("/api/forma-pagamentos/{id}", formaPagamento.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FormaPagamento> formaPagamentoList = formaPagamentoRepository.findAll();
        assertThat(formaPagamentoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FormaPagamento in Elasticsearch
        verify(mockFormaPagamentoSearchRepository, times(1)).deleteById(formaPagamento.getId());
    }

    @Test
    @Transactional
    public void searchFormaPagamento() throws Exception {
        // Initialize the database
        formaPagamentoRepository.saveAndFlush(formaPagamento);
        when(mockFormaPagamentoSearchRepository.search(queryStringQuery("id:" + formaPagamento.getId())))
            .thenReturn(Collections.singletonList(formaPagamento));
        // Search the formaPagamento
        restFormaPagamentoMockMvc.perform(get("/api/_search/forma-pagamentos?query=id:" + formaPagamento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formaPagamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }
}
