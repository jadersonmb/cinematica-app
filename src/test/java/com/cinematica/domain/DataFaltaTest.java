package com.cinematica.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.cinematica.web.rest.TestUtil;

public class DataFaltaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataFalta.class);
        DataFalta dataFalta1 = new DataFalta();
        dataFalta1.setId(1L);
        DataFalta dataFalta2 = new DataFalta();
        dataFalta2.setId(dataFalta1.getId());
        assertThat(dataFalta1).isEqualTo(dataFalta2);
        dataFalta2.setId(2L);
        assertThat(dataFalta1).isNotEqualTo(dataFalta2);
        dataFalta1.setId(null);
        assertThat(dataFalta1).isNotEqualTo(dataFalta2);
    }
}
