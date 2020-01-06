package com.cinematica.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cinematica.web.rest.TestUtil;

public class ConfiguracaoAgendaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfiguracaoAgenda.class);
        ConfiguracaoAgenda configuracaoAgenda1 = new ConfiguracaoAgenda();
        configuracaoAgenda1.setId(1L);
        ConfiguracaoAgenda configuracaoAgenda2 = new ConfiguracaoAgenda();
        configuracaoAgenda2.setId(configuracaoAgenda1.getId());
        assertThat(configuracaoAgenda1).isEqualTo(configuracaoAgenda2);
        configuracaoAgenda2.setId(2L);
        assertThat(configuracaoAgenda1).isNotEqualTo(configuracaoAgenda2);
        configuracaoAgenda1.setId(null);
        assertThat(configuracaoAgenda1).isNotEqualTo(configuracaoAgenda2);
    }
}
