package com.siakad.repository;

import com.siakad.dto.response.RpsDetailResDto;
import com.siakad.entity.Rps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RpsRepository extends JpaRepository<Rps, UUID>,
        JpaSpecificationExecutor<Rps> {
    Optional<Rps> findByIdAndIsDeletedFalse(UUID id);

    @Query(value = """
         SELECT
             rps.id,                    -- 0
             tk.id,                     -- 1
             tk.tahun,                  -- 2
             pa.id,                     -- 3
             pa.nama_periode,           -- 4
             pa.kode_periode,           -- 5
             ps.id,                     -- 6
             ps.nama_program_studi,     -- 7
             j.id,                      -- 8 (Jenjang ID)
             j.nama,                    -- 9 (Jenjang Nama)
             j.jenjang,                 -- 10 (Jenjang Jenjang)
             rps.tanggal_penyusun,      -- 11 (Indices shifted due to new fields)
             rps.deskripsi_mata_kuliah, -- 12
             rps.tujuan_mata_kuliah,    -- 13
             rps.materi_pembelajaran,   -- 14
             rps.pustaka_utama,         -- 15
             rps.pustaka_pendukung      -- 16
         FROM siak_rps as rps
             LEFT JOIN siak_kelas_rps as krps on rps.id = krps.siak_rps_id
             INNER JOIN siak_tahun_kurikulum as tk on rps.siak_tahun_kurikulum_id=tk.id
             INNER JOIN siak_periode_akademik as pa on rps.siak_periode_akademik_id=pa.id
             INNER JOIN public.siak_program_studi ps on rps.siak_program_studi_id = ps.id
             INNER JOIN public.siak_jenjang j on ps.siak_jenjang_id = j.id -- New JOIN for Jenjang table
         WHERE krps.siak_kelas_kuliah_id = (
                 SELECT kk.id
                 FROM siak_kelas_kuliah as kk
                     INNER JOIN public.siak_jadwal_kuliah sjk on kk.id = sjk.siak_kelas_kuliah_id
                 WHERE sjk.siak_dosen_id = :dosenId
                 limit 1
             )
         AND rps.siak_mata_kuliah_id = :id
    """, nativeQuery = true)
    Optional<Object[]> getRpsDetailProjectionByMataKuliahAndDosen(@Param("id") UUID id, @Param("dosenId") UUID dosenId);


    @Query("""
        SELECT new com.siakad.dto.response.RpsDetailResDto(
            r.id,
            tk.id, tk.tahun,
            pa.id, pa.namaPeriode, pa.kodePeriode,
            ps.id, ps.namaProgramStudi,
            j.id, j.nama, j.jenjang,
            r.tanggalPenyusun, r.deskripsiMataKuliah,
            r.tujuanMataKuliah, r.materiPembelajaran,
            r.pustakaUtama, r.pustakaPendukung
        )
        FROM Rps r
        INNER JOIN r.siakTahunKurikulum tk
        INNER JOIN r.siakPeriodeAkademik pa
        INNER JOIN r.siakProgramStudi ps
        INNER JOIN ps.siakJenjang j
        WHERE r.id = :rpsId
    """)
    Optional<RpsDetailResDto> getRpsDetailById(@Param("rpsId") UUID rpsId);
}
