package com.cinematica.web.rest;

import com.cinematica.domain.FormaPagamento;
import com.cinematica.repository.FormaPagamentoRepository;
import com.cinematica.repository.search.FormaPagamentoSearchRepository;
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
 * REST controller for managing {@link com.cinematica.domain.FormaPagamento}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FormaPagamentoResource {

    private final Logger log = LoggerFactory.getLogger(FormaPagamentoResource.class);

    private static final String ENTITY_NAME = "formaPagamento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormaPagamentoRepository formaPagamentoRepository;

    private final FormaPagamentoSearchRepository formaPagamentoSearchRepository;

    public FormaPagamentoResource(FormaPagamentoRepository formaPagamentoRepository, FormaPagamentoSearchRepository formaPagamentoSearchRepository) {
        this.formaPagamentoRepository = formaPagamentoRepository;
        this.formaPagamentoSearchRepository = formaPagamentoSearchRepository;
    }

    /**
     * {@code POST  /forma-pagamentos} : Create a new formaPagamento.
     *
     * @param formaPagamento the formaPagamento to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formaPagamento, or with status {@code 400 (Bad Request)} if the formaPagamento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/forma-pagamentos")
    public ResponseEntity<FormaPagamento> createFormaPagamento(@RequestBody FormaPagamento formaPagamento) throws URISyntaxException {
        log.debug("REST request to save FormaPagamento : {}", formaPagamento);
        if (formaPagamento.getId() != null) {
            throw new BadRequestAlertException("A new formaPagamento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FormaPagamento result = formaPagamentoRepository.save(formaPagamento);
        formaPagamentoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/forma-pagamentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /forma-pagamentos} : Updates an existing formaPagamento.
     *
     * @param formaPagamento the formaPagamento to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formaPagamento,
     * or with status {@code 400 (Bad Request)} if the formaPagamento is not valid,
     * or with status {@code 500 (Internal Server Error)} if the formaPagamento couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/forma-pagamentos")
    public ResponseEntity<FormaPagamento> updateFormaPagamento(@RequestBody FormaPagamento formaPagamento) throws URISyntaxException {
        log.debug("REST request to update FormaPagamento : {}", formaPagamento);
        if (formaPagamento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FormaPagamento result = formaPagamentoRepository.save(formaPagamento);
        formaPagamentoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formaPagamento.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /forma-pagamentos} : get all the formaPagamentos.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of formaPagamentos in body.
     */
    @GetMapping("/forma-pagamentos")
    public List<FormaPagamento> getAllFormaPagamentos() {
        log.debug("REST request to get all FormaPagamentos");
        return formaPagamentoRepository.findAll();
    }

    /**
     * {@code GET  /forma-pagamentos/:id} : get the "id" formaPagamento.
     *
     * @param id the id of the formaPagamento to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the formaPagamento, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/forma-pagamentos/{id}")
    public ResponseEntity<FormaPagamento> getFormaPagamento(@PathVariable Long id) {
        log.debug("REST request to get FormaPagamento : {}", id);
        Optional<FormaPagamento> formaPagamento = formaPagamentoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(formaPagamento);
    }

    /**
     * {@code DELETE  /forma-pagamentos/:id} : delete the "id" formaPagamento.
     *
     * @param id the id of the formaPagamento to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/forma-pagamentos/{id}")
    public ResponseEntity<Void> deleteFormaPagamento(@PathVariable Long id) {
        log.debug("REST request to delete FormaPagamento : {}", id);
        formaPagamentoRepository.deleteById(id);
        formaPagamentoSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/forma-pagamentos?query=:query} : search for the formaPagamento corresponding
     * to the query.
     *
     * @param query the query of the formaPagamento search.
     * @return the result of the search.
     */
    @GetMapping("/_search/forma-pagamentos")
    public List<FormaPagamento> searchFormaPagamentos(@RequestParam String query) {
        log.debug("REST request to search FormaPagamentos for query {}", query);
        return StreamSupport
            .stream(formaPagamentoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
