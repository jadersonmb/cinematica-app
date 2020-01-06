package com.cinematica.web.rest;

import com.cinematica.CinematicaApp;
import com.cinematica.domain.HorarioDisponivel;
import com.cinematica.repository.HorarioDisponivelRepository;
import com.cinematica.repository.search.HorarioDisponivelSearchRepository;
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
 * Integration tests for the {@link HorarioDisponivelResource} REST controller.
 */
@SpringBootTest(classes = CinematicaApp.class)
public class HorarioDisponivelResourceIT {

    @Autowired
    private HorarioDisponivelRepository horarioDisponivelRepository;

    /**
     * This repository is mocked in the com.cinematica.repository.search test package.
     *
     * @see com.cinematica.repository.search.HorarioDisponivelSearchRepositoryMockConfiguration
     */
    @Autowired
    private HorarioDisponivelSearchRepository mockHorarioDisponivelSearchRepository;

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

    private MockMvc restHorarioDisponivelMockMvc;

    private HorarioDisponivel horarioDisponivel;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HorarioDisponivelResource horarioDisponivelResource = new HorarioDisponivelResource(horarioDisponivelRepository, mockHorarioDisponivelSearchRepository);
        this.restHorarioDisponivelMockMvc = MockMvcBuilders.standaloneSetup(horarioDisponivelResource)
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
    public static HorarioDisponivel createEntity(EntityManager em) {
        HorarioDisponivel horarioDisponivel = new HorarioDisponivel();
        return horarioDisponivel;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HorarioDisponivel createUpdatedEntity(EntityManager em) {
        HorarioDisponivel horarioDisponivel = new HorarioDisponivel();
        return horarioDisponivel;
    }

    @BeforeEach
    public void initTest() {
        horarioDisponivel = createEntity(em);
    }

    @Test
    @Transactional
    public void createHorarioDisponivel() throws Exception {
        int databaseSizeBeforeCreate = horarioDisponivelRepository.findAll().size();

        // Create the HorarioDisponivel
        restHorarioDisponivelMockMvc.perform(post("/api/horario-disponivels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(horarioDisponivel)))
            .andExpect(status().isCreated());

        // Validate the HorarioDisponivel in the database
        List<HorarioDisponivel> horarioDisponivelList = horarioDisponivelRepository.findAll();
        assertThat(horarioDisponivelList).hasSize(databaseSizeBeforeCreate + 1);
        HorarioDisponivel testHorarioDisponivel = horarioDisponivelList.get(horarioDisponivelList.size() - 1);

        // Validate the HorarioDisponivel in Elasticsearch
        verify(mockHorarioDisponivelSearchRepository, times(1)).save(testHorarioDisponivel);
    }

    @Test
    @Transactional
    public void createHorarioDisponivelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = horarioDisponivelRepository.findAll().size();

        // Create the HorarioDisponivel with an existing ID
        horarioDisponivel.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHorarioDisponivelMockMvc.perform(post("/api/horario-disponivels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(horarioDisponivel)))
            .andExpect(status().isBadRequest());

        // Validate the HorarioDisponivel in the database
        List<HorarioDisponivel> horarioDisponivelList = horarioDisponivelRepository.findAll();
        assertThat(horarioDisponivelList).hasSize(databaseSizeBeforeCreate);

        // Validate the HorarioDisponivel in Elasticsearch
        verify(mockHorarioDisponivelSearchRepository, times(0)).save(horarioDisponivel);
    }


    @Test
    @Transactional
    public void getAllHorarioDisponivels() throws Exception {
        // Initialize the database
        horarioDisponivelRepository.saveAndFlush(horarioDisponivel);

        // Get all the horarioDisponivelList
        restHorarioDisponivelMockMvc.perform(get("/api/horario-disponivels?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(horarioDisponivel.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getHorarioDisponivel() throws Exception {
        // Initialize the database
        horarioDisponivelRepository.saveAndFlush(horarioDisponivel);

        // Get the horarioDisponivel
        restHorarioDisponivelMockMvc.perform(get("/api/horario-disponivels/{id}", horarioDisponivel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(horarioDisponivel.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingHorarioDisponivel() throws Exception {
        // Get the horarioDisponivel
        restHorarioDisponivelMockMvc.perform(get("/api/horario-disponivels/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHorarioDisponivel() throws Exception {
        // Initialize the database
        horarioDisponivelRepository.saveAndFlush(horarioDisponivel);

        int databaseSizeBeforeUpdate = horarioDisponivelRepository.findAll().size();

        // Update the horarioDisponivel
        HorarioDisponivel updatedHorarioDisponivel = horarioDisponivelRepository.findById(horarioDisponivel.getId()).get();
        // Disconnect from session so that the updates on updatedHorarioDisponivel are not directly saved in db
        em.detach(updatedHorarioDisponivel);

        restHorarioDisponivelMockMvc.perform(put("/api/horario-disponivels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHorarioDisponivel)))
            .andExpect(status().isOk());

        // Validate the HorarioDisponivel in the database
        List<HorarioDisponivel> horarioDisponivelList = horarioDisponivelRepository.findAll();
        assertThat(horarioDisponivelList).hasSize(databaseSizeBeforeUpdate);
        HorarioDisponivel testHorarioDisponivel = horarioDisponivelList.get(horarioDisponivelList.size() - 1);

        // Validate the HorarioDisponivel in Elasticsearch
        verify(mockHorarioDisponivelSearchRepository, times(1)).save(testHorarioDisponivel);
    }

    @Test
    @Transactional
    public void updateNonExistingHorarioDisponivel() throws Exception {
        int databaseSizeBeforeUpdate = horarioDisponivelRepository.findAll().size();

        // Create the HorarioDisponivel

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHorarioDisponivelMockMvc.perform(put("/api/horario-disponivels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(horarioDisponivel)))
            .andExpect(status().isBadRequest());

        // Validate the HorarioDisponivel in the database
        List<HorarioDisponivel> horarioDisponivelList = horarioDisponivelRepository.findAll();
        assertThat(horarioDisponivelList).hasSize(databaseSizeBeforeUpdate);

        // Validate the HorarioDisponivel in Elasticsearch
        verify(mockHorarioDisponivelSearchRepository, times(0)).save(horarioDisponivel);
    }

    @Test
    @Transactional
    public void deleteHorarioDisponivel() throws Exception {
        // Initialize the database
        horarioDisponivelRepository.saveAndFlush(horarioDisponivel);

        int databaseSizeBeforeDelete = horarioDisponivelRepository.findAll().size();

        // Delete the horarioDisponivel
        restHorarioDisponivelMockMvc.perform(delete("/api/horario-disponivels/{id}", horarioDisponivel.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HorarioDisponivel> horarioDisponivelList = horarioDisponivelRepository.findAll();
        assertThat(horarioDisponivelList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the HorarioDisponivel in Elasticsearch
        verify(mockHorarioDisponivelSearchRepository, times(1)).deleteById(horarioDisponivel.getId());
    }

    @Test
    @Transactional
    public void searchHorarioDisponivel() throws Exception {
        // Initialize the database
        horarioDisponivelRepository.saveAndFlush(horarioDisponivel);
        when(mockHorarioDisponivelSearchRepository.search(queryStringQuery("id:" + horarioDisponivel.getId())))
            .thenReturn(Collections.singletonList(horarioDisponivel));
        // Search the horarioDisponivel
        restHorarioDisponivelMockMvc.perform(get("/api/_search/horario-disponivels?query=id:" + horarioDisponivel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(horarioDisponivel.getId().intValue())));
    }
}
