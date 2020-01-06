package com.cinematica.web.rest;

import com.cinematica.CinematicaApp;
import com.cinematica.domain.Agenda;
import com.cinematica.repository.AgendaRepository;
import com.cinematica.repository.search.AgendaSearchRepository;
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
 * Integration tests for the {@link AgendaResource} REST controller.
 */
@SpringBootTest(classes = CinematicaApp.class)
public class AgendaResourceIT {

    private static final Instant DEFAULT_DATA_INICIO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_INICIO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATA_FIM = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_FIM = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_DIA_TODO = false;
    private static final Boolean UPDATED_DIA_TODO = true;

    private static final Boolean DEFAULT_FALTA = false;
    private static final Boolean UPDATED_FALTA = true;

    private static final Boolean DEFAULT_CANCELOU = false;
    private static final Boolean UPDATED_CANCELOU = true;

    @Autowired
    private AgendaRepository agendaRepository;

    /**
     * This repository is mocked in the com.cinematica.repository.search test package.
     *
     * @see com.cinematica.repository.search.AgendaSearchRepositoryMockConfiguration
     */
    @Autowired
    private AgendaSearchRepository mockAgendaSearchRepository;

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

    private MockMvc restAgendaMockMvc;

    private Agenda agenda;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AgendaResource agendaResource = new AgendaResource(agendaRepository, mockAgendaSearchRepository);
        this.restAgendaMockMvc = MockMvcBuilders.standaloneSetup(agendaResource)
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
    public static Agenda createEntity(EntityManager em) {
        Agenda agenda = new Agenda()
            .dataInicio(DEFAULT_DATA_INICIO)
            .dataFim(DEFAULT_DATA_FIM)
            .diaTodo(DEFAULT_DIA_TODO)
            .falta(DEFAULT_FALTA)
            .cancelou(DEFAULT_CANCELOU);
        return agenda;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agenda createUpdatedEntity(EntityManager em) {
        Agenda agenda = new Agenda()
            .dataInicio(UPDATED_DATA_INICIO)
            .dataFim(UPDATED_DATA_FIM)
            .diaTodo(UPDATED_DIA_TODO)
            .falta(UPDATED_FALTA)
            .cancelou(UPDATED_CANCELOU);
        return agenda;
    }

    @BeforeEach
    public void initTest() {
        agenda = createEntity(em);
    }

    @Test
    @Transactional
    public void createAgenda() throws Exception {
        int databaseSizeBeforeCreate = agendaRepository.findAll().size();

        // Create the Agenda
        restAgendaMockMvc.perform(post("/api/agenda")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agenda)))
            .andExpect(status().isCreated());

        // Validate the Agenda in the database
        List<Agenda> agendaList = agendaRepository.findAll();
        assertThat(agendaList).hasSize(databaseSizeBeforeCreate + 1);
        Agenda testAgenda = agendaList.get(agendaList.size() - 1);
        assertThat(testAgenda.getDataInicio()).isEqualTo(DEFAULT_DATA_INICIO);
        assertThat(testAgenda.getDataFim()).isEqualTo(DEFAULT_DATA_FIM);
        assertThat(testAgenda.isDiaTodo()).isEqualTo(DEFAULT_DIA_TODO);
        assertThat(testAgenda.isFalta()).isEqualTo(DEFAULT_FALTA);
        assertThat(testAgenda.isCancelou()).isEqualTo(DEFAULT_CANCELOU);

        // Validate the Agenda in Elasticsearch
        verify(mockAgendaSearchRepository, times(1)).save(testAgenda);
    }

    @Test
    @Transactional
    public void createAgendaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = agendaRepository.findAll().size();

        // Create the Agenda with an existing ID
        agenda.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgendaMockMvc.perform(post("/api/agenda")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agenda)))
            .andExpect(status().isBadRequest());

        // Validate the Agenda in the database
        List<Agenda> agendaList = agendaRepository.findAll();
        assertThat(agendaList).hasSize(databaseSizeBeforeCreate);

        // Validate the Agenda in Elasticsearch
        verify(mockAgendaSearchRepository, times(0)).save(agenda);
    }


    @Test
    @Transactional
    public void getAllAgenda() throws Exception {
        // Initialize the database
        agendaRepository.saveAndFlush(agenda);

        // Get all the agendaList
        restAgendaMockMvc.perform(get("/api/agenda?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agenda.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataInicio").value(hasItem(DEFAULT_DATA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].dataFim").value(hasItem(DEFAULT_DATA_FIM.toString())))
            .andExpect(jsonPath("$.[*].diaTodo").value(hasItem(DEFAULT_DIA_TODO.booleanValue())))
            .andExpect(jsonPath("$.[*].falta").value(hasItem(DEFAULT_FALTA.booleanValue())))
            .andExpect(jsonPath("$.[*].cancelou").value(hasItem(DEFAULT_CANCELOU.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getAgenda() throws Exception {
        // Initialize the database
        agendaRepository.saveAndFlush(agenda);

        // Get the agenda
        restAgendaMockMvc.perform(get("/api/agenda/{id}", agenda.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(agenda.getId().intValue()))
            .andExpect(jsonPath("$.dataInicio").value(DEFAULT_DATA_INICIO.toString()))
            .andExpect(jsonPath("$.dataFim").value(DEFAULT_DATA_FIM.toString()))
            .andExpect(jsonPath("$.diaTodo").value(DEFAULT_DIA_TODO.booleanValue()))
            .andExpect(jsonPath("$.falta").value(DEFAULT_FALTA.booleanValue()))
            .andExpect(jsonPath("$.cancelou").value(DEFAULT_CANCELOU.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAgenda() throws Exception {
        // Get the agenda
        restAgendaMockMvc.perform(get("/api/agenda/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAgenda() throws Exception {
        // Initialize the database
        agendaRepository.saveAndFlush(agenda);

        int databaseSizeBeforeUpdate = agendaRepository.findAll().size();

        // Update the agenda
        Agenda updatedAgenda = agendaRepository.findById(agenda.getId()).get();
        // Disconnect from session so that the updates on updatedAgenda are not directly saved in db
        em.detach(updatedAgenda);
        updatedAgenda
            .dataInicio(UPDATED_DATA_INICIO)
            .dataFim(UPDATED_DATA_FIM)
            .diaTodo(UPDATED_DIA_TODO)
            .falta(UPDATED_FALTA)
            .cancelou(UPDATED_CANCELOU);

        restAgendaMockMvc.perform(put("/api/agenda")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAgenda)))
            .andExpect(status().isOk());

        // Validate the Agenda in the database
        List<Agenda> agendaList = agendaRepository.findAll();
        assertThat(agendaList).hasSize(databaseSizeBeforeUpdate);
        Agenda testAgenda = agendaList.get(agendaList.size() - 1);
        assertThat(testAgenda.getDataInicio()).isEqualTo(UPDATED_DATA_INICIO);
        assertThat(testAgenda.getDataFim()).isEqualTo(UPDATED_DATA_FIM);
        assertThat(testAgenda.isDiaTodo()).isEqualTo(UPDATED_DIA_TODO);
        assertThat(testAgenda.isFalta()).isEqualTo(UPDATED_FALTA);
        assertThat(testAgenda.isCancelou()).isEqualTo(UPDATED_CANCELOU);

        // Validate the Agenda in Elasticsearch
        verify(mockAgendaSearchRepository, times(1)).save(testAgenda);
    }

    @Test
    @Transactional
    public void updateNonExistingAgenda() throws Exception {
        int databaseSizeBeforeUpdate = agendaRepository.findAll().size();

        // Create the Agenda

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgendaMockMvc.perform(put("/api/agenda")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agenda)))
            .andExpect(status().isBadRequest());

        // Validate the Agenda in the database
        List<Agenda> agendaList = agendaRepository.findAll();
        assertThat(agendaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Agenda in Elasticsearch
        verify(mockAgendaSearchRepository, times(0)).save(agenda);
    }

    @Test
    @Transactional
    public void deleteAgenda() throws Exception {
        // Initialize the database
        agendaRepository.saveAndFlush(agenda);

        int databaseSizeBeforeDelete = agendaRepository.findAll().size();

        // Delete the agenda
        restAgendaMockMvc.perform(delete("/api/agenda/{id}", agenda.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Agenda> agendaList = agendaRepository.findAll();
        assertThat(agendaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Agenda in Elasticsearch
        verify(mockAgendaSearchRepository, times(1)).deleteById(agenda.getId());
    }

    @Test
    @Transactional
    public void searchAgenda() throws Exception {
        // Initialize the database
        agendaRepository.saveAndFlush(agenda);
        when(mockAgendaSearchRepository.search(queryStringQuery("id:" + agenda.getId())))
            .thenReturn(Collections.singletonList(agenda));
        // Search the agenda
        restAgendaMockMvc.perform(get("/api/_search/agenda?query=id:" + agenda.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agenda.getId().intValue())))
            .andExpect(jsonPath("$.[*].dataInicio").value(hasItem(DEFAULT_DATA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].dataFim").value(hasItem(DEFAULT_DATA_FIM.toString())))
            .andExpect(jsonPath("$.[*].diaTodo").value(hasItem(DEFAULT_DIA_TODO.booleanValue())))
            .andExpect(jsonPath("$.[*].falta").value(hasItem(DEFAULT_FALTA.booleanValue())))
            .andExpect(jsonPath("$.[*].cancelou").value(hasItem(DEFAULT_CANCELOU.booleanValue())));
    }
}
