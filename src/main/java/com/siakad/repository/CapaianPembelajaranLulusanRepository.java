package com.siakad.repository;

import com.siakad.entity.CapaianPembelajaranLulusan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    boolean existsByProfilLulusanList_SiakProgramStudi_IdAndIsDeletedFalse(UUID programStudiId);

    boolean existsBySiakProgramStudiIdAndIsDeletedFalse(UUID programStudiId);

    boolean existsByProfilLulusanList_SiakProgramStudi_IdAndSiakTahunKurikulum_IdAndIsDeletedFalse(UUID prodiId, UUID tahunKurikulumId);


    @Query("SELECT DISTINCT cpl.siakProgramStudi.id FROM CapaianPembelajaranLulusan cpl WHERE cpl.siakProgramStudi.id IN :prodiIds")
    Set<UUID> findProdiIdsWithRelations(List<UUID> prodiIds);

    // Query untuk mengecek status pemetaan PL-CPL
    @Query("SELECT DISTINCT cpl.siakProgramStudi.id FROM CapaianPembelajaranLulusan cpl WHERE cpl.siakProgramStudi.id IN :prodiIds AND SIZE(cpl.profilLulusanList) > 0")
    Set<UUID> findProdiIdsWithPemetaanPlCpl(List<UUID> prodiIds);
}
