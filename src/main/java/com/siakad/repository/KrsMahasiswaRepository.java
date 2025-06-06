package com.siakad.repository;

import com.siakad.entity.KrsMahasiswa;
import com.siakad.entity.KrsRincianMahasiswa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface KrsMahasiswaRepository extends JpaRepository<KrsMahasiswa, UUID> {

    @Query("SELECT SUM(k.jumlahSksDiambil) FROM KrsMahasiswa k WHERE k.siakMahasiswa.id = :mahasiswaId AND k.isDeleted = false")
    Integer getJumlahSksDiambilByMahasiswaId(UUID mahasiswaId);

    Optional<KrsMahasiswa> findBySiakMahasiswa_IdAndIsDeletedFalse(UUID siakMahasiswa_id);

    boolean existsBySiakMahasiswa_IdAndIsDeletedFalse(UUID siakMahasiswa_id);

    @Query("""
    SELECT krm FROM KrsRincianMahasiswa krm 
    JOIN krm.siakKrsMahasiswa km 
    WHERE km.status = 'Menunggu'  
      AND km.siakPeriodeAkademik.status = 'ACTIVE' 
      AND km.siakMahasiswa.id = :mahasiswaId
    """)
    List<KrsRincianMahasiswa> findAllRincianByStatusMenungguAndPeriodeAktifAndMahasiswa(UUID mahasiswaId);

    @Query("""
    SELECT CASE 
        WHEN km.status = 'Diajukan' OR km.status = 'Disetujui' THEN true 
        ELSE false 
    END 
    FROM KrsMahasiswa km 
    WHERE km.siakMahasiswa.id = :mahasiswaId 
    ORDER BY km.siakMahasiswa.semester DESC
    """)
    Boolean isDiajukan(@Param("mahasiswaId") UUID mahasiswaId);

    @Query("""
    SELECT CASE 
        WHEN km.status = 'Disetujui' THEN true 
        ELSE false 
    END 
    FROM KrsMahasiswa km 
    WHERE km.siakMahasiswa.id = :mahasiswaId 
    ORDER BY km.siakMahasiswa.semester DESC
    """)
    Boolean isDisetujui(@Param("mahasiswaId") UUID mahasiswaId);

    @Query("SELECT km.status FROM KrsMahasiswa km WHERE km.siakMahasiswa.id = :mahasiswaId ORDER BY km.siakMahasiswa.semester DESC")
    String findLatestStatus(@Param("mahasiswaId") UUID mahasiswaId);
}
