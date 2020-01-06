package com.cinematica.web.rest;

import com.cinematica.domain.DataFalta;
import com.cinematica.repository.DataFaltaRepository;
import com.cinematica.repository.search.DataFaltaSearchRepository;
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
 * REST controller for managing {@link com.cinematica.domain.DataFalta}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DataFaltaResource {

    private final Logger log = LoggerFactory.getLogger(DataFaltaResource.class);

    private static final String ENTITY_NAME = "dataFalta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DataFaltaRepository dataFaltaRepository;

    private final DataFaltaSearchRepository dataFaltaSearchRepository;

    public DataFaltaResource(DataFaltaRepository dataFaltaRepository, DataFaltaSearchRepository dataFaltaSearchRepository) {
        this.dataFaltaRepository = dataFaltaRepository;
        this.dataFaltaSearchRepository = dataFaltaSearchRepository;
    }

    /**
     * {@code POST  /data-faltas} : Create a new dataFalta.
     *
     * @param dataFalta the dataFalta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dataFalta, or with status {@code 400 (Bad Request)} if the dataFalta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/data-faltas")
    public ResponseEntity<DataFalta> createDataFalta(@RequestBody DataFalta dataFalta) throws URISyntaxException {
        log.debug("REST request to save DataFalta : {}", dataFalta);
        if (dataFalta.getId() != null) {
            throw new BadRequestAlertException("A new dataFalta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DataFalta result = dataFaltaRepository.save(dataFalta);
        dataFaltaSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/data-faltas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /data-faltas} : Updates an existing dataFalta.
     *
     * @param dataFalta the dataFalta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dataFalta,
     * or with status {@code 400 (Bad Request)} if the dataFalta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dataFalta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/data-faltas")
    public ResponseEntity<DataFalta> updateDataFalta(@RequestBody DataFalta dataFalta) throws URISyntaxException {
        log.debug("REST request to update DataFalta : {}", dataFalta);
        if (dataFalta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DataFalta result = dataFaltaRepository.save(dataFalta);
        dataFaltaSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dataFalta.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /data-faltas} : get all the dataFaltas.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dataFaltas in body.
     */
    @GetMapping("/data-faltas")
    public List<DataFalta> getAllDataFaltas() {
        log.debug("REST request to get all DataFaltas");
        return dataFaltaRepository.findAll();
    }

    /**
     * {@code GET  /data-faltas/:id} : get the "id" dataFalta.
     *
     * @param id the id of the dataFalta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dataFalta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/data-faltas/{id}")
    public ResponseEntity<DataFalta> getDataFalta(@PathVariable Long id) {
        log.debug("REST request to get DataFalta : {}", id);
        Optional<DataFalta> dataFalta = dataFaltaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(dataFalta);
    }

    /**
     * {@code DELETE  /data-faltas/:id} : delete the "id" dataFalta.
     *
     * @param id the id of the dataFalta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/data-faltas/{id}")
    public ResponseEntity<Void> deleteDataFalta(@PathVariable Long id) {
        log.debug("REST request to delete DataFalta : {}", id);
        dataFaltaRepository.deleteById(id);
        dataFaltaSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/data-faltas?query=:query} : search for the dataFalta corresponding
     * to the query.
     *
     * @param query the query of the dataFalta search.
     * @return the result of the search.
     */
    @GetMapping("/_search/data-faltas")
    public List<DataFalta> searchDataFaltas(@RequestParam String query) {
        log.debug("REST request to search DataFaltas for query {}", query);
        return StreamSupport
            .stream(dataFaltaSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
