package com.cinematica.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cinematica.web.rest.TestUtil;

public class FormaPagamentoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormaPagamento.class);
        FormaPagamento formaPagamento1 = new FormaPagamento();
        formaPagamento1.setId(1L);
        FormaPagamento formaPagamento2 = new FormaPagamento();
        formaPagamento2.setId(formaPagamento1.getId());
        assertThat(formaPagamento1).isEqualTo(formaPagamento2);
        formaPagamento2.setId(2L);
        assertThat(formaPagamento1).isNotEqualTo(formaPagamento2);
        formaPagamento1.setId(null);
        assertThat(formaPagamento1).isNotEqualTo(formaPagamento2);
    }
}
