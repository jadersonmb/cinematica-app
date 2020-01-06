package com.cinematica.web.rest;

import com.cinematica.domain.ConfiguracaoAgenda;
import com.cinematica.repository.ConfiguracaoAgendaRepository;
import com.cinematica.repository.search.ConfiguracaoAgendaSearchRepository;
import com.cinematica.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional; 
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.cinematica.domain.ConfiguracaoAgenda}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ConfiguracaoAgendaResource {

    private final Logger log = LoggerFactory.getLogger(ConfiguracaoAgendaResource.class);

    private static final String ENTITY_NAME = "configuracaoAgenda";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfiguracaoAgendaRepository configuracaoAgendaRepository;

    private final ConfiguracaoAgendaSearchRepository configuracaoAgendaSearchRepository;

    public ConfiguracaoAgendaResource(ConfiguracaoAgendaRepository configuracaoAgendaRepository, ConfiguracaoAgendaSearchRepository configuracaoAgendaSearchRepository) {
        this.configuracaoAgendaRepository = configuracaoAgendaRepository;
        this.configuracaoAgendaSearchRepository = configuracaoAgendaSearchRepository;
    }

    /**
     * {@code POST  /configuracao-agenda} : Create a new configuracaoAgenda.
     *
     * @param configuracaoAgenda the configuracaoAgenda to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configuracaoAgenda, or with status {@code 400 (Bad Request)} if the configuracaoAgenda has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/configuracao-agenda")
    public ResponseEntity<ConfiguracaoAgenda> createConfiguracaoAgenda(@RequestBody ConfiguracaoAgenda configuracaoAgenda) throws URISyntaxException {
        log.debug("REST request to save ConfiguracaoAgenda : {}", configuracaoAgenda);
        if (configuracaoAgenda.getId() != null) {
            throw new BadRequestAlertException("A new configuracaoAgenda cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConfiguracaoAgenda result = configuracaoAgendaRepository.save(configuracaoAgenda);
        configuracaoAgendaSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/configuracao-agenda/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /configuracao-agenda} : Updates an existing configuracaoAgenda.
     *
     * @param configuracaoAgenda the configuracaoAgenda to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configuracaoAgenda,
     * or with status {@code 400 (Bad Request)} if the configuracaoAgenda is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configuracaoAgenda couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/configuracao-agenda")
    public ResponseEntity<ConfiguracaoAgenda> updateConfiguracaoAgenda(@RequestBody ConfiguracaoAgenda configuracaoAgenda) throws URISyntaxException {
        log.debug("REST request to update ConfiguracaoAgenda : {}", configuracaoAgenda);
        if (configuracaoAgenda.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ConfiguracaoAgenda result = configuracaoAgendaRepository.save(configuracaoAgenda);
        configuracaoAgendaSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configuracaoAgenda.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /configuracao-agenda} : get all the configuracaoAgenda.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configuracaoAgenda in body.
     */
    @GetMapping("/configuracao-agenda")
    public List<ConfiguracaoAgenda> getAllConfiguracaoAgenda() {
        log.debug("REST request to get all ConfiguracaoAgenda");
        return configuracaoAgendaRepository.findAll();
    }

    /**
     * {@code GET  /configuracao-agenda/:id} : get the "id" configuracaoAgenda.
     *
     * @param id the id of the configuracaoAgenda to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configuracaoAgenda, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/configuracao-agenda/{id}")
    public ResponseEntity<ConfiguracaoAgenda> getConfiguracaoAgenda(@PathVariable Long id) {
        log.debug("REST request to get ConfiguracaoAgenda : {}", id);
        Optional<ConfiguracaoAgenda> configuracaoAgenda = configuracaoAgendaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(configuracaoAgenda);
    }

    /**
     * {@code DELETE  /configuracao-agenda/:id} : delete the "id" configuracaoAgenda.
     *
     * @param id the id of the configuracaoAgenda to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/configuracao-agenda/{id}")
    public ResponseEntity<Void> deleteConfiguracaoAgenda(@PathVariable Long id) {
        log.debug("REST request to delete ConfiguracaoAgenda : {}", id);
        configuracaoAgendaRepository.deleteById(id);
        configuracaoAgendaSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/configuracao-agenda?query=:query} : search for the configuracaoAgenda corresponding
     * to the query.
     *
     * @param query the query of the configuracaoAgenda search.
     * @return the result of the search.
     */
    @GetMapping("/_search/configuracao-agenda")
    public List<ConfiguracaoAgenda> searchConfiguracaoAgenda(@RequestParam String query) {
        log.debug("REST request to search ConfiguracaoAgenda for query {}", query);
        return StreamSupport
            .stream(configuracaoAgendaSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
