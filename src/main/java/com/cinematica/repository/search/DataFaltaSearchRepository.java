package com.cinematica.repository.search;

import com.cinematica.domain.DataFalta;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link DataFalta} entity.
 */
public interface DataFaltaSearchRepository extends ElasticsearchRepository<DataFalta, Long> {
}
