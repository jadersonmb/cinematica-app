package com.cinematica.web.rest;

import com.cinematica.domain.Horario;
import com.cinematica.repository.HorarioRepository;
import com.cinematica.repository.search.HorarioSearchRepository;
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
 * REST controller for managing {@link com.cinematica.domain.Horario}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class HorarioResource {

    private final Logger log = LoggerFactory.getLogger(HorarioResource.class);

    private static final String ENTITY_NAME = "horario";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HorarioRepository horarioRepository;

    private final HorarioSearchRepository horarioSearchRepository;

    public HorarioResource(HorarioRepository horarioRepository, HorarioSearchRepository horarioSearchRepository) {
        this.horarioRepository = horarioRepository;
        this.horarioSearchRepository = horarioSearchRepository;
    }

    /**
     * {@code POST  /horarios} : Create a new horario.
     *
     * @param horario the horario to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new horario, or with status {@code 400 (Bad Request)} if the horario has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/horarios")
    public ResponseEntity<Horario> createHorario(@RequestBody Horario horario) throws URISyntaxException {
        log.debug("REST request to save Horario : {}", horario);
        if (horario.getId() != null) {
            throw new BadRequestAlertException("A new horario cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Horario result = horarioRepository.save(horario);
        horarioSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/horarios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /horarios} : Updates an existing horario.
     *
     * @param horario the horario to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated horario,
     * or with status {@code 400 (Bad Request)} if the horario is not valid,
     * or with status {@code 500 (Internal Server Error)} if the horario couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/horarios")
    public ResponseEntity<Horario> updateHorario(@RequestBody Horario horario) throws URISyntaxException {
        log.debug("REST request to update Horario : {}", horario);
        if (horario.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Horario result = horarioRepository.save(horario);
        horarioSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, horario.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /horarios} : get all the horarios.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of horarios in body.
     */
    @GetMapping("/horarios")
    public List<Horario> getAllHorarios() {
        log.debug("REST request to get all Horarios");
        return horarioRepository.findAll();
    }

    /**
     * {@code GET  /horarios/:id} : get the "id" horario.
     *
     * @param id the id of the horario to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the horario, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/horarios/{id}")
    public ResponseEntity<Horario> getHorario(@PathVariable Long id) {
        log.debug("REST request to get Horario : {}", id);
        Optional<Horario> horario = horarioRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(horario);
    }

    /**
     * {@code DELETE  /horarios/:id} : delete the "id" horario.
     *
     * @param id the id of the horario to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/horarios/{id}")
    public ResponseEntity<Void> deleteHorario(@PathVariable Long id) {
        log.debug("REST request to delete Horario : {}", id);
        horarioRepository.deleteById(id);
        horarioSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/horarios?query=:query} : search for the horario corresponding
     * to the query.
     *
     * @param query the query of the horario search.
     * @return the result of the search.
     */
    @GetMapping("/_search/horarios")
    public List<Horario> searchHorarios(@RequestParam String query) {
        log.debug("REST request to search Horarios for query {}", query);
        return StreamSupport
            .stream(horarioSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
