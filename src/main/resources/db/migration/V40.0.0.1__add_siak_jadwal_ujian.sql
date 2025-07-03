CREATE TABLE IF NOT EXISTS public.siak_jadwal_ujian(
    id UUID PRIMARY KEY DEFAULT  gen_random_uuid(),
    siak_dosen_id UUID NULL,
    siak_kelas_kuliah_id UUID NULL,
    siak_ruangan_id UUID NULL,
    tanggal DATE NOT NULL,
    jam_mulai TIME NOT NULL,
    jam_selesai TIME NOT NULL,
    jenis_ujian VARCHAR(50) NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (siak_dosen_id) REFERENCES siak_dosen(id),
    FOREIGN KEY (siak_kelas_kuliah_id) REFERENCES siak_kelas_kuliah(id),
    FOREIGN KEY (siak_ruangan_id) REFERENCES siak_ruangan(id)
)
