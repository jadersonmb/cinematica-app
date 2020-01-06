package com.cinematica.web.rest;

import com.cinematica.domain.Especialidade;
import com.cinematica.repository.EspecialidadeRepository;
import com.cinematica.repository.search.EspecialidadeSearchRepository;
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
 * REST controller for managing {@link com.cinematica.domain.Especialidade}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EspecialidadeResource {

    private final Logger log = LoggerFactory.getLogger(EspecialidadeResource.class);

    private static final String ENTITY_NAME = "especialidade";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EspecialidadeRepository especialidadeRepository;

    private final EspecialidadeSearchRepository especialidadeSearchRepository;

    public EspecialidadeResource(EspecialidadeRepository especialidadeRepository, EspecialidadeSearchRepository especialidadeSearchRepository) {
        this.especialidadeRepository = especialidadeRepository;
        this.especialidadeSearchRepository = especialidadeSearchRepository;
    }

    /**
     * {@code POST  /especialidades} : Create a new especialidade.
     *
     * @param especialidade the especialidade to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new especialidade, or with status {@code 400 (Bad Request)} if the especialidade has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/especialidades")
    public ResponseEntity<Especialidade> createEspecialidade(@RequestBody Especialidade especialidade) throws URISyntaxException {
        log.debug("REST request to save Especialidade : {}", especialidade);
        if (especialidade.getId() != null) {
            throw new BadRequestAlertException("A new especialidade cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Especialidade result = especialidadeRepository.save(especialidade);
        especialidadeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/especialidades/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /especialidades} : Updates an existing especialidade.
     *
     * @param especialidade the especialidade to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated especialidade,
     * or with status {@code 400 (Bad Request)} if the especialidade is not valid,
     * or with status {@code 500 (Internal Server Error)} if the especialidade couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/especialidades")
    public ResponseEntity<Especialidade> updateEspecialidade(@RequestBody Especialidade especialidade) throws URISyntaxException {
        log.debug("REST request to update Especialidade : {}", especialidade);
        if (especialidade.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Especialidade result = especialidadeRepository.save(especialidade);
        especialidadeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, especialidade.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /especialidades} : get all the especialidades.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of especialidades in body.
     */
    @GetMapping("/especialidades")
    public List<Especialidade> getAllEspecialidades() {
        log.debug("REST request to get all Especialidades");
        return especialidadeRepository.findAll();
    }

    /**
     * {@code GET  /especialidades/:id} : get the "id" especialidade.
     *
     * @param id the id of the especialidade to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the especialidade, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/especialidades/{id}")
    public ResponseEntity<Especialidade> getEspecialidade(@PathVariable Long id) {
        log.debug("REST request to get Especialidade : {}", id);
        Optional<Especialidade> especialidade = especialidadeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(especialidade);
    }

    /**
     * {@code DELETE  /especialidades/:id} : delete the "id" especialidade.
     *
     * @param id the id of the especialidade to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/especialidades/{id}")
    public ResponseEntity<Void> deleteEspecialidade(@PathVariable Long id) {
        log.debug("REST request to delete Especialidade : {}", id);
        especialidadeRepository.deleteById(id);
        especialidadeSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/especialidades?query=:query} : search for the especialidade corresponding
     * to the query.
     *
     * @param query the query of the especialidade search.
     * @return the result of the search.
     */
    @GetMapping("/_search/especialidades")
    public List<Especialidade> searchEspecialidades(@RequestParam String query) {
        log.debug("REST request to search Especialidades for query {}", query);
        return StreamSupport
            .stream(especialidadeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
