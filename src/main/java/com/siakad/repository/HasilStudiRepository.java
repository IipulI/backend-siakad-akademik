package com.siakad.repository;

import com.siakad.entity.HasilStudi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HasilStudiRepository extends JpaRepository<HasilStudi, UUID> {
    Optional<HasilStudi> findByIdAndIsDeletedFalse(UUID id);

    Optional<HasilStudi> findTopBySiakMahasiswa_IdOrderByCreatedAtDesc(UUID siakMahasiswaId);
    Optional<HasilStudi> findBySiakMahasiswa_IdAndSiakPeriodeAkademik_IdAndIsDeletedFalse(UUID siakMahasiswaId, UUID periodeAkademik);
    Optional<HasilStudi> findBySiakMahasiswa_IdAndIsDeletedFalse(UUID siakMahasiswaId);

    @Query("""
        SELECT m.ips FROM HasilStudi m WHERE m.isDeleted = false
    """)
    List<BigDecimal> findIps();

    // List IPS urut berdasarkan semester
    @Query("""
    SELECT h.ips FROM HasilStudi h
    WHERE h.siakMahasiswa.id = :mahasiswaId AND h.isDeleted = false
    ORDER BY h.semester ASC
    """)
    List<BigDecimal> findIpsByMahasiswa(UUID mahasiswaId);

    // Ambil data hasil studi terakhir (semester tertinggi)
    @Query("""
    SELECT h FROM HasilStudi h
    WHERE h.siakMahasiswa.id = :mahasiswaId AND h.isDeleted = false
    ORDER BY h.semester DESC LIMIT 1
    """)
    Optional<HasilStudi> findLatestByMahasiswa(UUID mahasiswaId);

    // Total SKS kumulatif
    @Query("""
    SELECT SUM(h.sksLulus) FROM HasilStudi h
    WHERE h.siakMahasiswa.id = :mahasiswaId AND h.isDeleted = false
""")
    Integer sumSksLulusByMahasiswa(UUID mahasiswaId);

    // Total SKS diambil (opsional)
    @Query("""
    SELECT SUM(h.sksDiambil) FROM HasilStudi h
    WHERE h.siakMahasiswa.id = :mahasiswaId AND h.isDeleted = false
""")
    Integer sumSksDiambilByMahasiswa(UUID mahasiswaId);


}
