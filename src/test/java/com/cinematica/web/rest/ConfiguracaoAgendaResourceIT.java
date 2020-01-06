package com.cinematica.web.rest;

import com.cinematica.CinematicaApp;
import com.cinematica.domain.ConfiguracaoAgenda;
import com.cinematica.repository.ConfiguracaoAgendaRepository;
import com.cinematica.repository.search.ConfiguracaoAgendaSearchRepository;
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
 * Integration tests for the {@link ConfiguracaoAgendaResource} REST controller.
 */
@SpringBootTest(classes = CinematicaApp.class)
public class ConfiguracaoAgendaResourceIT {

    private static final Boolean DEFAULT_SEGUNDA = false;
    private static final Boolean UPDATED_SEGUNDA = true;

    private static final Boolean DEFAULT_TERCA = false;
    private static final Boolean UPDATED_TERCA = true;

    private static final Boolean DEFAULT_QUARTA = false;
    private static final Boolean UPDATED_QUARTA = true;

    private static final Boolean DEFAULT_QUINTA = false;
    private static final Boolean UPDATED_QUINTA = true;

    private static final Boolean DEFAULT_SEXTA = false;
    private static final Boolean UPDATED_SEXTA = true;

    private static final Boolean DEFAULT_SABADO = false;
    private static final Boolean UPDATED_SABADO = true;

    private static final Boolean DEFAULT_DOMINGO = false;
    private static final Boolean UPDATED_DOMINGO = true;

    @Autowired
    private ConfiguracaoAgendaRepository configuracaoAgendaRepository;

    /**
     * This repository is mocked in the com.cinematica.repository.search test package.
     *
     * @see com.cinematica.repository.search.ConfiguracaoAgendaSearchRepositoryMockConfiguration
     */
    @Autowired
    private ConfiguracaoAgendaSearchRepository mockConfiguracaoAgendaSearchRepository;

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

    private MockMvc restConfiguracaoAgendaMockMvc;

    private ConfiguracaoAgenda configuracaoAgenda;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConfiguracaoAgendaResource configuracaoAgendaResource = new ConfiguracaoAgendaResource(configuracaoAgendaRepository, mockConfiguracaoAgendaSearchRepository);
        this.restConfiguracaoAgendaMockMvc = MockMvcBuilders.standaloneSetup(configuracaoAgendaResource)
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
    public static ConfiguracaoAgenda createEntity(EntityManager em) {
        ConfiguracaoAgenda configuracaoAgenda = new ConfiguracaoAgenda()
            .segunda(DEFAULT_SEGUNDA)
            .terca(DEFAULT_TERCA)
            .quarta(DEFAULT_QUARTA)
            .quinta(DEFAULT_QUINTA)
            .sexta(DEFAULT_SEXTA)
            .sabado(DEFAULT_SABADO)
            .domingo(DEFAULT_DOMINGO);
        return configuracaoAgenda;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfiguracaoAgenda createUpdatedEntity(EntityManager em) {
        ConfiguracaoAgenda configuracaoAgenda = new ConfiguracaoAgenda()
            .segunda(UPDATED_SEGUNDA)
            .terca(UPDATED_TERCA)
            .quarta(UPDATED_QUARTA)
            .quinta(UPDATED_QUINTA)
            .sexta(UPDATED_SEXTA)
            .sabado(UPDATED_SABADO)
            .domingo(UPDATED_DOMINGO);
        return configuracaoAgenda;
    }

    @BeforeEach
    public void initTest() {
        configuracaoAgenda = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfiguracaoAgenda() throws Exception {
        int databaseSizeBeforeCreate = configuracaoAgendaRepository.findAll().size();

        // Create the ConfiguracaoAgenda
        restConfiguracaoAgendaMockMvc.perform(post("/api/configuracao-agenda")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configuracaoAgenda)))
            .andExpect(status().isCreated());

        // Validate the ConfiguracaoAgenda in the database
        List<ConfiguracaoAgenda> configuracaoAgendaList = configuracaoAgendaRepository.findAll();
        assertThat(configuracaoAgendaList).hasSize(databaseSizeBeforeCreate + 1);
        ConfiguracaoAgenda testConfiguracaoAgenda = configuracaoAgendaList.get(configuracaoAgendaList.size() - 1);
        assertThat(testConfiguracaoAgenda.isSegunda()).isEqualTo(DEFAULT_SEGUNDA);
        assertThat(testConfiguracaoAgenda.isTerca()).isEqualTo(DEFAULT_TERCA);
        assertThat(testConfiguracaoAgenda.isQuarta()).isEqualTo(DEFAULT_QUARTA);
        assertThat(testConfiguracaoAgenda.isQuinta()).isEqualTo(DEFAULT_QUINTA);
        assertThat(testConfiguracaoAgenda.isSexta()).isEqualTo(DEFAULT_SEXTA);
        assertThat(testConfiguracaoAgenda.isSabado()).isEqualTo(DEFAULT_SABADO);
        assertThat(testConfiguracaoAgenda.isDomingo()).isEqualTo(DEFAULT_DOMINGO);

        // Validate the ConfiguracaoAgenda in Elasticsearch
        verify(mockConfiguracaoAgendaSearchRepository, times(1)).save(testConfiguracaoAgenda);
    }

    @Test
    @Transactional
    public void createConfiguracaoAgendaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = configuracaoAgendaRepository.findAll().size();

        // Create the ConfiguracaoAgenda with an existing ID
        configuracaoAgenda.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfiguracaoAgendaMockMvc.perform(post("/api/configuracao-agenda")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configuracaoAgenda)))
            .andExpect(status().isBadRequest());

        // Validate the ConfiguracaoAgenda in the database
        List<ConfiguracaoAgenda> configuracaoAgendaList = configuracaoAgendaRepository.findAll();
        assertThat(configuracaoAgendaList).hasSize(databaseSizeBeforeCreate);

        // Validate the ConfiguracaoAgenda in Elasticsearch
        verify(mockConfiguracaoAgendaSearchRepository, times(0)).save(configuracaoAgenda);
    }


    @Test
    @Transactional
    public void getAllConfiguracaoAgenda() throws Exception {
        // Initialize the database
        configuracaoAgendaRepository.saveAndFlush(configuracaoAgenda);

        // Get all the configuracaoAgendaList
        restConfiguracaoAgendaMockMvc.perform(get("/api/configuracao-agenda?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configuracaoAgenda.getId().intValue())))
            .andExpect(jsonPath("$.[*].segunda").value(hasItem(DEFAULT_SEGUNDA.booleanValue())))
            .andExpect(jsonPath("$.[*].terca").value(hasItem(DEFAULT_TERCA.booleanValue())))
            .andExpect(jsonPath("$.[*].quarta").value(hasItem(DEFAULT_QUARTA.booleanValue())))
            .andExpect(jsonPath("$.[*].quinta").value(hasItem(DEFAULT_QUINTA.booleanValue())))
            .andExpect(jsonPath("$.[*].sexta").value(hasItem(DEFAULT_SEXTA.booleanValue())))
            .andExpect(jsonPath("$.[*].sabado").value(hasItem(DEFAULT_SABADO.booleanValue())))
            .andExpect(jsonPath("$.[*].domingo").value(hasItem(DEFAULT_DOMINGO.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getConfiguracaoAgenda() throws Exception {
        // Initialize the database
        configuracaoAgendaRepository.saveAndFlush(configuracaoAgenda);

        // Get the configuracaoAgenda
        restConfiguracaoAgendaMockMvc.perform(get("/api/configuracao-agenda/{id}", configuracaoAgenda.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(configuracaoAgenda.getId().intValue()))
            .andExpect(jsonPath("$.segunda").value(DEFAULT_SEGUNDA.booleanValue()))
            .andExpect(jsonPath("$.terca").value(DEFAULT_TERCA.booleanValue()))
            .andExpect(jsonPath("$.quarta").value(DEFAULT_QUARTA.booleanValue()))
            .andExpect(jsonPath("$.quinta").value(DEFAULT_QUINTA.booleanValue()))
            .andExpect(jsonPath("$.sexta").value(DEFAULT_SEXTA.booleanValue()))
            .andExpect(jsonPath("$.sabado").value(DEFAULT_SABADO.booleanValue()))
            .andExpect(jsonPath("$.domingo").value(DEFAULT_DOMINGO.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingConfiguracaoAgenda() throws Exception {
        // Get the configuracaoAgenda
        restConfiguracaoAgendaMockMvc.perform(get("/api/configuracao-agenda/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfiguracaoAgenda() throws Exception {
        // Initialize the database
        configuracaoAgendaRepository.saveAndFlush(configuracaoAgenda);

        int databaseSizeBeforeUpdate = configuracaoAgendaRepository.findAll().size();

        // Update the configuracaoAgenda
        ConfiguracaoAgenda updatedConfiguracaoAgenda = configuracaoAgendaRepository.findById(configuracaoAgenda.getId()).get();
        // Disconnect from session so that the updates on updatedConfiguracaoAgenda are not directly saved in db
        em.detach(updatedConfiguracaoAgenda);
        updatedConfiguracaoAgenda
            .segunda(UPDATED_SEGUNDA)
            .terca(UPDATED_TERCA)
            .quarta(UPDATED_QUARTA)
            .quinta(UPDATED_QUINTA)
            .sexta(UPDATED_SEXTA)
            .sabado(UPDATED_SABADO)
            .domingo(UPDATED_DOMINGO);

        restConfiguracaoAgendaMockMvc.perform(put("/api/configuracao-agenda")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedConfiguracaoAgenda)))
            .andExpect(status().isOk());

        // Validate the ConfiguracaoAgenda in the database
        List<ConfiguracaoAgenda> configuracaoAgendaList = configuracaoAgendaRepository.findAll();
        assertThat(configuracaoAgendaList).hasSize(databaseSizeBeforeUpdate);
        ConfiguracaoAgenda testConfiguracaoAgenda = configuracaoAgendaList.get(configuracaoAgendaList.size() - 1);
        assertThat(testConfiguracaoAgenda.isSegunda()).isEqualTo(UPDATED_SEGUNDA);
        assertThat(testConfiguracaoAgenda.isTerca()).isEqualTo(UPDATED_TERCA);
        assertThat(testConfiguracaoAgenda.isQuarta()).isEqualTo(UPDATED_QUARTA);
        assertThat(testConfiguracaoAgenda.isQuinta()).isEqualTo(UPDATED_QUINTA);
        assertThat(testConfiguracaoAgenda.isSexta()).isEqualTo(UPDATED_SEXTA);
        assertThat(testConfiguracaoAgenda.isSabado()).isEqualTo(UPDATED_SABADO);
        assertThat(testConfiguracaoAgenda.isDomingo()).isEqualTo(UPDATED_DOMINGO);

        // Validate the ConfiguracaoAgenda in Elasticsearch
        verify(mockConfiguracaoAgendaSearchRepository, times(1)).save(testConfiguracaoAgenda);
    }

    @Test
    @Transactional
    public void updateNonExistingConfiguracaoAgenda() throws Exception {
        int databaseSizeBeforeUpdate = configuracaoAgendaRepository.findAll().size();

        // Create the ConfiguracaoAgenda

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfiguracaoAgendaMockMvc.perform(put("/api/configuracao-agenda")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configuracaoAgenda)))
            .andExpect(status().isBadRequest());

        // Validate the ConfiguracaoAgenda in the database
        List<ConfiguracaoAgenda> configuracaoAgendaList = configuracaoAgendaRepository.findAll();
        assertThat(configuracaoAgendaList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ConfiguracaoAgenda in Elasticsearch
        verify(mockConfiguracaoAgendaSearchRepository, times(0)).save(configuracaoAgenda);
    }

    @Test
    @Transactional
    public void deleteConfiguracaoAgenda() throws Exception {
        // Initialize the database
        configuracaoAgendaRepository.saveAndFlush(configuracaoAgenda);

        int databaseSizeBeforeDelete = configuracaoAgendaRepository.findAll().size();

        // Delete the configuracaoAgenda
        restConfiguracaoAgendaMockMvc.perform(delete("/api/configuracao-agenda/{id}", configuracaoAgenda.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConfiguracaoAgenda> configuracaoAgendaList = configuracaoAgendaRepository.findAll();
        assertThat(configuracaoAgendaList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ConfiguracaoAgenda in Elasticsearch
        verify(mockConfiguracaoAgendaSearchRepository, times(1)).deleteById(configuracaoAgenda.getId());
    }

    @Test
    @Transactional
    public void searchConfiguracaoAgenda() throws Exception {
        // Initialize the database
        configuracaoAgendaRepository.saveAndFlush(configuracaoAgenda);
        when(mockConfiguracaoAgendaSearchRepository.search(queryStringQuery("id:" + configuracaoAgenda.getId())))
            .thenReturn(Collections.singletonList(configuracaoAgenda));
        // Search the configuracaoAgenda
        restConfiguracaoAgendaMockMvc.perform(get("/api/_search/configuracao-agenda?query=id:" + configuracaoAgenda.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configuracaoAgenda.getId().intValue())))
            .andExpect(jsonPath("$.[*].segunda").value(hasItem(DEFAULT_SEGUNDA.booleanValue())))
            .andExpect(jsonPath("$.[*].terca").value(hasItem(DEFAULT_TERCA.booleanValue())))
            .andExpect(jsonPath("$.[*].quarta").value(hasItem(DEFAULT_QUARTA.booleanValue())))
            .andExpect(jsonPath("$.[*].quinta").value(hasItem(DEFAULT_QUINTA.booleanValue())))
            .andExpect(jsonPath("$.[*].sexta").value(hasItem(DEFAULT_SEXTA.booleanValue())))
            .andExpect(jsonPath("$.[*].sabado").value(hasItem(DEFAULT_SABADO.booleanValue())))
            .andExpect(jsonPath("$.[*].domingo").value(hasItem(DEFAULT_DOMINGO.booleanValue())));
    }
}
