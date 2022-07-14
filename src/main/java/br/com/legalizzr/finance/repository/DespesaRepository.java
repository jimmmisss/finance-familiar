package br.com.legalizzr.finance.repository;

import br.com.legalizzr.finance.domain.Despesa;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Despesa entity.
 */
@Repository
public interface DespesaRepository extends JpaRepository<Despesa, Long> {
    default Optional<Despesa> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Despesa> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Despesa> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct despesa from Despesa despesa left join fetch despesa.subCategoria",
        countQuery = "select count(distinct despesa) from Despesa despesa"
    )
    Page<Despesa> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct despesa from Despesa despesa left join fetch despesa.subCategoria")
    List<Despesa> findAllWithToOneRelationships();

    @Query("select despesa from Despesa despesa left join fetch despesa.subCategoria where despesa.id =:id")
    Optional<Despesa> findOneWithToOneRelationships(@Param("id") Long id);
}
