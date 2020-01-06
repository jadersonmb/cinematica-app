package com.cinematica.web.rest;

import com.cinematica.CinematicaApp;
import com.cinematica.domain.DataFalta;
import com.cinematica.repository.DataFaltaRepository;
import com.cinematica.repository.search.DataFaltaSearchRepository;
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

/**
 * Integration tests for the {@link DataFaltaResource} REST controller.
 */
@SpringBootTest(classes = CinematicaApp.class)
public class DataFaltaResourceIT {

    private static final Instant DEFAULT_DATA_FALTA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_FALTA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private DataFaltaRepository dataFaltaRepository;

    /**
     * This repository is mocked in the com.cinematica.repository.search test package.
     *
     * @see com.cinematica.repository.search.DataFaltaSearchRepositoryMockConfiguration
     */
    @Autowired
    private DataFaltaSearchRepository mockDataFaltaSearchRepository;

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

    private MockMvc restDataFaltaMockMvc;

    private DataFalta dataFalta;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DataFaltaResource dataFaltaResource = new DataFaltaResource(dataFaltaRepository, mockDataFaltaSearchRepository);
        this.restDataFaltaMockMvc = MockMvcBuilders.standaloneSetup(dataFaltaResource)
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
    public static DataFalta createEntity(EntityManager em) {
        DataFalta dataFalta = new DataFalta()
            .dataFalta(DEFAULT_DATA_FALTA);
        return dataFalta;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DataFalta createUpdatedEntity(EntityManager em) {
        DataFalta dataFalta = new DataFalta()
            .dataFalta(UPDATED_DATA_FALTA);
        return dataFalta;
    }

    @BeforeEach
    public void initTest() {
        dataFalta = createEntity(em);
    }

    @Test
    @Transactional
    public void createDataFalta() throws Exception {
        int databaseSizeBeforeCreate = dataFaltaRepository.findAll().size();

        // Create the DataFalta
        restDataFaltaMockMvc.perform(post("/api/data-faltas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataFalta)))
            .andExpect(status().isCreated());

        // Validate the DataFalta in the database
        List<DataFalta> dataFaltaList = dataFaltaRepository.findAll();
        assertThat(dataFaltaList).hasSize(databaseSizeBeforeCreate + 1);
        DataFalta testDataFalta = dataFaltaList.get(dataFaltaList.size() - 1);
        assertThat(testDataFalta.getDataFalta()).isEqualTo(DEFAULT_DATA_FALTA);

        // Validate the DataFalta in Elasticsearch
        verify(mockDataFaltaSearchRepository, times(1)).save(testDataFalta);
    }

    @Test
    @Transactional
    public void createDataFaltaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dataFaltaRepository.findAll().size();

        // Create the DataFalta with an existing ID
        dataFalta.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataFaltaMockMvc.perform(post("/api/data-faltas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataFalta)))
            .andExpect(status().isBadRequest());

        // Validate the DataFalta in the database
        List<DataFalta> dataFaltaList = dataFaltaRepository.findAll();
        assertThat(dataFaltaList).hasSize(databaseSizeBeforeCreate);

        // Validate the DataFalta in Elasticsearch
        verify(mockDataFaltaSearchRepository, times(0)).save(dataFalta);
    }


    @Test
    @Transactional
    public void getAllDataFaltas() throws Exception {
        // Initialize the database
        dataFaltaRepository.saveAndFlush(dataFalta);

        // Get all the dataFaltaList
        restDataFaltaMockMvc.perform(get("/api/data-faltas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataFalta.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataFalta").value(hasItem(DEFAULT_DATA_FALTA.toString())));
    }
    
    @Test
    @Transactional
    public void getDataFalta() throws Exception {
        // Initialize the database
        dataFaltaRepository.saveAndFlush(dataFalta);

        // Get the dataFalta
        restDataFaltaMockMvc.perform(get("/api/data-faltas/{id}", dataFalta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dataFalta.getId().intValue()))
            .andExpect(jsonPath("$.dataFalta").value(DEFAULT_DATA_FALTA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDataFalta() throws Exception {
        // Get the dataFalta
        restDataFaltaMockMvc.perform(get("/api/data-faltas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDataFalta() throws Exception {
        // Initialize the database
        dataFaltaRepository.saveAndFlush(dataFalta);

        int databaseSizeBeforeUpdate = dataFaltaRepository.findAll().size();

        // Update the dataFalta
        DataFalta updatedDataFalta = dataFaltaRepository.findById(dataFalta.getId()).get();
        // Disconnect from session so that the updates on updatedDataFalta are not directly saved in db
        em.detach(updatedDataFalta);
        updatedDataFalta
            .dataFalta(UPDATED_DATA_FALTA);

        restDataFaltaMockMvc.perform(put("/api/data-faltas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDataFalta)))
            .andExpect(status().isOk());

        // Validate the DataFalta in the database
        List<DataFalta> dataFaltaList = dataFaltaRepository.findAll();
        assertThat(dataFaltaList).hasSize(databaseSizeBeforeUpdate);
        DataFalta testDataFalta = dataFaltaList.get(dataFaltaList.size() - 1);
        assertThat(testDataFalta.getDataFalta()).isEqualTo(UPDATED_DATA_FALTA);

        // Validate the DataFalta in Elasticsearch
        verify(mockDataFaltaSearchRepository, times(1)).save(testDataFalta);
    }

    @Test
    @Transactional
    public void updateNonExistingDataFalta() throws Exception {
        int databaseSizeBeforeUpdate = dataFaltaRepository.findAll().size();

        // Create the DataFalta

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataFaltaMockMvc.perform(put("/api/data-faltas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dataFalta)))
            .andExpect(status().isBadRequest());

        // Validate the DataFalta in the database
        List<DataFalta> dataFaltaList = dataFaltaRepository.findAll();
        assertThat(dataFaltaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DataFalta in Elasticsearch
        verify(mockDataFaltaSearchRepository, times(0)).save(dataFalta);
    }

    @Test
    @Transactional
    public void deleteDataFalta() throws Exception {
        // Initialize the database
        dataFaltaRepository.saveAndFlush(dataFalta);

        int databaseSizeBeforeDelete = dataFaltaRepository.findAll().size();

        // Delete the dataFalta
        restDataFaltaMockMvc.perform(delete("/api/data-faltas/{id}", dataFalta.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DataFalta> dataFaltaList = dataFaltaRepository.findAll();
        assertThat(dataFaltaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DataFalta in Elasticsearch
        verify(mockDataFaltaSearchRepository, times(1)).deleteById(dataFalta.getId());
    }

    @Test
    @Transactional
    public void searchDataFalta() throws Exception {
        // Initialize the database
        dataFaltaRepository.saveAndFlush(dataFalta);
        when(mockDataFaltaSearchRepository.search(queryStringQuery("id:" + dataFalta.getId())))
            .thenReturn(Collections.singletonList(dataFalta));
        // Search the dataFalta
        restDataFaltaMockMvc.perform(get("/api/_search/data-faltas?query=id:" + dataFalta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataFalta.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataFalta").value(hasItem(DEFAULT_DATA_FALTA.toString())));
    }
}
