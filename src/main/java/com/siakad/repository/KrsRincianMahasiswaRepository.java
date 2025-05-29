package com.siakad.repository;

import com.siakad.dto.response.PesertaKelas;
import com.siakad.entity.JadwalKuliah;
import com.siakad.entity.KrsRincianMahasiswa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
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

    @Query(value = """
    SELECT j.* FROM siak_jadwal_kuliah j
    JOIN siak_kelas_kuliah kk ON j.kelas_kuliah_id = kk.id
    JOIN siak_rincian_krs_mahasiswa krm ON krm.siak_kelas_kuliah_id = kk.id
    WHERE krm.id = :krsId
    """, nativeQuery = true)
    List<JadwalKuliah> findJadwalByKrsIdNative(@Param("krsId") UUID krsRincianId);

    @Query("SELECT k FROM KrsRincianMahasiswa k where k.siakKelasKuliah.id=:kelasId AND k.isDeleted=false")
    List<KrsRincianMahasiswa> findPesertaByKelasIdAndIsDeletedFalse(@Param("kelasId") UUID kelasId);

    @Query("SELECT k FROM KrsRincianMahasiswa k WHERE k.siakKrsMahasiswa.id = :krsMahasiswaId AND k.siakKelasKuliah.id = :kelasId AND k.isDeleted = false AND (k.status IS NULL OR k.status = '' OR k.status = 'Tidak Lulus')")
    Optional<KrsRincianMahasiswa> findFirstByKrsMahasiswaIdAndKelasIdAndStatusNullOrEmpty(@Param("krsMahasiswaId") UUID krsMahasiswaId, @Param("kelasId") UUID kelasId);

    boolean existsByStatus(String status);
}
