package com.cinematica.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link FormaPagamentoSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class FormaPagamentoSearchRepositoryMockConfiguration {

    @MockBean
    private FormaPagamentoSearchRepository mockFormaPagamentoSearchRepository;

}
