package com.cinematica.web.rest;

import com.cinematica.domain.FluxoCaixa;
import com.cinematica.repository.FluxoCaixaRepository;
import com.cinematica.repository.search.FluxoCaixaSearchRepository;
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
 * REST controller for managing {@link com.cinematica.domain.FluxoCaixa}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FluxoCaixaResource {

    private final Logger log = LoggerFactory.getLogger(FluxoCaixaResource.class);

    private static final String ENTITY_NAME = "fluxoCaixa";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FluxoCaixaRepository fluxoCaixaRepository;

    private final FluxoCaixaSearchRepository fluxoCaixaSearchRepository;

    public FluxoCaixaResource(FluxoCaixaRepository fluxoCaixaRepository, FluxoCaixaSearchRepository fluxoCaixaSearchRepository) {
        this.fluxoCaixaRepository = fluxoCaixaRepository;
        this.fluxoCaixaSearchRepository = fluxoCaixaSearchRepository;
    }

    /**
     * {@code POST  /fluxo-caixas} : Create a new fluxoCaixa.
     *
     * @param fluxoCaixa the fluxoCaixa to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fluxoCaixa, or with status {@code 400 (Bad Request)} if the fluxoCaixa has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fluxo-caixas")
    public ResponseEntity<FluxoCaixa> createFluxoCaixa(@RequestBody FluxoCaixa fluxoCaixa) throws URISyntaxException {
        log.debug("REST request to save FluxoCaixa : {}", fluxoCaixa);
        if (fluxoCaixa.getId() != null) {
            throw new BadRequestAlertException("A new fluxoCaixa cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FluxoCaixa result = fluxoCaixaRepository.save(fluxoCaixa);
        fluxoCaixaSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/fluxo-caixas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fluxo-caixas} : Updates an existing fluxoCaixa.
     *
     * @param fluxoCaixa the fluxoCaixa to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fluxoCaixa,
     * or with status {@code 400 (Bad Request)} if the fluxoCaixa is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fluxoCaixa couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fluxo-caixas")
    public ResponseEntity<FluxoCaixa> updateFluxoCaixa(@RequestBody FluxoCaixa fluxoCaixa) throws URISyntaxException {
        log.debug("REST request to update FluxoCaixa : {}", fluxoCaixa);
        if (fluxoCaixa.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FluxoCaixa result = fluxoCaixaRepository.save(fluxoCaixa);
        fluxoCaixaSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fluxoCaixa.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /fluxo-caixas} : get all the fluxoCaixas.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fluxoCaixas in body.
     */
    @GetMapping("/fluxo-caixas")
    public List<FluxoCaixa> getAllFluxoCaixas() {
        log.debug("REST request to get all FluxoCaixas");
        return fluxoCaixaRepository.findAll();
    }

    /**
     * {@code GET  /fluxo-caixas/:id} : get the "id" fluxoCaixa.
     *
     * @param id the id of the fluxoCaixa to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fluxoCaixa, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fluxo-caixas/{id}")
    public ResponseEntity<FluxoCaixa> getFluxoCaixa(@PathVariable Long id) {
        log.debug("REST request to get FluxoCaixa : {}", id);
        Optional<FluxoCaixa> fluxoCaixa = fluxoCaixaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(fluxoCaixa);
    }

    /**
     * {@code DELETE  /fluxo-caixas/:id} : delete the "id" fluxoCaixa.
     *
     * @param id the id of the fluxoCaixa to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fluxo-caixas/{id}")
    public ResponseEntity<Void> deleteFluxoCaixa(@PathVariable Long id) {
        log.debug("REST request to delete FluxoCaixa : {}", id);
        fluxoCaixaRepository.deleteById(id);
        fluxoCaixaSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/fluxo-caixas?query=:query} : search for the fluxoCaixa corresponding
     * to the query.
     *
     * @param query the query of the fluxoCaixa search.
     * @return the result of the search.
     */
    @GetMapping("/_search/fluxo-caixas")
    public List<FluxoCaixa> searchFluxoCaixas(@RequestParam String query) {
        log.debug("REST request to search FluxoCaixas for query {}", query);
        return StreamSupport
            .stream(fluxoCaixaSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
