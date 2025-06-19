package com.siakad.repository;

import com.siakad.dto.response.PesertaKelas;
import com.siakad.entity.JadwalKuliah;
import com.siakad.entity.KelasKuliah;
import com.siakad.entity.KrsRincianMahasiswa;
import com.siakad.entity.Mahasiswa;
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
            "AND kr.siakKrsMahasiswa.siakPeriodeAkademik.id = :periodeId " +
            "AND kr.isDeleted = false")
     List<KrsRincianMahasiswa> findBySiakKrsMahasiswaSiakMahasiswaIdAndSiakKrsMahasiswaSiakPeriodeAkademikIdAndIsDeletedFalse(
            @Param("mahasiswaId") UUID mahasiswaId, @Param("periodeId") UUID periodeId);

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


    @Query("SELECT krs.siakMahasiswa.id FROM KrsRincianMahasiswa r " +
            "JOIN r.siakKrsMahasiswa krs " +
            "WHERE r.siakKelasKuliah.id = :kelasId AND r.isDeleted = false")
    Set<UUID> findRegisteredMahasiswaIdsByKelasId(@Param("kelasId") UUID kelasId);

    @Query("SELECT krr FROM KrsRincianMahasiswa krr " +
            "JOIN FETCH krr.siakKelasKuliah kk " +
            "JOIN FETCH kk.siakMataKuliah mk " +
            "JOIN FETCH mk.siakTahunKurikulum tk " +
            "WHERE krr.siakKrsMahasiswa.siakMahasiswa.id = :mahasiswaId " +
            "AND krr.siakKrsMahasiswa.siakPeriodeAkademik.id = :periodeAkademikId " +
            "AND krr.isDeleted = false")
    List<KrsRincianMahasiswa> findAllByMahasiswaAndPeriodeWithDetails(UUID mahasiswaId, UUID periodeAkademikId);


    @Query("SELECT krr.hurufMutu, SUM(kk.siakMataKuliah.sksTatapMuka + kk.siakMataKuliah.sksPraktikum) " +
            "FROM KrsRincianMahasiswa krr " +
            "JOIN krr.siakKelasKuliah kk " +
            "WHERE krr.siakKrsMahasiswa.siakMahasiswa.id = :mahasiswaId " +
            "AND krr.hurufMutu IS NOT NULL " +
            "GROUP BY krr.hurufMutu")
    List<Object[]> findGradeDistributionBySks(UUID mahasiswaId);
}
