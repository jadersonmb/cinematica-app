package com.cinematica.repository.search;

import com.cinematica.domain.FluxoCaixa;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link FluxoCaixa} entity.
 */
public interface FluxoCaixaSearchRepository extends ElasticsearchRepository<FluxoCaixa, Long> {
}
