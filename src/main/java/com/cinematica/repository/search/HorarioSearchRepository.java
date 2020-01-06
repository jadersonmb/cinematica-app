package com.cinematica.repository.search;

import com.cinematica.domain.Horario;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Horario} entity.
 */
public interface HorarioSearchRepository extends ElasticsearchRepository<Horario, Long> {
}
