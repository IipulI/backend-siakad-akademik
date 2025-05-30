package com.siakad.repository;

import com.siakad.entity.KelasRps;
import com.siakad.entity.KelasRpsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface KelasRpsRepository extends JpaRepository<KelasRps, KelasRpsId> {
    Optional<KelasRps> findSiakRps_idBySiakKelasKuliah_IdAndIsDeletedFalse(UUID kelasId);
}
