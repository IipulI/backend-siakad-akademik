-- SIAK RUANGAN TABLE --
CREATE TABLE IF NOT EXISTS public.siak_ruangan (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    siak_program_studi_id UUID NOT NULL,
    siak_fakultas_id UUID,
    nama_ruangan VARCHAR(15) NOT NULL,
    kapasitas INT NOT NULL,
    lantai VARCHAR(5) NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (siak_program_studi_id) REFERENCES siak_program_studi(id),
    FOREIGN KEY (siak_fakultas_id) REFERENCES siak_fakultas(id)
);