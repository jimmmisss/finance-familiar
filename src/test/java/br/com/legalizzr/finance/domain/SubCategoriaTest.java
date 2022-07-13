package br.com.legalizzr.finance.domain;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.legalizzr.finance.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubCategoriaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubCategoria.class);
        SubCategoria subCategoria1 = new SubCategoria();
        subCategoria1.setId(1L);
        SubCategoria subCategoria2 = new SubCategoria();
        subCategoria2.setId(subCategoria1.getId());
        assertThat(subCategoria1).isEqualTo(subCategoria2);
        subCategoria2.setId(2L);
        assertThat(subCategoria1).isNotEqualTo(subCategoria2);
        subCategoria1.setId(null);
        assertThat(subCategoria1).isNotEqualTo(subCategoria2);
    }
}
