package com.cinematica.web.rest;

import com.cinematica.domain.Medico;
import com.cinematica.repository.MedicoRepository;
import com.cinematica.repository.search.MedicoSearchRepository;
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
 * REST controller for managing {@link com.cinematica.domain.Medico}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MedicoResource {

    private final Logger log = LoggerFactory.getLogger(MedicoResource.class);

    private static final String ENTITY_NAME = "medico";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MedicoRepository medicoRepository;

    private final MedicoSearchRepository medicoSearchRepository;

    public MedicoResource(MedicoRepository medicoRepository, MedicoSearchRepository medicoSearchRepository) {
        this.medicoRepository = medicoRepository;
        this.medicoSearchRepository = medicoSearchRepository;
    }

    /**
     * {@code POST  /medicos} : Create a new medico.
     *
     * @param medico the medico to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new medico, or with status {@code 400 (Bad Request)} if the medico has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/medicos")
    public ResponseEntity<Medico> createMedico(@RequestBody Medico medico) throws URISyntaxException {
        log.debug("REST request to save Medico : {}", medico);
        if (medico.getId() != null) {
            throw new BadRequestAlertException("A new medico cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Medico result = medicoRepository.save(medico);
        medicoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/medicos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /medicos} : Updates an existing medico.
     *
     * @param medico the medico to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medico,
     * or with status {@code 400 (Bad Request)} if the medico is not valid,
     * or with status {@code 500 (Internal Server Error)} if the medico couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/medicos")
    public ResponseEntity<Medico> updateMedico(@RequestBody Medico medico) throws URISyntaxException {
        log.debug("REST request to update Medico : {}", medico);
        if (medico.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Medico result = medicoRepository.save(medico);
        medicoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, medico.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /medicos} : get all the medicos.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of medicos in body.
     */
    @GetMapping("/medicos")
    public List<Medico> getAllMedicos() {
        log.debug("REST request to get all Medicos");
        return medicoRepository.findAll();
    }

    /**
     * {@code GET  /medicos/:id} : get the "id" medico.
     *
     * @param id the id of the medico to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the medico, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/medicos/{id}")
    public ResponseEntity<Medico> getMedico(@PathVariable Long id) {
        log.debug("REST request to get Medico : {}", id);
        Optional<Medico> medico = medicoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(medico);
    }

    /**
     * {@code DELETE  /medicos/:id} : delete the "id" medico.
     *
     * @param id the id of the medico to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/medicos/{id}")
    public ResponseEntity<Void> deleteMedico(@PathVariable Long id) {
        log.debug("REST request to delete Medico : {}", id);
        medicoRepository.deleteById(id);
        medicoSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/medicos?query=:query} : search for the medico corresponding
     * to the query.
     *
     * @param query the query of the medico search.
     * @return the result of the search.
     */
    @GetMapping("/_search/medicos")
    public List<Medico> searchMedicos(@RequestParam String query) {
        log.debug("REST request to search Medicos for query {}", query);
        return StreamSupport
            .stream(medicoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
