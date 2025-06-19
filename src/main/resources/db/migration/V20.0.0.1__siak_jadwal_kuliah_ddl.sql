-- SIAK JADWAL KULIAH TABLE --
CREATE TABLE IF NOT EXISTS public.siak_jadwal_kuliah (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    siak_dosen_id UUID NULL,
    siak_kelas_kuliah_id UUID NOT NULL,
    siak_ruangan_id UUID NOT NULL,
    jam_mulai TIME NOT NULL,
    jam_selesai TIME NOT NULL,
    hari VARCHAR(10) NOT NULL,
    jenis_pertemuan VARCHAR(15) NOT NULL,
    metode_pembelajaran VARCHAR(10) NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (siak_dosen_id) REFERENCES siak_dosen(id),
    FOREIGN KEY (siak_kelas_kuliah_id) REFERENCES siak_kelas_kuliah(id),
    FOREIGN KEY (siak_ruangan_id) REFERENCES siak_ruangan(id)
);