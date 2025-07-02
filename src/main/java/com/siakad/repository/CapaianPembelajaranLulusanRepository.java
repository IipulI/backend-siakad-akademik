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


    // Query relasi untuk get all prodi
    @Query("SELECT DISTINCT cpl.siakProgramStudi.id FROM CapaianPembelajaranLulusan cpl WHERE cpl.siakProgramStudi.id IN :prodiIds")
    Set<UUID> findProdiIdsWithRelations(List<UUID> prodiIds);

    @Query("SELECT DISTINCT cpl.siakProgramStudi.id FROM CapaianPembelajaranLulusan cpl WHERE cpl.siakProgramStudi.id IN :prodiIds AND SIZE(cpl.profilLulusanList) > 0")
    Set<UUID> findProdiIdsWithPemetaanPlCpl(List<UUID> prodiIds);



    // query relasi untuk satu prodi
    boolean existsBySiakProgramStudiId(UUID prodiId);

    @Query("SELECT CASE WHEN COUNT(cpl) > 0 THEN true ELSE false END " +
            "FROM CapaianPembelajaranLulusan cpl " +
            "WHERE cpl.siakProgramStudi.id = :prodiId AND SIZE(cpl.profilLulusanList) > 0")
    boolean existsPemetaanPlCplByProdiId(@Param("prodiId") UUID prodiId);
}
