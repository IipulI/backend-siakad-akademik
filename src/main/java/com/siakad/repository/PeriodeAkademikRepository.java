package com.siakad.repository;

import com.siakad.entity.PeriodeAkademik;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PeriodeAkademikRepository extends JpaRepository<PeriodeAkademik, UUID>,
        JpaSpecificationExecutor<PeriodeAkademik> {
    Optional<PeriodeAkademik> findByIdAndIsDeletedFalse(UUID id);

    Optional<PeriodeAkademik> findByKodePeriodeAndIsDeletedFalse(String kodePeriode);

    @Query("""
    SELECT p FROM PeriodeAkademik p
    WHERE p.status = 'ACTIVE' AND p.isDeleted = false
    ORDER BY p.tanggalMulai DESC
""")
    Optional<PeriodeAkademik> findFirstByStatusActive();


    List<PeriodeAkademik> findAllByIsDeletedFalse();

}
