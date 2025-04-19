-- SIAK KELUARGA MAHASISWA TABLE --
CREATE TABLE IF NOT EXISTS public.siak_keluarga_mahasiswa (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    siak_mahasiswa_id UUID NOT NULL,
    hubungan VARCHAR(20) NOT NULL,
    nama VARCHAR(255) NOT NULL,
    nik VARCHAR(16) NOT NULL,
    tanggal_lahir DATE NOT NULL,
    status_hidup VARCHAR(10) NOT NULL,
    status_kerabat VARCHAR(10) NOT NULL,
    pendidikan VARCHAR(20) NOT NULL,
    pekerjaan VARCHAR(50) NOT NULL,
    penghasilan VARCHAR(45) NOT NULL,
    alamat TEXT NOT NULL,
    no_telepon VARCHAR(15) NOT NULL,
    email VARCHAR(255) NOT NULL,
    foto_profil BYTEA,
    ijzah_sekolah BYTEA,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (siak_mahasiswa_id) REFERENCES siak_mahasiswa(id)
);

CREATE INDEX IF NOT EXISTS idx_keluarga_mahasiswa_id ON public.siak_keluarga_mahasiswa(siak_mahasiswa_id);
