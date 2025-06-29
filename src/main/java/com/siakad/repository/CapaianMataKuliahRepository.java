package com.siakad.repository;

import com.siakad.entity.CapaianMataKuliah;
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
public interface CapaianMataKuliahRepository extends JpaRepository<CapaianMataKuliah, UUID>, JpaSpecificationExecutor<CapaianMataKuliah> {
    Optional<CapaianMataKuliah> findByIdAndIsDeletedFalse(UUID id);



    // Query relasi untuk get all prodi
    @Query("SELECT DISTINCT cpmk.siakMataKuliah.siakProgramStudi.id FROM CapaianMataKuliah cpmk WHERE cpmk.siakMataKuliah.siakProgramStudi.id IN :prodiIds")
    Set<UUID> findProdiIdsWithRelations(List<UUID> prodiIds);


    // query relasi untuk satu prodi
    @Query("SELECT CASE WHEN COUNT(cpmk) > 0 THEN true ELSE false END " +
            "FROM CapaianMataKuliah cpmk " +
            "WHERE cpmk.siakMataKuliah.siakProgramStudi.id = :prodiId")
    boolean existsCpmkByProdiId(@Param("prodiId") UUID prodiId);
}
