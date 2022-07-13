package br.com.legalizzr.finance.repository;

import br.com.legalizzr.finance.domain.SubCategoria;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SubCategoria entity.
 */
@Repository
public interface SubCategoriaRepository extends JpaRepository<SubCategoria, Long> {
    default Optional<SubCategoria> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<SubCategoria> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<SubCategoria> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct subCategoria from SubCategoria subCategoria left join fetch subCategoria.categoria",
        countQuery = "select count(distinct subCategoria) from SubCategoria subCategoria"
    )
    Page<SubCategoria> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct subCategoria from SubCategoria subCategoria left join fetch subCategoria.categoria")
    List<SubCategoria> findAllWithToOneRelationships();

    @Query("select subCategoria from SubCategoria subCategoria left join fetch subCategoria.categoria where subCategoria.id =:id")
    Optional<SubCategoria> findOneWithToOneRelationships(@Param("id") Long id);
}
