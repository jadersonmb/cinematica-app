package com.cinematica.repository.search;

import com.cinematica.domain.Profissao;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Profissao} entity.
 */
public interface ProfissaoSearchRepository extends ElasticsearchRepository<Profissao, Long> {
}
