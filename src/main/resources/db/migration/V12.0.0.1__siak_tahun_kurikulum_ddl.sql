-- SIAK TAHUN KURIKULUM TABLE --
CREATE TABLE IF NOT EXISTS public.siak_tahun_kurikulum (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  siak_periode_akademik_id UUID NOT NULL,
  keterangan VARCHAR(255) NOT NULL,
  tahun VARCHAR(10) NOT NULL,
  tanggal_mulai DATE NOT NULL,
  tanggal_selesai DATE NOT NULL,
  is_deleted BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP,
  FOREIGN KEY (siak_periode_akademik_id) REFERENCES siak_periode_akademik(id)
);