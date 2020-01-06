package com.cinematica.web.rest;

import com.cinematica.domain.HorarioDisponivel;
import com.cinematica.repository.HorarioDisponivelRepository;
import com.cinematica.repository.search.HorarioDisponivelSearchRepository;
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
 * REST controller for managing {@link com.cinematica.domain.HorarioDisponivel}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class HorarioDisponivelResource {

    private final Logger log = LoggerFactory.getLogger(HorarioDisponivelResource.class);

    private static final String ENTITY_NAME = "horarioDisponivel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HorarioDisponivelRepository horarioDisponivelRepository;

    private final HorarioDisponivelSearchRepository horarioDisponivelSearchRepository;

    public HorarioDisponivelResource(HorarioDisponivelRepository horarioDisponivelRepository, HorarioDisponivelSearchRepository horarioDisponivelSearchRepository) {
        this.horarioDisponivelRepository = horarioDisponivelRepository;
        this.horarioDisponivelSearchRepository = horarioDisponivelSearchRepository;
    }

    /**
     * {@code POST  /horario-disponivels} : Create a new horarioDisponivel.
     *
     * @param horarioDisponivel the horarioDisponivel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new horarioDisponivel, or with status {@code 400 (Bad Request)} if the horarioDisponivel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/horario-disponivels")
    public ResponseEntity<HorarioDisponivel> createHorarioDisponivel(@RequestBody HorarioDisponivel horarioDisponivel) throws URISyntaxException {
        log.debug("REST request to save HorarioDisponivel : {}", horarioDisponivel);
        if (horarioDisponivel.getId() != null) {
            throw new BadRequestAlertException("A new horarioDisponivel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HorarioDisponivel result = horarioDisponivelRepository.save(horarioDisponivel);
        horarioDisponivelSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/horario-disponivels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /horario-disponivels} : Updates an existing horarioDisponivel.
     *
     * @param horarioDisponivel the horarioDisponivel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated horarioDisponivel,
     * or with status {@code 400 (Bad Request)} if the horarioDisponivel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the horarioDisponivel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/horario-disponivels")
    public ResponseEntity<HorarioDisponivel> updateHorarioDisponivel(@RequestBody HorarioDisponivel horarioDisponivel) throws URISyntaxException {
        log.debug("REST request to update HorarioDisponivel : {}", horarioDisponivel);
        if (horarioDisponivel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HorarioDisponivel result = horarioDisponivelRepository.save(horarioDisponivel);
        horarioDisponivelSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, horarioDisponivel.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /horario-disponivels} : get all the horarioDisponivels.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of horarioDisponivels in body.
     */
    @GetMapping("/horario-disponivels")
    public List<HorarioDisponivel> getAllHorarioDisponivels() {
        log.debug("REST request to get all HorarioDisponivels");
        return horarioDisponivelRepository.findAll();
    }

    /**
     * {@code GET  /horario-disponivels/:id} : get the "id" horarioDisponivel.
     *
     * @param id the id of the horarioDisponivel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the horarioDisponivel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/horario-disponivels/{id}")
    public ResponseEntity<HorarioDisponivel> getHorarioDisponivel(@PathVariable Long id) {
        log.debug("REST request to get HorarioDisponivel : {}", id);
        Optional<HorarioDisponivel> horarioDisponivel = horarioDisponivelRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(horarioDisponivel);
    }

    /**
     * {@code DELETE  /horario-disponivels/:id} : delete the "id" horarioDisponivel.
     *
     * @param id the id of the horarioDisponivel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/horario-disponivels/{id}")
    public ResponseEntity<Void> deleteHorarioDisponivel(@PathVariable Long id) {
        log.debug("REST request to delete HorarioDisponivel : {}", id);
        horarioDisponivelRepository.deleteById(id);
        horarioDisponivelSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/horario-disponivels?query=:query} : search for the horarioDisponivel corresponding
     * to the query.
     *
     * @param query the query of the horarioDisponivel search.
     * @return the result of the search.
     */
    @GetMapping("/_search/horario-disponivels")
    public List<HorarioDisponivel> searchHorarioDisponivels(@RequestParam String query) {
        log.debug("REST request to search HorarioDisponivels for query {}", query);
        return StreamSupport
            .stream(horarioDisponivelSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
