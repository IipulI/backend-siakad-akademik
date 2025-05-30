package com.siakad.repository;

import com.siakad.entity.HasilStudi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface HasilStudiRepository extends JpaRepository<HasilStudi, UUID> {
    Optional<HasilStudi> findByIdAndIsDeletedFalse(UUID id);

    Optional<HasilStudi> findTopBySiakMahasiswa_IdOrderByCreatedAtDesc(UUID siakMahasiswaId);
    Optional<HasilStudi> findBySiakMahasiswa_IdAndSiakPeriodeAkademik_IdAndIsDeletedFalse(UUID siakMahasiswaId, UUID periodeAkademik);
}
