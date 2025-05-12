-- SIAK RPS TABLE --
CREATE TABLE IF NOT EXISTS public.siak_rps (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    siak_mata_kuliah_id UUID NOT NULL,
    siak_program_studi_id UUID NOT NULL,
    siak_tahun_kurikulum_id UUID NOT NULL,
    tanggal_penyusun DATE NOT NULL,
    deskripsi_mata_kuliah TEXT NOT NULL,
    tujuan_mata_kuliah TEXT NOT NULL,
    materi_pembelajaran TEXT NOT NULL,
    pustaka_utama TEXT NOT NULL,
    pustaka_pendukung TEXT NOT NULL,
    dokumen_rps BYTEA NOT NULL,
    is_deleted bool NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (siak_tahun_kurikulum_id) REFERENCES siak_tahun_kurikulum(id),
    FOREIGN KEY (siak_mata_kuliah_id) REFERENCES siak_mata_kuliah(id),
    FOREIGN KEY (siak_program_studi_id) REFERENCES siak_program_studi(id)
);