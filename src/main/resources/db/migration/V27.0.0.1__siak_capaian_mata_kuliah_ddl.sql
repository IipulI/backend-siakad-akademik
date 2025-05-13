-- SIAK CAPAIAN MATA KULIAH TABLE --
CREATE TABLE IF NOT EXISTS public.siak_capaian_mata_kuliah (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    siak_mata_kuliah_id UUID NOT NULL,
    kode_cpmk VARCHAR(20) NOT NULL,
    deskripsi_cpmk TEXT NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (siak_mata_kuliah_id) REFERENCES siak_mata_kuliah(id)
);
