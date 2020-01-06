package com.cinematica.repository.search;

import com.cinematica.domain.Agenda;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Agenda} entity.
 */
public interface AgendaSearchRepository extends ElasticsearchRepository<Agenda, Long> {
}
