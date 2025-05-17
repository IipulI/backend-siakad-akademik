-- SIAK CAPAIAN PEMBELJARAN LULUSAN TABLE --
CREATE TABLE IF NOT EXISTS public.siak_capaian_pembelajaran_lulusan (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    siak_program_studi_id UUID NOT NULL,
    siak_tahun_kurikulum_id UUID NOT NULL,
    kode_cpl VARCHAR(20) NOT NULL,
    deskripsi_cpl TEXT NOT NULL,
    kategori_cpl VARCHAR(20) NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (siak_program_studi_id) REFERENCES siak_program_studi(id),
    FOREIGN KEY (siak_tahun_kurikulum_id) REFERENCES siak_tahun_kurikulum(id)
);
