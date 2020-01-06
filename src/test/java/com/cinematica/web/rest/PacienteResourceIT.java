package com.cinematica.web.rest;

import com.cinematica.CinematicaApp;
import com.cinematica.domain.Paciente;
import com.cinematica.repository.PacienteRepository;
import com.cinematica.repository.search.PacienteSearchRepository;
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

import com.cinematica.domain.enumeration.Sexo;
/**
 * Integration tests for the {@link PacienteResource} REST controller.
 */
@SpringBootTest(classes = CinematicaApp.class)
public class PacienteResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_NOME_COMPLETO = "AAAAAAAAAA";
    private static final String UPDATED_NOME_COMPLETO = "BBBBBBBBBB";

    private static final Instant DEFAULT_CRIADO_EM = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CRIADO_EM = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ATUALIZADO_EM = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ATUALIZADO_EM = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CPF = "AAAAAAAAAA";
    private static final String UPDATED_CPF = "BBBBBBBBBB";

    private static final String DEFAULT_RG = "AAAAAAAAAA";
    private static final String UPDATED_RG = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONE_CELULAR = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONE_CELULAR = "BBBBBBBBBB";

    private static final String DEFAULT_FOTO_URL = "AAAAAAAAAA";
    private static final String UPDATED_FOTO_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_FUNCIONARIO = false;
    private static final Boolean UPDATED_FUNCIONARIO = true;

    private static final Instant DEFAULT_DATA_NASCIMENTO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_NASCIMENTO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREFITO = "AAAAAAAAAA";
    private static final String UPDATED_CREFITO = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONE = "BBBBBBBBBB";

    private static final String DEFAULT_FOTO_URL_ENDERECO = "AAAAAAAAAA";
    private static final String UPDATED_FOTO_URL_ENDERECO = "BBBBBBBBBB";

    private static final String DEFAULT_INDICACAO = "AAAAAAAAAA";
    private static final String UPDATED_INDICACAO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    private static final Sexo DEFAULT_SEXO = Sexo.Masculino;
    private static final Sexo UPDATED_SEXO = Sexo.Feminino;

    @Autowired
    private PacienteRepository pacienteRepository;

    /**
     * This repository is mocked in the com.cinematica.repository.search test package.
     *
     * @see com.cinematica.repository.search.PacienteSearchRepositoryMockConfiguration
     */
    @Autowired
    private PacienteSearchRepository mockPacienteSearchRepository;

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

    private MockMvc restPacienteMockMvc;

    private Paciente paciente;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PacienteResource pacienteResource = new PacienteResource(pacienteRepository, mockPacienteSearchRepository);
        this.restPacienteMockMvc = MockMvcBuilders.standaloneSetup(pacienteResource)
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
    public static Paciente createEntity(EntityManager em) {
        Paciente paciente = new Paciente()
            .nome(DEFAULT_NOME)
            .nomeCompleto(DEFAULT_NOME_COMPLETO)
            .criadoEm(DEFAULT_CRIADO_EM)
            .atualizadoEm(DEFAULT_ATUALIZADO_EM)
            .cpf(DEFAULT_CPF)
            .rg(DEFAULT_RG)
            .email(DEFAULT_EMAIL)
            .telefoneCelular(DEFAULT_TELEFONE_CELULAR)
            .fotoUrl(DEFAULT_FOTO_URL)
            .funcionario(DEFAULT_FUNCIONARIO)
            .dataNascimento(DEFAULT_DATA_NASCIMENTO)
            .crefito(DEFAULT_CREFITO)
            .telefone(DEFAULT_TELEFONE)
            .fotoUrlEndereco(DEFAULT_FOTO_URL_ENDERECO)
            .indicacao(DEFAULT_INDICACAO)
            .ativo(DEFAULT_ATIVO)
            .sexo(DEFAULT_SEXO);
        return paciente;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paciente createUpdatedEntity(EntityManager em) {
        Paciente paciente = new Paciente()
            .nome(UPDATED_NOME)
            .nomeCompleto(UPDATED_NOME_COMPLETO)
            .criadoEm(UPDATED_CRIADO_EM)
            .atualizadoEm(UPDATED_ATUALIZADO_EM)
            .cpf(UPDATED_CPF)
            .rg(UPDATED_RG)
            .email(UPDATED_EMAIL)
            .telefoneCelular(UPDATED_TELEFONE_CELULAR)
            .fotoUrl(UPDATED_FOTO_URL)
            .funcionario(UPDATED_FUNCIONARIO)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .crefito(UPDATED_CREFITO)
            .telefone(UPDATED_TELEFONE)
            .fotoUrlEndereco(UPDATED_FOTO_URL_ENDERECO)
            .indicacao(UPDATED_INDICACAO)
            .ativo(UPDATED_ATIVO)
            .sexo(UPDATED_SEXO);
        return paciente;
    }

    @BeforeEach
    public void initTest() {
        paciente = createEntity(em);
    }

    @Test
    @Transactional
    public void createPaciente() throws Exception {
        int databaseSizeBeforeCreate = pacienteRepository.findAll().size();

        // Create the Paciente
        restPacienteMockMvc.perform(post("/api/pacientes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paciente)))
            .andExpect(status().isCreated());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeCreate + 1);
        Paciente testPaciente = pacienteList.get(pacienteList.size() - 1);
        assertThat(testPaciente.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testPaciente.getNomeCompleto()).isEqualTo(DEFAULT_NOME_COMPLETO);
        assertThat(testPaciente.getCriadoEm()).isEqualTo(DEFAULT_CRIADO_EM);
        assertThat(testPaciente.getAtualizadoEm()).isEqualTo(DEFAULT_ATUALIZADO_EM);
        assertThat(testPaciente.getCpf()).isEqualTo(DEFAULT_CPF);
        assertThat(testPaciente.getRg()).isEqualTo(DEFAULT_RG);
        assertThat(testPaciente.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPaciente.getTelefoneCelular()).isEqualTo(DEFAULT_TELEFONE_CELULAR);
        assertThat(testPaciente.getFotoUrl()).isEqualTo(DEFAULT_FOTO_URL);
        assertThat(testPaciente.isFuncionario()).isEqualTo(DEFAULT_FUNCIONARIO);
        assertThat(testPaciente.getDataNascimento()).isEqualTo(DEFAULT_DATA_NASCIMENTO);
        assertThat(testPaciente.getCrefito()).isEqualTo(DEFAULT_CREFITO);
        assertThat(testPaciente.getTelefone()).isEqualTo(DEFAULT_TELEFONE);
        assertThat(testPaciente.getFotoUrlEndereco()).isEqualTo(DEFAULT_FOTO_URL_ENDERECO);
        assertThat(testPaciente.getIndicacao()).isEqualTo(DEFAULT_INDICACAO);
        assertThat(testPaciente.isAtivo()).isEqualTo(DEFAULT_ATIVO);
        assertThat(testPaciente.getSexo()).isEqualTo(DEFAULT_SEXO);

        // Validate the Paciente in Elasticsearch
        verify(mockPacienteSearchRepository, times(1)).save(testPaciente);
    }

    @Test
    @Transactional
    public void createPacienteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pacienteRepository.findAll().size();

        // Create the Paciente with an existing ID
        paciente.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPacienteMockMvc.perform(post("/api/pacientes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paciente)))
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeCreate);

        // Validate the Paciente in Elasticsearch
        verify(mockPacienteSearchRepository, times(0)).save(paciente);
    }


    @Test
    @Transactional
    public void getAllPacientes() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get all the pacienteList
        restPacienteMockMvc.perform(get("/api/pacientes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paciente.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].nomeCompleto").value(hasItem(DEFAULT_NOME_COMPLETO)))
            .andExpect(jsonPath("$.[*].criadoEm").value(hasItem(DEFAULT_CRIADO_EM.toString())))
            .andExpect(jsonPath("$.[*].atualizadoEm").value(hasItem(DEFAULT_ATUALIZADO_EM.toString())))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)))
            .andExpect(jsonPath("$.[*].rg").value(hasItem(DEFAULT_RG)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefoneCelular").value(hasItem(DEFAULT_TELEFONE_CELULAR)))
            .andExpect(jsonPath("$.[*].fotoUrl").value(hasItem(DEFAULT_FOTO_URL)))
            .andExpect(jsonPath("$.[*].funcionario").value(hasItem(DEFAULT_FUNCIONARIO.booleanValue())))
            .andExpect(jsonPath("$.[*].dataNascimento").value(hasItem(DEFAULT_DATA_NASCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].crefito").value(hasItem(DEFAULT_CREFITO)))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)))
            .andExpect(jsonPath("$.[*].fotoUrlEndereco").value(hasItem(DEFAULT_FOTO_URL_ENDERECO)))
            .andExpect(jsonPath("$.[*].indicacao").value(hasItem(DEFAULT_INDICACAO)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())))
            .andExpect(jsonPath("$.[*].sexo").value(hasItem(DEFAULT_SEXO.toString())));
    }
    
    @Test
    @Transactional
    public void getPaciente() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        // Get the paciente
        restPacienteMockMvc.perform(get("/api/pacientes/{id}", paciente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(paciente.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.nomeCompleto").value(DEFAULT_NOME_COMPLETO))
            .andExpect(jsonPath("$.criadoEm").value(DEFAULT_CRIADO_EM.toString()))
            .andExpect(jsonPath("$.atualizadoEm").value(DEFAULT_ATUALIZADO_EM.toString()))
            .andExpect(jsonPath("$.cpf").value(DEFAULT_CPF))
            .andExpect(jsonPath("$.rg").value(DEFAULT_RG))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telefoneCelular").value(DEFAULT_TELEFONE_CELULAR))
            .andExpect(jsonPath("$.fotoUrl").value(DEFAULT_FOTO_URL))
            .andExpect(jsonPath("$.funcionario").value(DEFAULT_FUNCIONARIO.booleanValue()))
            .andExpect(jsonPath("$.dataNascimento").value(DEFAULT_DATA_NASCIMENTO.toString()))
            .andExpect(jsonPath("$.crefito").value(DEFAULT_CREFITO))
            .andExpect(jsonPath("$.telefone").value(DEFAULT_TELEFONE))
            .andExpect(jsonPath("$.fotoUrlEndereco").value(DEFAULT_FOTO_URL_ENDERECO))
            .andExpect(jsonPath("$.indicacao").value(DEFAULT_INDICACAO))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO.booleanValue()))
            .andExpect(jsonPath("$.sexo").value(DEFAULT_SEXO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPaciente() throws Exception {
        // Get the paciente
        restPacienteMockMvc.perform(get("/api/pacientes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePaciente() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        int databaseSizeBeforeUpdate = pacienteRepository.findAll().size();

        // Update the paciente
        Paciente updatedPaciente = pacienteRepository.findById(paciente.getId()).get();
        // Disconnect from session so that the updates on updatedPaciente are not directly saved in db
        em.detach(updatedPaciente);
        updatedPaciente
            .nome(UPDATED_NOME)
            .nomeCompleto(UPDATED_NOME_COMPLETO)
            .criadoEm(UPDATED_CRIADO_EM)
            .atualizadoEm(UPDATED_ATUALIZADO_EM)
            .cpf(UPDATED_CPF)
            .rg(UPDATED_RG)
            .email(UPDATED_EMAIL)
            .telefoneCelular(UPDATED_TELEFONE_CELULAR)
            .fotoUrl(UPDATED_FOTO_URL)
            .funcionario(UPDATED_FUNCIONARIO)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .crefito(UPDATED_CREFITO)
            .telefone(UPDATED_TELEFONE)
            .fotoUrlEndereco(UPDATED_FOTO_URL_ENDERECO)
            .indicacao(UPDATED_INDICACAO)
            .ativo(UPDATED_ATIVO)
            .sexo(UPDATED_SEXO);

        restPacienteMockMvc.perform(put("/api/pacientes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPaciente)))
            .andExpect(status().isOk());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);
        Paciente testPaciente = pacienteList.get(pacienteList.size() - 1);
        assertThat(testPaciente.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testPaciente.getNomeCompleto()).isEqualTo(UPDATED_NOME_COMPLETO);
        assertThat(testPaciente.getCriadoEm()).isEqualTo(UPDATED_CRIADO_EM);
        assertThat(testPaciente.getAtualizadoEm()).isEqualTo(UPDATED_ATUALIZADO_EM);
        assertThat(testPaciente.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testPaciente.getRg()).isEqualTo(UPDATED_RG);
        assertThat(testPaciente.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPaciente.getTelefoneCelular()).isEqualTo(UPDATED_TELEFONE_CELULAR);
        assertThat(testPaciente.getFotoUrl()).isEqualTo(UPDATED_FOTO_URL);
        assertThat(testPaciente.isFuncionario()).isEqualTo(UPDATED_FUNCIONARIO);
        assertThat(testPaciente.getDataNascimento()).isEqualTo(UPDATED_DATA_NASCIMENTO);
        assertThat(testPaciente.getCrefito()).isEqualTo(UPDATED_CREFITO);
        assertThat(testPaciente.getTelefone()).isEqualTo(UPDATED_TELEFONE);
        assertThat(testPaciente.getFotoUrlEndereco()).isEqualTo(UPDATED_FOTO_URL_ENDERECO);
        assertThat(testPaciente.getIndicacao()).isEqualTo(UPDATED_INDICACAO);
        assertThat(testPaciente.isAtivo()).isEqualTo(UPDATED_ATIVO);
        assertThat(testPaciente.getSexo()).isEqualTo(UPDATED_SEXO);

        // Validate the Paciente in Elasticsearch
        verify(mockPacienteSearchRepository, times(1)).save(testPaciente);
    }

    @Test
    @Transactional
    public void updateNonExistingPaciente() throws Exception {
        int databaseSizeBeforeUpdate = pacienteRepository.findAll().size();

        // Create the Paciente

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPacienteMockMvc.perform(put("/api/pacientes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paciente)))
            .andExpect(status().isBadRequest());

        // Validate the Paciente in the database
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Paciente in Elasticsearch
        verify(mockPacienteSearchRepository, times(0)).save(paciente);
    }

    @Test
    @Transactional
    public void deletePaciente() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);

        int databaseSizeBeforeDelete = pacienteRepository.findAll().size();

        // Delete the paciente
        restPacienteMockMvc.perform(delete("/api/pacientes/{id}", paciente.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Paciente> pacienteList = pacienteRepository.findAll();
        assertThat(pacienteList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Paciente in Elasticsearch
        verify(mockPacienteSearchRepository, times(1)).deleteById(paciente.getId());
    }

    @Test
    @Transactional
    public void searchPaciente() throws Exception {
        // Initialize the database
        pacienteRepository.saveAndFlush(paciente);
        when(mockPacienteSearchRepository.search(queryStringQuery("id:" + paciente.getId())))
            .thenReturn(Collections.singletonList(paciente));
        // Search the paciente
        restPacienteMockMvc.perform(get("/api/_search/pacientes?query=id:" + paciente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paciente.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].nomeCompleto").value(hasItem(DEFAULT_NOME_COMPLETO)))
            .andExpect(jsonPath("$.[*].criadoEm").value(hasItem(DEFAULT_CRIADO_EM.toString())))
            .andExpect(jsonPath("$.[*].atualizadoEm").value(hasItem(DEFAULT_ATUALIZADO_EM.toString())))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)))
            .andExpect(jsonPath("$.[*].rg").value(hasItem(DEFAULT_RG)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telefoneCelular").value(hasItem(DEFAULT_TELEFONE_CELULAR)))
            .andExpect(jsonPath("$.[*].fotoUrl").value(hasItem(DEFAULT_FOTO_URL)))
            .andExpect(jsonPath("$.[*].funcionario").value(hasItem(DEFAULT_FUNCIONARIO.booleanValue())))
            .andExpect(jsonPath("$.[*].dataNascimento").value(hasItem(DEFAULT_DATA_NASCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].crefito").value(hasItem(DEFAULT_CREFITO)))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)))
            .andExpect(jsonPath("$.[*].fotoUrlEndereco").value(hasItem(DEFAULT_FOTO_URL_ENDERECO)))
            .andExpect(jsonPath("$.[*].indicacao").value(hasItem(DEFAULT_INDICACAO)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())))
            .andExpect(jsonPath("$.[*].sexo").value(hasItem(DEFAULT_SEXO.toString())));
    }
}
