package com.cinematica.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link MedicoSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class MedicoSearchRepositoryMockConfiguration {

    @MockBean
    private MedicoSearchRepository mockMedicoSearchRepository;

}
