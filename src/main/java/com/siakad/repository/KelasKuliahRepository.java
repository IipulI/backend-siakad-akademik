package com.siakad.repository;

import com.siakad.entity.Dosen;
import com.siakad.entity.KelasKuliah;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface KelasKuliahRepository extends JpaRepository<KelasKuliah, UUID>, JpaSpecificationExecutor<KelasKuliah> {
    Optional<KelasKuliah> findByIdAndIsDeletedFalse(UUID id);

    @Query("""
        SELECT p FROM KelasKuliah p WHERE p.id IN :ids AND p.isDeleted=false
    """)
    List<KelasKuliah> findAllByIdAndIsDeletedFalse(@Param("ids") List<UUID> ids);

    List<KelasKuliah> findAllByIsDeletedFalse();

    @Query("""
        SELECT p FROM KelasKuliah p WHERE p.statusKelas = 'Aktif'
    """)
    List<KelasKuliah> findAllByStatusKelas();
}
