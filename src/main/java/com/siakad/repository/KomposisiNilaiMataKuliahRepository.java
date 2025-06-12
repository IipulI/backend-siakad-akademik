package com.siakad.repository;

import com.siakad.entity.KomposisiNilaiMataKuliah;
import com.siakad.entity.KomposisiNilaiMataKuliahId;
import com.siakad.entity.KomposisiPenilaian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KomposisiNilaiMataKuliahRepository extends JpaRepository<KomposisiNilaiMataKuliah, KomposisiNilaiMataKuliahId> {
    List<KomposisiNilaiMataKuliah> findAllBySiakMataKuliah_IdAndIsDeletedFalse(UUID siakMataKuliahId);

    @Query("""
    SELECT k FROM KomposisiNilaiMataKuliah k
    WHERE k.siakMataKuliah.id = :mataKuliahId
      AND k.isDeleted = false
""")
    List<KomposisiNilaiMataKuliah> findByMataKuliahId(@Param("mataKuliahId") UUID mataKuliahId);



    /**
     * Batch-fetches all grading components for a given list of courses.
     * This is crucial for performance.
     */
    @Query("SELECT knm FROM KomposisiNilaiMataKuliah knm " +
            "JOIN FETCH knm.siakKomposisiNilai " +
            "WHERE knm.siakMataKuliah.id IN :mataKuliahIds AND knm.isDeleted = false")
    List<KomposisiNilaiMataKuliah> findAllByMataKuliahIn(List<UUID> mataKuliahIds);
}
