-- SIAK KOMPOSISI PENILAIAN TABLE --
CREATE TABLE IF NOT EXISTS public.siak_komposisi_nilai (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    siak_tahun_kurikulum_id UUID NOT NULL,
    nama VARCHAR(45) NOT NULL,
    persentase NUMERIC(5,2),
    is_deleted bool NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (siak_tahun_kurikulum_id) REFERENCES siak_tahun_kurikulum(id)
);