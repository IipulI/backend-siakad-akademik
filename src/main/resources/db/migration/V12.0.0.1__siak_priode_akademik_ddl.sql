-- SIAK PRIODE AKADEMIK TABLE --
CREATE TABLE IF NOT EXISTS public.siak_periode_akademik (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    siak_tahun_ajaran_id UUID NOT NULL,
    nama_periode VARCHAR(100) NOT NULL,
    kode_periode VARCHAR(20) NOT NULL,
    jenis VARCHAR(10) NOT NULL,
    tanggal_mulai DATE NOT NULL,
    tanggal_selesai DATE NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (siak_tahun_ajaran_id) REFERENCES siak_tahun_ajaran(id)
);