package com.cinematica.repository.search;

import com.cinematica.domain.FormaPagamento;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link FormaPagamento} entity.
 */
public interface FormaPagamentoSearchRepository extends ElasticsearchRepository<FormaPagamento, Long> {
}
