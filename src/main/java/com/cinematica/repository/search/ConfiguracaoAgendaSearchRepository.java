package com.cinematica.repository.search;

import com.cinematica.domain.ConfiguracaoAgenda;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ConfiguracaoAgenda} entity.
 */
public interface ConfiguracaoAgendaSearchRepository extends ElasticsearchRepository<ConfiguracaoAgenda, Long> {
}
