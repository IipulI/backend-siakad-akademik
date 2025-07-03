package com.siakad.repository;

import com.siakad.entity.JadwalKuliah;
import com.siakad.entity.JadwalUjian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JadwalUjianRepository extends JpaRepository<JadwalUjian, UUID> {

    @Query("SELECT j FROM JadwalUjian j WHERE j.siakKelasKuliah.id = :id and j.isDeleted = false")
    List<JadwalUjian> findAllByKelasKuliahIdAndIsDeletedFalse(@Param("id") UUID id);
}
