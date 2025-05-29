-- SIAK SKALA PENILAIAN TABLE --
CREATE TABLE IF NOT EXISTS public.siak_skala_penilaian (
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   siak_program_studi_id UUID NOT NULL,
   siak_tahun_ajaran_id UUID NOT NULL,
   angka_mutu VARCHAR(5) NOT NULL,
   nilai_mutu NUMERIC(5,2) NOT NULL,
   nilai_min NUMERIC(5,2) NOT NULL,
   nilai_max NUMERIC(5,2) NOT NULL,
   is_deleted bool NULL DEFAULT FALSE,
   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP,
   FOREIGN KEY (siak_tahun_ajaran_id) REFERENCES siak_tahun_ajaran(id),
   FOREIGN KEY (siak_program_studi_id) REFERENCES siak_program_studi(id)
);