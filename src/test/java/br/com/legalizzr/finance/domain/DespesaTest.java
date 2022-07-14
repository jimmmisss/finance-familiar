package br.com.legalizzr.finance.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.legalizzr.finance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DespesaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Despesa.class);
        Despesa despesa1 = new Despesa();
        despesa1.setId(1L);
        Despesa despesa2 = new Despesa();
        despesa2.setId(despesa1.getId());
        assertThat(despesa1).isEqualTo(despesa2);
        despesa2.setId(2L);
        assertThat(despesa1).isNotEqualTo(despesa2);
        despesa1.setId(null);
        assertThat(despesa1).isNotEqualTo(despesa2);
    }
}
