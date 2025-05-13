package com.siakad.repository;

import com.siakad.entity.CapaianPembelajaranLulusan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CapaianPembelajaranLulusanRepository extends JpaRepository<CapaianPembelajaranLulusan, UUID>,
        JpaSpecificationExecutor<CapaianPembelajaranLulusan> {
    Optional<CapaianPembelajaranLulusan> findByIdAndIsDeletedFalse(UUID id);

    @Query("""
    SELECT p FROM CapaianPembelajaranLulusan p
    WHERE p.id IN :ids AND p.isDeleted = false
    """)
    List<CapaianPembelajaranLulusan> findAllByIdInAndIsDeletedFalse(@Param("ids") List<UUID> ids);
}
