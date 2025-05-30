-- SIAK HASIL STUDI TABLE --
CREATE TABLE IF NOT EXISTS public.siak_hasil_studi (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  siak_mahasiswa_id UUID NOT NULL,
  siak_periode_akademik_id UUID NOT NULL,
  semester INT NOT NULL,
  ips NUMERIC(5,2) NOT NULL,
  ipk NUMERIC(5,2) NOT NULL,
  sks_diambil INT NOT NULL,
  sks_lulus INT NOT NULL,
  is_deleted BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP,
  FOREIGN KEY (siak_mahasiswa_id) REFERENCES siak_mahasiswa(id),
  FOREIGN KEY (siak_periode_akademik_id) REFERENCES siak_periode_akademik(id)
);