package com.cinematica.web.rest;

import com.cinematica.CinematicaApp;
import com.cinematica.domain.Especialidade;
import com.cinematica.repository.EspecialidadeRepository;
import com.cinematica.repository.search.EspecialidadeSearchRepository;
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
 * Integration tests for the {@link EspecialidadeResource} REST controller.
 */
@SpringBootTest(classes = CinematicaApp.class)
public class EspecialidadeResourceIT {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    @Autowired
    private EspecialidadeRepository especialidadeRepository;

    /**
     * This repository is mocked in the com.cinematica.repository.search test package.
     *
     * @see com.cinematica.repository.search.EspecialidadeSearchRepositoryMockConfiguration
     */
    @Autowired
    private EspecialidadeSearchRepository mockEspecialidadeSearchRepository;

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

    private MockMvc restEspecialidadeMockMvc;

    private Especialidade especialidade;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EspecialidadeResource especialidadeResource = new EspecialidadeResource(especialidadeRepository, mockEspecialidadeSearchRepository);
        this.restEspecialidadeMockMvc = MockMvcBuilders.standaloneSetup(especialidadeResource)
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
    public static Especialidade createEntity(EntityManager em) {
        Especialidade especialidade = new Especialidade()
            .descricao(DEFAULT_DESCRICAO);
        return especialidade;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Especialidade createUpdatedEntity(EntityManager em) {
        Especialidade especialidade = new Especialidade()
            .descricao(UPDATED_DESCRICAO);
        return especialidade;
    }

    @BeforeEach
    public void initTest() {
        especialidade = createEntity(em);
    }

    @Test
    @Transactional
    public void createEspecialidade() throws Exception {
        int databaseSizeBeforeCreate = especialidadeRepository.findAll().size();

        // Create the Especialidade
        restEspecialidadeMockMvc.perform(post("/api/especialidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(especialidade)))
            .andExpect(status().isCreated());

        // Validate the Especialidade in the database
        List<Especialidade> especialidadeList = especialidadeRepository.findAll();
        assertThat(especialidadeList).hasSize(databaseSizeBeforeCreate + 1);
        Especialidade testEspecialidade = especialidadeList.get(especialidadeList.size() - 1);
        assertThat(testEspecialidade.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);

        // Validate the Especialidade in Elasticsearch
        verify(mockEspecialidadeSearchRepository, times(1)).save(testEspecialidade);
    }

    @Test
    @Transactional
    public void createEspecialidadeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = especialidadeRepository.findAll().size();

        // Create the Especialidade with an existing ID
        especialidade.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEspecialidadeMockMvc.perform(post("/api/especialidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(especialidade)))
            .andExpect(status().isBadRequest());

        // Validate the Especialidade in the database
        List<Especialidade> especialidadeList = especialidadeRepository.findAll();
        assertThat(especialidadeList).hasSize(databaseSizeBeforeCreate);

        // Validate the Especialidade in Elasticsearch
        verify(mockEspecialidadeSearchRepository, times(0)).save(especialidade);
    }


    @Test
    @Transactional
    public void getAllEspecialidades() throws Exception {
        // Initialize the database
        especialidadeRepository.saveAndFlush(especialidade);

        // Get all the especialidadeList
        restEspecialidadeMockMvc.perform(get("/api/especialidades?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(especialidade.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }
    
    @Test
    @Transactional
    public void getEspecialidade() throws Exception {
        // Initialize the database
        especialidadeRepository.saveAndFlush(especialidade);

        // Get the especialidade
        restEspecialidadeMockMvc.perform(get("/api/especialidades/{id}", especialidade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(especialidade.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO));
    }

    @Test
    @Transactional
    public void getNonExistingEspecialidade() throws Exception {
        // Get the especialidade
        restEspecialidadeMockMvc.perform(get("/api/especialidades/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEspecialidade() throws Exception {
        // Initialize the database
        especialidadeRepository.saveAndFlush(especialidade);

        int databaseSizeBeforeUpdate = especialidadeRepository.findAll().size();

        // Update the especialidade
        Especialidade updatedEspecialidade = especialidadeRepository.findById(especialidade.getId()).get();
        // Disconnect from session so that the updates on updatedEspecialidade are not directly saved in db
        em.detach(updatedEspecialidade);
        updatedEspecialidade
            .descricao(UPDATED_DESCRICAO);

        restEspecialidadeMockMvc.perform(put("/api/especialidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEspecialidade)))
            .andExpect(status().isOk());

        // Validate the Especialidade in the database
        List<Especialidade> especialidadeList = especialidadeRepository.findAll();
        assertThat(especialidadeList).hasSize(databaseSizeBeforeUpdate);
        Especialidade testEspecialidade = especialidadeList.get(especialidadeList.size() - 1);
        assertThat(testEspecialidade.getDescricao()).isEqualTo(UPDATED_DESCRICAO);

        // Validate the Especialidade in Elasticsearch
        verify(mockEspecialidadeSearchRepository, times(1)).save(testEspecialidade);
    }

    @Test
    @Transactional
    public void updateNonExistingEspecialidade() throws Exception {
        int databaseSizeBeforeUpdate = especialidadeRepository.findAll().size();

        // Create the Especialidade

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEspecialidadeMockMvc.perform(put("/api/especialidades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(especialidade)))
            .andExpect(status().isBadRequest());

        // Validate the Especialidade in the database
        List<Especialidade> especialidadeList = especialidadeRepository.findAll();
        assertThat(especialidadeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Especialidade in Elasticsearch
        verify(mockEspecialidadeSearchRepository, times(0)).save(especialidade);
    }

    @Test
    @Transactional
    public void deleteEspecialidade() throws Exception {
        // Initialize the database
        especialidadeRepository.saveAndFlush(especialidade);

        int databaseSizeBeforeDelete = especialidadeRepository.findAll().size();

        // Delete the especialidade
        restEspecialidadeMockMvc.perform(delete("/api/especialidades/{id}", especialidade.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Especialidade> especialidadeList = especialidadeRepository.findAll();
        assertThat(especialidadeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Especialidade in Elasticsearch
        verify(mockEspecialidadeSearchRepository, times(1)).deleteById(especialidade.getId());
    }

    @Test
    @Transactional
    public void searchEspecialidade() throws Exception {
        // Initialize the database
        especialidadeRepository.saveAndFlush(especialidade);
        when(mockEspecialidadeSearchRepository.search(queryStringQuery("id:" + especialidade.getId())))
            .thenReturn(Collections.singletonList(especialidade));
        // Search the especialidade
        restEspecialidadeMockMvc.perform(get("/api/_search/especialidades?query=id:" + especialidade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(especialidade.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }
}
