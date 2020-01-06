package com.cinematica.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cinematica.web.rest.TestUtil;

public class ProfissaoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Profissao.class);
        Profissao profissao1 = new Profissao();
        profissao1.setId(1L);
        Profissao profissao2 = new Profissao();
        profissao2.setId(profissao1.getId());
        assertThat(profissao1).isEqualTo(profissao2);
        profissao2.setId(2L);
        assertThat(profissao1).isNotEqualTo(profissao2);
        profissao1.setId(null);
        assertThat(profissao1).isNotEqualTo(profissao2);
    }
}
