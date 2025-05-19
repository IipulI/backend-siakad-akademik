package com.siakad.repository;

import com.siakad.dto.response.MataKuliahCpmkMappingDto;
import com.siakad.entity.MataKuliah;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MataKuliahRepository extends JpaRepository<MataKuliah, UUID>,
        JpaSpecificationExecutor<MataKuliah> {
    Optional<MataKuliah> findByIdAndIsDeletedFalse(UUID id);

    @Query(value = """
    SELECT 
        mk.id AS mataKuliahId,
        mk.kode_mata_kuliah AS kodeMataKuliah,
        mk.nama_mata_kuliah AS namaMataKuliah,
        t.tahun AS tahunKurikulum,
        CASE 
            WHEN COUNT(cpmk.id) > 0 THEN true
            ELSE false
        END AS hasCpmk
    FROM siak_mata_kuliah mk
    LEFT JOIN siak_tahun_kurikulum t ON mk.siak_tahun_kurikulum_id = t.id
    LEFT JOIN siak_capaian_mata_kuliah cpmk 
        ON mk.id = cpmk.siak_mata_kuliah_id 
        AND cpmk.is_deleted = false
    WHERE mk.is_deleted = false
      AND (:tahunKurikulum IS NULL OR t.tahun = :tahunKurikulum)
    GROUP BY mk.id, mk.kode_mata_kuliah, mk.nama_mata_kuliah, t.tahun
    """, nativeQuery = true)
    List<MataKuliahCpmkMappingDto> findAllMataKuliahWithCpmkStatus(@Param("tahunKurikulum") String tahunKurikulum);
}
