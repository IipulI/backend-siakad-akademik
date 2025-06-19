package com.siakad.repository;

import com.siakad.entity.ProfilLulusan;
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
public interface ProfilLulusanRepository extends JpaRepository<ProfilLulusan, UUID>,
        JpaSpecificationExecutor<ProfilLulusan> {
    Optional<ProfilLulusan> findByIdAndIsDeletedFalse(UUID id);

    @Query("""
    SELECT p FROM ProfilLulusan p
    JOIN FETCH p.siakProgramStudi
    JOIN FETCH p.siakTahunKurikulum
    WHERE p.id IN :ids AND p.isDeleted = false
    """)
    List<ProfilLulusan> findAllByIdInAndIsDeletedFalse(@Param("ids") List<UUID> ids);


    @Query("SELECT DISTINCT pl.siakProgramStudi.id FROM ProfilLulusan pl WHERE pl.siakProgramStudi.id IN :prodiIds")
    Set<UUID> findProdiIdsWithRelations(List<UUID> prodiIds);
}
