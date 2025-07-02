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
    List<JadwalKuliah> findJadwalKuliahByKelasIdAndSiakDosenIdAndIsDeletedFalse(@Param("id") UUID kelasKuliahId, @Param("dosenId") UUID dosenId);

    List<JadwalKuliah> findBySiakKelasKuliahId(UUID siakKelasKuliahId);

    List<JadwalKuliah> findByHariIgnoreCaseAndSiakKelasKuliah_SiakPeriodeAkademik_IdAndIsDeletedFalse(
            String hari, UUID siakPeriodeAkademikId);


    @Query("SELECT j FROM JadwalKuliah j " +
            "JOIN FETCH j.siakKelasKuliah skk " +
            "JOIN FETCH skk.siakMataKuliah " +
            "JOIN FETCH j.siakRuangan " +
            "JOIN FETCH j.siakDosen " + // Assuming Dosen entity exists
            "WHERE skk.id IN :kelasKuliahIds AND j.isDeleted = false")
    List<JadwalKuliah> findBySiakKelasKuliahIdInAndIsDeletedFalse(
            @Param("kelasKuliahIds") List<UUID> kelasKuliahIds
    );

    /**
     * Finds all schedule entries for a given list of KelasKuliah IDs.
     * Using JOIN FETCH eagerly loads related entities in a single query to prevent
     * the N+1 query problem when accessing them later in a loop.
     * @param kelasKuliahIds A list of KelasKuliah UUIDs.
     * @return A list of JadwalKuliah entities with their relations pre-fetched.
     */
    @Query("SELECT j FROM JadwalKuliah j " +
            "JOIN FETCH j.siakKelasKuliah skk " +
            "JOIN FETCH skk.siakMataKuliah " +
            "JOIN FETCH j.siakRuangan " +
            "JOIN FETCH j.siakDosen " +
            "WHERE skk.id IN :kelasKuliahIds AND j.isDeleted = false")
    List<JadwalKuliah> findBySiakKelasKuliahIdInAndIsDeletedFalseFetchingRelations(
            @Param("kelasKuliahIds") List<UUID> kelasKuliahIds
    );


    /**
     * Finds all schedule entries for a given list of KelasKuliah IDs on a specific day.
     * Also uses JOIN FETCH for performance and compares the day case-insensitively.
     * @param kelasKuliahIds A list of KelasKuliah UUIDs.
     * @param hari The day of the week to filter by (e.g., "Senin").
     * @return A list of JadwalKuliah entities for that day.
     */
    @Query("SELECT j FROM JadwalKuliah j " +
            "JOIN FETCH j.siakKelasKuliah skk " +
            "JOIN FETCH skk.siakMataKuliah " +
            "JOIN FETCH j.siakRuangan " +
            "JOIN FETCH j.siakDosen " +
            "WHERE skk.id IN :kelasKuliahIds " +
            "AND lower(j.hari) = lower(:hari) " +
            "AND j.isDeleted = false")
    List<JadwalKuliah> findByKelasKuliahIdsAndHari(
            @Param("kelasKuliahIds") List<UUID> kelasKuliahIds,
            @Param("hari") String hari
    );

    /**
     * Finds all schedule entries on current Dosen id.
     * Also uses JOIN FETCH for performance and compares the day case-insensitively.
     * @param dosenId ID of Dosen
     * @param namaPeriode Name of Periode Akademik
     * @return A list of JadwalKuliah entities for that day.
     */
    @Query("SELECT j FROM JadwalKuliah j " +
            "JOIN j.siakKelasKuliah kk " +
            "WHERE j.siakDosen.id = :dosenId " +
            "AND kk.siakPeriodeAkademik.namaPeriode = :namaPeriode " +
            "AND j.isDeleted = false")
    List<JadwalKuliah> getjadwalKuliahDosenMingguan(
            @Param("dosenId") UUID dosenId,
            @Param("namaPeriode") String namaPeriode);

    /**
     * Finds all schedule entries on a specific day and current Dosen id.
     * Also uses JOIN FETCH for performance and compares the day case-insensitively.
     * @param dosenId ID of Dosen
     * @param namaPeriode Name of Periode Akademik
     * @param hari The day of the week to filter by (e.g., "Senin").
     * @return A list of JadwalKuliah entities for that day.
     */
    @Query("SELECT j FROM JadwalKuliah j " +
            "JOIN j.siakKelasKuliah kk " +
            "WHERE j.siakDosen.id = :dosenId " +
            "AND kk.siakPeriodeAkademik.namaPeriode = :namaPeriode " +
            "AND lower(j.hari) = lower(:hari) " +
            "AND j.isDeleted = false")
    List<JadwalKuliah> getjadwalKuliahDosenHarian(
            @Param("dosenId") UUID dosenId,
            @Param("namaPeriode") String namaPeriode,
            @Param("hari") String hari
    );

    Optional<JadwalKuliah> findBySiakKelasKuliahIdAndIsDeletedFalse(UUID kelasId);
}
