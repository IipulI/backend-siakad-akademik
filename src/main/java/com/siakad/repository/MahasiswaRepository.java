package com.siakad.repository;

import com.siakad.entity.Jenjang;
import com.siakad.entity.Mahasiswa;
import com.siakad.entity.ProgramStudi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MahasiswaRepository extends JpaRepository<Mahasiswa, UUID>,
        JpaSpecificationExecutor<Mahasiswa> {

    Optional<Mahasiswa> findByIdAndIsDeletedFalse(UUID id);
    boolean existsByNpm(String npm);
    boolean existsByEmailPribadi(String email);

    @Query(value = """
        SELECT * FROM siak_mahasiswa m WHERE m.is_deleted = false
    """, nativeQuery = true)
    Page<Mahasiswa> findAllNotDeleted(Pageable pageable);

    @Query(value = """
    SELECT m.* FROM siak_mahasiswa m
    JOIN siak_program_studi ps ON m.siak_program_studi_id = ps.id
    JOIN siak_fakultas f ON ps.siak_fakultas_id = f.id
    WHERE m.is_deleted = false
""", nativeQuery = true)
    Page<Mahasiswa> findByWithRelasiNative(Specification<Mahasiswa> spec,Pageable pageable);

    @Query("""
        SELECT m FROM Mahasiswa m WHERE m.statusMahasiswa = 'Aktif'
    """)
    List<Mahasiswa> findAllByStatusMahasiswa();

    @Query("""
    SELECT COUNT(m) FROM Mahasiswa m WHERE m.statusMahasiswa = 'Aktif'
""")
    Integer countMahasiswaAktif();

    @Query("""
    SELECT COUNT(m) FROM Mahasiswa m
    """)
    Integer countAllMahasiswa();

    @Query(value = """
    SELECT COUNT(m) FROM Mahasiswa m JOIN PeriodeAkademik p ON m.periodeMasuk = p.kodePeriode
        WHERE p.status = 'ACTIVE' AND m.isDeleted = false
    """)
    Integer findMahasiswaBaruDiPeriodeAkademikAktif();

    @Query("""
    SELECT COUNT(m) FROM Mahasiswa m
    WHERE m.angkatan = :angkatan AND m.statusMahasiswa = :status AND m.isDeleted = false
""")
    int countByAngkatanAndStatus(String angkatan, String status);

    @Query("""
    SELECT DISTINCT m.angkatan FROM Mahasiswa m WHERE m.isDeleted = false
""")
    List<String> findDistinctAngkatan();


    @Query("SELECT m FROM Mahasiswa m WHERE m.isDeleted = false AND m.siakProgramStudi.id = :prodiId")
    List<Mahasiswa> findAllByProgramStudiAndNotDeleted(@Param("prodiId") UUID prodiId);


    @Query("SELECT COUNT(m) FROM Mahasiswa m WHERE m.siakProgramStudi.id = :prodiId AND m.statusMahasiswa = 'ACTIVE' AND m.isDeleted = false")
    Integer countAktifByProdi(UUID prodiId);

    @Query("SELECT COUNT(m) FROM Mahasiswa m WHERE m.siakProgramStudi.id = :prodiId AND m.statusMahasiswa = 'CUTI' AND m.isDeleted = false")
    Integer countCutiByProdi(UUID prodiId);

    @Query("SELECT COUNT(m) FROM Mahasiswa m WHERE m.siakProgramStudi.id = :prodiId AND m.statusMahasiswa = 'NON_ACTIVE' AND m.isDeleted = false")
    Integer countNonAktifByProdi(UUID prodiId);

    @Query("SELECT COUNT(m) FROM Mahasiswa m WHERE m.siakProgramStudi.id = :prodiId AND m.isDeleted = false")
    Integer countTotalByProdi(UUID prodiId);

    int countBySiakProgramStudiAndIsDeletedFalse(ProgramStudi ps);

    int countBySiakProgramStudiAndJenisKelaminAndIsDeletedFalse(ProgramStudi ps, String jenisKelamin);

    int countBySiakProgramStudiAndNoTerdaftarIsNotNullAndIsDeletedFalse(ProgramStudi ps);

    int countBySiakProgramStudiAndNoTerdaftarIsNullAndIsDeletedFalse(ProgramStudi ps);

    @Query("SELECT m.siakProgramStudi.siakJenjang FROM Mahasiswa m WHERE m.id = :mahasiswaId")
    Jenjang findJenjangByMahasiswaId(@Param("mahasiswaId") UUID mahasiswaId);


    @Query(value = """
        WITH LatestHasilStudi AS (
            SELECT
                hs.siak_mahasiswa_id,
                hs.ips
            FROM
                siak_hasil_studi AS hs
            WHERE
                hs.siak_mahasiswa_id = :id
            ORDER BY
                hs.semester DESC
            LIMIT 1
        ),
        ActiveKRSStatus AS (
            SELECT
                km.siak_mahasiswa_id,
                km.status
            FROM
                siak_krs_mahasiswa AS km
            LEFT JOIN
                siak_periode_akademik AS spa ON km.siak_periode_akademik_id = spa.id
            WHERE
                km.siak_mahasiswa_id = :id
                AND spa.status = 'ACTIVE'
            LIMIT 1
        ),
        ActivePeriodeAkademik AS (
            SELECT
                paa.nama_periode
            FROM
                siak_periode_akademik AS paa
            WHERE
                paa.status = 'ACTIVE'
            LIMIT 1
        )
        SELECT
            m.nama,
            m.semester,
            m.id AS mahasiswa_id,
            d.nama AS dosen_pembimbing,
            bs.batas_sks,
            aks.status,
            apa.nama_periode
        FROM
            siak_mahasiswa AS m
        LEFT JOIN
            siak_program_studi AS ps ON m.siak_program_studi_id = ps.id
        LEFT JOIN
            siak_pembimbing_akademik AS pa ON m.id = pa.siak_mahasiswa_id
        LEFT JOIN
            siak_dosen AS d ON pa.siak_dosen_id = d.id
        LEFT JOIN
            LatestHasilStudi AS lhs ON lhs.siak_mahasiswa_id = m.id
        LEFT JOIN
            siak_batas_sks AS bs ON bs.siak_jenjang_id = ps.siak_jenjang_id
                                  AND lhs.ips BETWEEN bs.ips_min AND bs.ips_max 
        LEFT JOIN
            ActiveKRSStatus AS aks ON aks.siak_mahasiswa_id = m.id
        CROSS JOIN
            ActivePeriodeAkademik AS apa
        WHERE
            m.id = :id
        LIMIT 1;
    """, nativeQuery = true)
    Optional<Object[]> getKrsInfo(@Param("id") UUID id);

}
