-- PROGRAM STUDI TABLE --
CREATE TABLE IF NOT EXISTS public.siak_program_studi (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    siak_fakultas_id UUID NOT NULL,
    nama_program_studi VARCHAR(255) NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (siak_fakultas_id) REFERENCES siak_fakultas(id)
);

CREATE INDEX IF NOT EXISTS idx_siak_program_studi_id ON public.siak_program_studi(siak_fakultas_id);
