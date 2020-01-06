package com.cinematica.repository.search;

import com.cinematica.domain.Paciente;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Paciente} entity.
 */
public interface PacienteSearchRepository extends ElasticsearchRepository<Paciente, Long> {
}
