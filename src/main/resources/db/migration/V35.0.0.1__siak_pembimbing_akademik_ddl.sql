-- SIAK PEMBIMBING AKADEMIK TABLE --
CREATE TABLE IF NOT EXISTS public.siak_pembimbing_akademik (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    siak_dosen_id UUID NOT NULL,
    siak_periode_akademik_id UUID NOT NULL,
    siak_mahasiswa_id UUID NOT NULL,
    no_sk VARCHAR(225) NOT NULL,
    tanggal_sk DATE NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (siak_dosen_id) REFERENCES siak_dosen(id),
    FOREIGN KEY (siak_mahasiswa_id) REFERENCES siak_mahasiswa(id),
    FOREIGN KEY (siak_periode_akademik_id) REFERENCES siak_periode_akademik(id)
);
