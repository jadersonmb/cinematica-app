package com.cinematica.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cinematica.web.rest.TestUtil;

public class HorarioDisponivelTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HorarioDisponivel.class);
        HorarioDisponivel horarioDisponivel1 = new HorarioDisponivel();
        horarioDisponivel1.setId(1L);
        HorarioDisponivel horarioDisponivel2 = new HorarioDisponivel();
        horarioDisponivel2.setId(horarioDisponivel1.getId());
        assertThat(horarioDisponivel1).isEqualTo(horarioDisponivel2);
        horarioDisponivel2.setId(2L);
        assertThat(horarioDisponivel1).isNotEqualTo(horarioDisponivel2);
        horarioDisponivel1.setId(null);
        assertThat(horarioDisponivel1).isNotEqualTo(horarioDisponivel2);
    }
}
