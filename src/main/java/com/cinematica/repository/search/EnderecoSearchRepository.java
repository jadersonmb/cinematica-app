package com.cinematica.repository.search;

import com.cinematica.domain.Endereco;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Endereco} entity.
 */
public interface EnderecoSearchRepository extends ElasticsearchRepository<Endereco, Long> {
}
