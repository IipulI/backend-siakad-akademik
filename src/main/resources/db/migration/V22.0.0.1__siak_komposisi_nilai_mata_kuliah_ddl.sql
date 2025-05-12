-- SIAK KOMPOSISI NILAI MATA KULIAH TABLE
CREATE TABLE IF NOT EXISTS public.siak_komposisi_nilai_mata_kuliah (
    siak_mata_kuliah_id UUID NOT NULL,
    siak_komposisi_nilai_id UUID NOT NULL,
    is_deleted bool NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);