package com.cinematica.repository.search;

import com.cinematica.domain.HorarioDisponivel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link HorarioDisponivel} entity.
 */
public interface HorarioDisponivelSearchRepository extends ElasticsearchRepository<HorarioDisponivel, Long> {
}
