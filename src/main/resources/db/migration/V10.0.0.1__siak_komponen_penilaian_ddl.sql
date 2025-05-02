-- SIAK KOMPONEN PENILAIAN TABLE --
CREATE TABLE IF NOT EXISTS public.siak_komponen_penilaian (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nama_komponen VARCHAR(45) NOT NULL,
    nilai_komponen DOUBLE PRECISION,
    is_deleted bool NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);