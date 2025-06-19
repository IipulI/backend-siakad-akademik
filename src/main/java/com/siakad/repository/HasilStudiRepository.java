package com.siakad.repository;

import com.siakad.entity.HasilStudi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public interface HasilStudiRepository extends JpaRepository<HasilStudi, UUID> {
    Optional<HasilStudi> findByIdAndIsDeletedFalse(UUID id);

    Optional<HasilStudi> findTopBySiakMahasiswa_IdOrderByCreatedAtDesc(UUID siakMahasiswaId);
    Optional<HasilStudi> findBySiakMahasiswa_IdAndSiakPeriodeAkademik_IdAndIsDeletedFalse(UUID siakMahasiswaId, UUID periodeAkademik);
    Optional<HasilStudi> findBySiakMahasiswa_IdAndIsDeletedFalse(UUID siakMahasiswaId);
    List<HasilStudi> findAllBySiakMahasiswa_IdAndIsDeletedFalse(UUID siakMahasiswaId);

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

    /**
     * This advanced query gets ONLY the latest HasilStudi record for each student in the list.
     * It finds the max semester for each student in the subquery and joins back to it.
     *
     * @param mahasiswaIds The list of student UUIDs to find results for.
     * @return A list of the most recent HasilStudi entities for the given students.
     */
    @Query("SELECT h FROM HasilStudi h WHERE h.siakMahasiswa.id IN :mahasiswaIds AND h.semester = " +
            "(SELECT MAX(h2.semester) FROM HasilStudi h2 WHERE h2.siakMahasiswa.id = h.siakMahasiswa.id)")
    List<HasilStudi> findLatestByMahasiswaIds(List<UUID> mahasiswaIds);

    /**
     * Helper method to convert the list of results into a Map for fast lookups in the service.
     * The key is the Mahasiswa ID.
     */
    default Map<UUID, HasilStudi> findLatestMapByMahasiswaIds(List<UUID> mahasiswaIds) {
        return findLatestByMahasiswaIds(mahasiswaIds).stream()
                .collect(Collectors.toMap(hs -> hs.getSiakMahasiswa().getId(), Function.identity()));
    }

    // This single query gives us the data for both the IP and SKS Lulus charts
    List<HasilStudi> findBySiakMahasiswaIdOrderBySemesterAsc(UUID mahasiswaId);
}
