package com.siakad.repository;

import com.siakad.entity.KrsMahasiswa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface KrsMahasiswaRepository extends JpaRepository<KrsMahasiswa, UUID> {

    @Query("SELECT SUM(k.jumlahSksDiambil) FROM KrsMahasiswa k WHERE k.siakMahasiswa.id = :mahasiswaId AND k.isDeleted = false")
    Integer getJumlahSksDiambilByMahasiswaId(UUID mahasiswaId);
}
