package com.siakad.repository;

import com.siakad.entity.JadwalKuliah;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JadwalKuliahRepository extends JpaRepository<JadwalKuliah, UUID> {

    List<JadwalKuliah> findAllByIsDeletedFalse();

    Optional<JadwalKuliah> findByIdAndIsDeletedFalse(UUID id);

    @Query("SELECT j FROM JadwalKuliah j WHERE j.siakKelasKuliah.id = :id AND j.isDeleted = false")
    List<JadwalKuliah> findAllByKelasKuliahIdAndIsDeletedFalse(@Param("id") UUID kelasKuliahId);

    @Query("SELECT j FROM JadwalKuliah j WHERE j.siakKelasKuliah.id = :id AND j.siakDosen.id = :dosenId AND j.isDeleted = false ")
    List<JadwalKuliah> findJadwalKuliahBySiakDosenIdAndIsDeletedFalse(@Param("id") UUID kelasKuliahId, @Param("dosenId") UUID dosenId);

    List<JadwalKuliah> findBySiakKelasKuliahId(UUID siakKelasKuliahId);

    List<JadwalKuliah> findByHariIgnoreCaseAndSiakKelasKuliah_SiakPeriodeAkademik_IdAndIsDeletedFalse(
            String hari, UUID siakPeriodeAkademikId);

}
