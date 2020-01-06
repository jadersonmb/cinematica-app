package com.cinematica.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cinematica.web.rest.TestUtil;

public class FluxoCaixaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FluxoCaixa.class);
        FluxoCaixa fluxoCaixa1 = new FluxoCaixa();
        fluxoCaixa1.setId(1L);
        FluxoCaixa fluxoCaixa2 = new FluxoCaixa();
        fluxoCaixa2.setId(fluxoCaixa1.getId());
        assertThat(fluxoCaixa1).isEqualTo(fluxoCaixa2);
        fluxoCaixa2.setId(2L);
        assertThat(fluxoCaixa1).isNotEqualTo(fluxoCaixa2);
        fluxoCaixa1.setId(null);
        assertThat(fluxoCaixa1).isNotEqualTo(fluxoCaixa2);
    }
}
