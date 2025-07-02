package com.siakad.repository;

import com.siakad.dto.response.KelasKuliahWithTakenStatusDto;
import com.siakad.entity.JadwalKuliah;
import com.siakad.entity.KelasKuliah;
import com.siakad.entity.KrsRincianMahasiswa;
import com.siakad.entity.Mahasiswa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface KrsRincianMahasiswaRepository extends JpaRepository<KrsRincianMahasiswa, UUID>,
        JpaSpecificationExecutor<KrsRincianMahasiswa> {

    Optional<KrsRincianMahasiswa> findByIdAndIsDeletedFalse(UUID id);

    List<KrsRincianMahasiswa> findAllBySiakKrsMahasiswa_IdAndSiakKelasKuliah_IdInAndIsDeletedFalse(UUID krsId, List<UUID> kelasIds);

    List<KrsRincianMahasiswa> findAllBySiakKrsMahasiswa_IdAndIsDeletedFalse(UUID id);
    Optional<KrsRincianMahasiswa> findBySiakKrsMahasiswa_IdAndSiakKelasKuliah_Id(UUID krsId, UUID kelasId);
    boolean existsBySiakKelasKuliah_SiakMataKuliah_IdAndSiakKrsMahasiswa_SiakMahasiswa_IdAndSiakKrsMahasiswa_IdNotAndIsDeletedFalse(
            UUID mataKuliahId, UUID mahasiswaId, UUID excludeKrsId);
    Boolean existsBySiakKelasKuliah_IdAndSiakKrsMahasiswa_SiakMahasiswa_IdAndIsDeletedFalse(UUID kelasId, UUID krsId);

    @Query("SELECT kr FROM KrsRincianMahasiswa kr " +
            "WHERE kr.siakKrsMahasiswa.siakMahasiswa.id = :mahasiswaId " +
            "AND kr.isDeleted = false")
    List<KrsRincianMahasiswa> findBySiakKrsMahasiswaSiakMahasiswaIdAndIsDeletedFalse(
            @Param("mahasiswaId") UUID mahasiswaId
    );

    List<KrsRincianMahasiswa> findBySiakKrsMahasiswaSiakMahasiswa_IdAndSiakKrsMahasiswaSiakPeriodeAkademik_IdAndIsDeletedFalse(UUID siakMahasiswaId, UUID periodeAkademikId);

     @Query("SELECT kr FROM KrsRincianMahasiswa kr JOIN FETCH kr.siakKelasKuliah skk JOIN FETCH skk.siakMataKuliah " +
            "WHERE kr.siakKrsMahasiswa.siakMahasiswa.id = :mahasiswaId " +
            "AND kr.siakKrsMahasiswa.siakPeriodeAkademik.namaPeriode = :namaPeriode " +
            "AND kr.isDeleted = false")
     List<KrsRincianMahasiswa> findBySiakKrsMahasiswaSiakMahasiswaIdAndSiakKrsMahasiswaSiakPeriodeAkademikIdAndIsDeletedFalse(
            @Param("mahasiswaId") UUID mahasiswaId, @Param("namaPeriode") String namaPeriode);

    @Query(value = """
    SELECT j.* FROM siak_jadwal_kuliah j
    JOIN siak_kelas_kuliah kk ON j.siak_kelas_kuliah_id = kk.id
    JOIN siak_rincian_krs_mahasiswa krm ON krm.siak_kelas_kuliah_id = kk.id
    WHERE krm.id = :krsId
    """, nativeQuery = true)
    List<JadwalKuliah> findJadwalByKrsIdNative(@Param("krsId") UUID krsRincianId);

    List<KrsRincianMahasiswa> findAllBySiakKelasKuliah_IdAndSiakKrsMahasiswa_SiakMahasiswa_IdInAndIsDeletedFalse(UUID kelas, List<UUID> mahasiswaIds);

    @Query("SELECT k FROM KrsRincianMahasiswa k where k.siakKelasKuliah.id=:kelasId AND k.isDeleted=false")
    List<KrsRincianMahasiswa> findPesertaByKelasIdAndIsDeletedFalse(@Param("kelasId") UUID kelasId);

    @Query("""
    SELECT k FROM KrsRincianMahasiswa k 
    WHERE k.siakKrsMahasiswa.id = :krsMahasiswaId 
      AND k.siakKelasKuliah.id = :kelasId 
      AND k.siakKrsMahasiswa.siakPeriodeAkademik.status = 'ACTIVE'
      AND k.isDeleted = false 
      AND (k.status IS NULL OR k.status = '' OR k.status = 'Tidak Lulus')
""")
    Optional<KrsRincianMahasiswa> findFirstByKrsMahasiswaIdAndKelasIdAndPeriodeStatusActive(
            @Param("krsMahasiswaId") UUID krsMahasiswaId,
            @Param("kelasId") UUID kelasId
    );

    List<KrsRincianMahasiswa> findBySiakKelasKuliahAndIsDeletedFalse(KelasKuliah kelas);
    int countBySiakKelasKuliahAndIsDeletedFalse(KelasKuliah kelas);

    Optional<KrsRincianMahasiswa> findBySiakKelasKuliah_IdAndSiakKrsMahasiswa_SiakMahasiswa_IdAndIsDeletedFalse(UUID kelasId, UUID mahasiswaId);

    @Query("""
    SELECT DISTINCT r.siakKrsMahasiswa.siakMahasiswa 
    FROM KrsRincianMahasiswa r 
    WHERE r.siakKelasKuliah.id = :kelasKuliahId
      AND r.isDeleted = false
      AND r.siakKrsMahasiswa.isDeleted = false
""")
    List<Mahasiswa> findMahasiswaByKelasKuliahId(@Param("kelasKuliahId") UUID kelasKuliahId);


    List<KrsRincianMahasiswa> findAllByIsDeletedFalse();

    List<KrsRincianMahasiswa> findAllBySiakKrsMahasiswa_SiakMahasiswa_IdAndSiakKrsMahasiswa_SiakPeriodeAkademik_IdAndIsDeletedFalse(UUID mahasiswaId, UUID kelasId);

    @Query("SELECT krm FROM KrsRincianMahasiswa as krm where krm.siakKrsMahasiswa.siakMahasiswa.id = :id and krm.siakKrsMahasiswa.siakPeriodeAkademik.namaPeriode = :namaPeriode")
    List<KrsRincianMahasiswa> findByMahasiswaAndByNamaPeriode(@Param("id") UUID id, @Param("namaPeriode") String namaPeriode);

    @Query("""
        SELECT krm
        FROM KrsRincianMahasiswa krm
        JOIN krm.siakKrsMahasiswa km
        JOIN km.siakMahasiswa sm
        WHERE sm.id = :mahasiswaId
          AND krm.isDeleted = false
          AND km.isDeleted = false
    """)
    List<KrsRincianMahasiswa> findAllActiveByMahasiswaId(@Param("mahasiswaId") UUID mahasiswaId);


    @Query("""
    SELECT k FROM KrsRincianMahasiswa k
    WHERE k.siakKrsMahasiswa.siakMahasiswa.id = :mahasiswaId
    AND k.siakKrsMahasiswa.siakPeriodeAkademik.namaPeriode = :namaPeriode
    AND k.isDeleted = false
    """)
    List<KrsRincianMahasiswa> findAllActiveByMahasiswaIdAndPeriodeAkademik(UUID mahasiswaId, String namaPeriode);



    @Query("SELECT krs.siakMahasiswa.id FROM KrsRincianMahasiswa r " +
            "JOIN r.siakKrsMahasiswa krs " +
            "WHERE r.siakKelasKuliah.id = :kelasId AND r.isDeleted = false")
    Set<UUID> findRegisteredMahasiswaIdsByKelasId(@Param("kelasId") UUID kelasId);

    @Query("SELECT krr FROM KrsRincianMahasiswa krr " +
            "JOIN FETCH krr.siakKelasKuliah kk " +
            "JOIN FETCH kk.siakMataKuliah mk " +
            "JOIN FETCH mk.siakTahunKurikulum tk " +
            "WHERE krr.siakKrsMahasiswa.siakMahasiswa.id = :mahasiswaId " +
            "AND krr.siakKrsMahasiswa.siakPeriodeAkademik.namaPeriode = :namaPeriode " +
            "AND krr.isDeleted = false")
    List<KrsRincianMahasiswa> findAllByMahasiswaAndPeriodeWithDetails(UUID mahasiswaId, String namaPeriode);


    @Query("SELECT krr.hurufMutu, SUM(kk.siakMataKuliah.sksTatapMuka + kk.siakMataKuliah.sksPraktikum) " +
            "FROM KrsRincianMahasiswa krr " +
            "JOIN krr.siakKelasKuliah kk " +
            "WHERE krr.siakKrsMahasiswa.siakMahasiswa.id = :mahasiswaId " +
            "AND krr.hurufMutu IS NOT NULL " +
            "GROUP BY krr.hurufMutu")
    List<Object[]> findGradeDistributionBySks(UUID mahasiswaId);

    @Query(value = """
        SELECT
            tk.tahun,
            mk.kode_mata_kuliah,
            mk.nama_mata_kuliah,
            mk.nilai_min AS course_min_grade_letter,
            kk.nama,
            mk.sks_tatap_muka,
            mk.sks_praktikum,
            rkm.nilai AS student_raw_score,
            sp_student.huruf_mutu AS student_letter_grade,
            sp_student.angka_mutu AS student_grade_point,
            sp_course_min.angka_mutu AS course_min_grade_point,
        
            CASE
                WHEN sp_student.angka_mutu IS NULL THEN NULL
                WHEN sp_course_min.angka_mutu IS NULL THEN NULL
                WHEN sp_student.angka_mutu >= sp_course_min.angka_mutu THEN TRUE
                ELSE FALSE
            END AS is_passed,
        
            CASE
                WHEN sp_student.angka_mutu IS NULL THEN 'No student grade assigned'
                WHEN sp_course_min.angka_mutu IS NULL THEN 'No course minimum grade defined for course min letter'
                WHEN sp_student.angka_mutu >= sp_course_min.angka_mutu THEN 'Passed'
                ELSE 'Failed'
            END AS pass_fail_message
        FROM
            siak_rincian_krs_mahasiswa AS rkm
        INNER JOIN
            siak_krs_mahasiswa AS km ON rkm.siak_krs_mahasiswa_id = km.id
        INNER JOIN
            siak_periode_akademik AS pa ON km.siak_periode_akademik_id = pa.id
        INNER JOIN
            siak_kelas_kuliah AS kk ON rkm.siak_kelas_kuliah_id = kk.id
        INNER JOIN
            siak_mata_kuliah AS mk ON kk.siak_mata_kuliah_id = mk.id
        INNER JOIN
            siak_tahun_kurikulum AS tk ON mk.siak_tahun_kurikulum_id = tk.id
        
        LEFT JOIN LATERAL (
            SELECT
                sp_inner.huruf_mutu,
                sp_inner.angka_mutu
            FROM
                siak_skala_penilaian AS sp_inner
            WHERE
                rkm.nilai BETWEEN sp_inner.nilai_min AND sp_inner.nilai_max
            ORDER BY
                sp_inner.nilai_min ASC, sp_inner.nilai_max DESC
            LIMIT 1
        ) AS sp_student ON TRUE
        
        
        LEFT JOIN LATERAL (
            SELECT
                sp_inner.huruf_mutu,
                sp_inner.angka_mutu
            FROM
                siak_skala_penilaian AS sp_inner
            WHERE
                sp_inner.huruf_mutu = mk.nilai_min
            ORDER BY
                sp_inner.nilai_min ASC
            LIMIT 1
        ) AS sp_course_min ON TRUE
        
        WHERE
            km.siak_mahasiswa_id = :id
            AND pa.nama_periode = :namaPeriode;
    """, nativeQuery = true)
    List<Object[]> getSuntingFindByMahasiswaIdAndNamaPeriode(@Param("id") UUID id, @Param("namaPeriode") String namaPeriode);

    @Query(value = "SELECT NEW com.siakad.dto.response.KelasKuliahWithTakenStatusDto(" +
            // KelasKuliah fields (kl)
            "kl.id, kl.nama, kl.kapasitas, kl.sistemKuliah, kl.statusKelas, kl.jumlahPertemuan, kl.tanggalMulai, kl.tanggalSelesai, " +
            // MataKuliah fields (mk)
            "mk.id, mk.semester, mk.kodeMataKuliah, mk.namaMataKuliah, mk.jenisMataKuliah, mk.nilaiMin, mk.sksTatapMuka, mk.sksPraktikum, mk.adaPraktikum, mk.opsiMataKuliah, " +
            // JadwalKuliah fields (jkl)
            "jkl.hari, jkl.jamMulai, jkl.jamSelesai, d.nama, " +
            // PeriodeAkademik (pa) and TahunAjaran (sa) fields
            "pa.id, pa.namaPeriode, sa.tahun, " +
            // ProgramStudi (ps) and TahunKurikulum (tk) fields
            "ps.namaProgramStudi, tk.tahun, " +
            // isTakenByCurrentUser flag (using EXISTS)
            "CASE WHEN EXISTS (" +
            "   SELECT 1 FROM KrsRincianMahasiswa studentKrs " +
            "   JOIN studentKrs.siakKrsMahasiswa studentKrsMhs " +
            "   WHERE studentKrsMhs.siakMahasiswa.id = :idMahasiswa " +
            "   AND studentKrs.siakKelasKuliah.siakMataKuliah.id = mk.id " +
            "   AND studentKrs.isDeleted = FALSE) THEN TRUE ELSE FALSE END, " +
            // lastTakenNilai (this DTO field will hold hurufMutu)
            " (SELECT skrm.hurufMutu FROM KrsRincianMahasiswa skrm " +
            "  JOIN skrm.siakKrsMahasiswa skm " +
            "  WHERE skm.siakMahasiswa.id = :idMahasiswa " +
            "  AND skrm.siakKelasKuliah.siakMataKuliah.id = mk.id " +
            "  AND skrm.isDeleted = FALSE " +
            "  ORDER BY skrm.createdAt DESC " +
            "  LIMIT 1) " +
            ") " +
            "FROM KelasKuliah kl " +
            "JOIN kl.siakMataKuliah mk " +
            "JOIN kl.siakPeriodeAkademik pa " +
            "JOIN pa.siakTahunAjaran sa " +
            "JOIN mk.siakProgramStudi ps " +
            "JOIN mk.siakTahunKurikulum tk " +
            "LEFT JOIN kl.siakJadwalKuliah jkl ON jkl.jenisPertemuan = 'kuliah' AND jkl.isDeleted = FALSE " +
            "LEFT JOIN jkl.siakDosen d " +
            "WHERE kl.isDeleted = FALSE " +
            "AND (:keyword IS NULL OR :keyword = '' OR mk.namaMataKuliah LIKE %:keyword% OR mk.kodeMataKuliah LIKE %:keyword%) " +
            "AND (:semesters IS NULL OR mk.semester IN :semesters) " +
            // UPDATED ORDER BY CLAUSE
            "ORDER BY " +
            "    CASE WHEN " +
            "        (SELECT COUNT(studentKrs.id) FROM KrsRincianMahasiswa studentKrs " +
            "         JOIN studentKrs.siakKrsMahasiswa studentKrsMhs " +
            "         WHERE studentKrsMhs.siakMahasiswa.id = :idMahasiswa " +
            "         AND studentKrs.siakKelasKuliah.siakMataKuliah.id = mk.id " +
            "         AND studentKrs.isDeleted = FALSE) > 0 THEN 1 ELSE 0 END ASC, " + // 3. "Not Taken" first
            "    mk.semester ASC, " +                  // 1. By Course Semester (e.g., 1, 2, 3...)
            "    mk.namaMataKuliah ASC, " +           // 2. By Course Name (alphabetical)
            "    (SELECT skrm.nilaiAkhir FROM KrsRincianMahasiswa skrm JOIN skrm.siakKrsMahasiswa skm " +
            "     WHERE skm.siakMahasiswa.id = :idMahasiswa AND skrm.siakKelasKuliah.siakMataKuliah.id = mk.id " +
            "     AND skrm.isDeleted = FALSE ORDER BY skrm.createdAt DESC LIMIT 1) ASC, " + // 4. Lowest numerical grade
            "    kl.sistemKuliah ASC, " +              // 5. By Class Type/System (e.g., "Reguler" before "Karyawan")
            "    kl.nama ASC"                          // 6. Final tie-breaker by Class Name
            , nativeQuery = false)
    Page<KelasKuliahWithTakenStatusDto> findKelasKuliahWithTakenStatusAndLastNilai(
            @Param("keyword") String keyword,
            @Param("semesters") List<Integer> semesters,
            @Param("idMahasiswa") UUID idMahasiswa,
            Pageable pageable);
}
