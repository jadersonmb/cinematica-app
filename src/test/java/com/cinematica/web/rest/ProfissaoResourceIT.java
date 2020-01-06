package com.cinematica.web.rest;

import com.cinematica.CinematicaApp;
import com.cinematica.domain.Profissao;
import com.cinematica.repository.ProfissaoRepository;
import com.cinematica.repository.search.ProfissaoSearchRepository;
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
 * Integration tests for the {@link ProfissaoResource} REST controller.
 */
@SpringBootTest(classes = CinematicaApp.class)
public class ProfissaoResourceIT {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    @Autowired
    private ProfissaoRepository profissaoRepository;

    /**
     * This repository is mocked in the com.cinematica.repository.search test package.
     *
     * @see com.cinematica.repository.search.ProfissaoSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProfissaoSearchRepository mockProfissaoSearchRepository;

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

    private MockMvc restProfissaoMockMvc;

    private Profissao profissao;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProfissaoResource profissaoResource = new ProfissaoResource(profissaoRepository, mockProfissaoSearchRepository);
        this.restProfissaoMockMvc = MockMvcBuilders.standaloneSetup(profissaoResource)
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
    public static Profissao createEntity(EntityManager em) {
        Profissao profissao = new Profissao()
            .descricao(DEFAULT_DESCRICAO);
        return profissao;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profissao createUpdatedEntity(EntityManager em) {
        Profissao profissao = new Profissao()
            .descricao(UPDATED_DESCRICAO);
        return profissao;
    }

    @BeforeEach
    public void initTest() {
        profissao = createEntity(em);
    }

    @Test
    @Transactional
    public void createProfissao() throws Exception {
        int databaseSizeBeforeCreate = profissaoRepository.findAll().size();

        // Create the Profissao
        restProfissaoMockMvc.perform(post("/api/profissaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profissao)))
            .andExpect(status().isCreated());

        // Validate the Profissao in the database
        List<Profissao> profissaoList = profissaoRepository.findAll();
        assertThat(profissaoList).hasSize(databaseSizeBeforeCreate + 1);
        Profissao testProfissao = profissaoList.get(profissaoList.size() - 1);
        assertThat(testProfissao.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);

        // Validate the Profissao in Elasticsearch
        verify(mockProfissaoSearchRepository, times(1)).save(testProfissao);
    }

    @Test
    @Transactional
    public void createProfissaoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = profissaoRepository.findAll().size();

        // Create the Profissao with an existing ID
        profissao.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfissaoMockMvc.perform(post("/api/profissaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profissao)))
            .andExpect(status().isBadRequest());

        // Validate the Profissao in the database
        List<Profissao> profissaoList = profissaoRepository.findAll();
        assertThat(profissaoList).hasSize(databaseSizeBeforeCreate);

        // Validate the Profissao in Elasticsearch
        verify(mockProfissaoSearchRepository, times(0)).save(profissao);
    }


    @Test
    @Transactional
    public void getAllProfissaos() throws Exception {
        // Initialize the database
        profissaoRepository.saveAndFlush(profissao);

        // Get all the profissaoList
        restProfissaoMockMvc.perform(get("/api/profissaos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profissao.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }
    
    @Test
    @Transactional
    public void getProfissao() throws Exception {
        // Initialize the database
        profissaoRepository.saveAndFlush(profissao);

        // Get the profissao
        restProfissaoMockMvc.perform(get("/api/profissaos/{id}", profissao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(profissao.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO));
    }

    @Test
    @Transactional
    public void getNonExistingProfissao() throws Exception {
        // Get the profissao
        restProfissaoMockMvc.perform(get("/api/profissaos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProfissao() throws Exception {
        // Initialize the database
        profissaoRepository.saveAndFlush(profissao);

        int databaseSizeBeforeUpdate = profissaoRepository.findAll().size();

        // Update the profissao
        Profissao updatedProfissao = profissaoRepository.findById(profissao.getId()).get();
        // Disconnect from session so that the updates on updatedProfissao are not directly saved in db
        em.detach(updatedProfissao);
        updatedProfissao
            .descricao(UPDATED_DESCRICAO);

        restProfissaoMockMvc.perform(put("/api/profissaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProfissao)))
            .andExpect(status().isOk());

        // Validate the Profissao in the database
        List<Profissao> profissaoList = profissaoRepository.findAll();
        assertThat(profissaoList).hasSize(databaseSizeBeforeUpdate);
        Profissao testProfissao = profissaoList.get(profissaoList.size() - 1);
        assertThat(testProfissao.getDescricao()).isEqualTo(UPDATED_DESCRICAO);

        // Validate the Profissao in Elasticsearch
        verify(mockProfissaoSearchRepository, times(1)).save(testProfissao);
    }

    @Test
    @Transactional
    public void updateNonExistingProfissao() throws Exception {
        int databaseSizeBeforeUpdate = profissaoRepository.findAll().size();

        // Create the Profissao

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfissaoMockMvc.perform(put("/api/profissaos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profissao)))
            .andExpect(status().isBadRequest());

        // Validate the Profissao in the database
        List<Profissao> profissaoList = profissaoRepository.findAll();
        assertThat(profissaoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Profissao in Elasticsearch
        verify(mockProfissaoSearchRepository, times(0)).save(profissao);
    }

    @Test
    @Transactional
    public void deleteProfissao() throws Exception {
        // Initialize the database
        profissaoRepository.saveAndFlush(profissao);

        int databaseSizeBeforeDelete = profissaoRepository.findAll().size();

        // Delete the profissao
        restProfissaoMockMvc.perform(delete("/api/profissaos/{id}", profissao.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Profissao> profissaoList = profissaoRepository.findAll();
        assertThat(profissaoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Profissao in Elasticsearch
        verify(mockProfissaoSearchRepository, times(1)).deleteById(profissao.getId());
    }

    @Test
    @Transactional
    public void searchProfissao() throws Exception {
        // Initialize the database
        profissaoRepository.saveAndFlush(profissao);
        when(mockProfissaoSearchRepository.search(queryStringQuery("id:" + profissao.getId())))
            .thenReturn(Collections.singletonList(profissao));
        // Search the profissao
        restProfissaoMockMvc.perform(get("/api/_search/profissaos?query=id:" + profissao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profissao.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }
}
