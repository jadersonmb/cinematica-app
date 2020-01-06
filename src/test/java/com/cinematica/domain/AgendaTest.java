package com.cinematica.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cinematica.web.rest.TestUtil;

public class AgendaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Agenda.class);
        Agenda agenda1 = new Agenda();
        agenda1.setId(1L);
        Agenda agenda2 = new Agenda();
        agenda2.setId(agenda1.getId());
        assertThat(agenda1).isEqualTo(agenda2);
        agenda2.setId(2L);
        assertThat(agenda1).isNotEqualTo(agenda2);
        agenda1.setId(null);
        assertThat(agenda1).isNotEqualTo(agenda2);
    }
}
