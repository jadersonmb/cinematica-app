package com.cinematica.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link AgendaSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class AgendaSearchRepositoryMockConfiguration {

    @MockBean
    private AgendaSearchRepository mockAgendaSearchRepository;

}
