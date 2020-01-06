package com.cinematica.repository.search;

import com.cinematica.domain.Medico;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Medico} entity.
 */
public interface MedicoSearchRepository extends ElasticsearchRepository<Medico, Long> {
}
