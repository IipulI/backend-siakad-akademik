ALTER TABLE public.siak_invoice_mahasiswa
ADD COLUMN siak_periode_akademik_id UUID NOT NULL;

ALTER TABLE public.siak_invoice_mahasiswa
ADD FOREIGN KEY (siak_periode_akademik_id) REFERENCES siak_periode_akademik(id);