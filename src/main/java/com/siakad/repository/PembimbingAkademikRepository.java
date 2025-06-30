package com.siakad.repository;

import com.siakad.entity.PembimbingAkademik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PembimbingAkademikRepository extends JpaRepository<PembimbingAkademik, UUID>,
        JpaSpecificationExecutor<PembimbingAkademik> {
    List<PembimbingAkademik> findAllByIsDeletedFalse();

    Optional<PembimbingAkademik> findBySiakMahasiswa_IdAndIsDeletedFalse(UUID siakMahasiswa);

    List<PembimbingAkademik> findAllBySiakMahasiswa_IdAndSiakPeriodeAkademik_IdAndIsDeletedFalse(UUID mahasiswaId, UUID periodeId);

    List<PembimbingAkademik> findBySiakMahasiswaIdInAndSiakPeriodeAkademikId(List<UUID> mahasiswaIds, UUID periodeAkademikId);

    UUID id(UUID id);
}
