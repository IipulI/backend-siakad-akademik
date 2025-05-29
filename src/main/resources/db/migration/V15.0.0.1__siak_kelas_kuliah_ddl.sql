-- SIAK KELAS KULIAH TABLE --
CREATE TABLE IF NOT EXISTS public.siak_kelas_kuliah (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    siak_mata_kuliah_id UUID NOT NULL,
    siak_program_studi_id UUID NOT NULL,
    siak_periode_akademik_id UUID NOT NULL,
    nama VARCHAR(45) NOT NULL,
    kapasitas INT NOT NULL,
    sistem_kuliah VARCHAR(45) NOT NULL,
    status_kelas VARCHAR(10) NOT NULL,
    jumlah_pertemuan INT NOT NULL,
    tanggal_mulai DATE NOT NULL,
    tanggal_selesai DATE NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (siak_mata_kuliah_id) REFERENCES siak_mata_kuliah(id),
    FOREIGN KEY (siak_program_studi_id) REFERENCES siak_program_studi(id),
    FOREIGN KEY (siaK_periode_akademik_id) REFERENCES siak_periode_akademik(id)
);