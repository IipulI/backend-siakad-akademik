-- SIAK MAHASISWA TABLE --
CREATE TABLE IF NOT EXISTS public.siak_mahasiswa (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    siak_user_id UUID NOT NULL UNIQUE,
    nama VARCHAR(255) NOT NULL,
    npm VARCHAR(45) NOT NULL UNIQUE,
    angkatan VARCHAR(10) NOT NULL,
    status VARCHAR(45) NOT NULL,
    jenis_kelamin VARCHAR(12) NOT NULL,
    tempat_lahir VARCHAR(40) NOT NULL,
    tanggal_lahir DATE NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    no_telepon VARCHAR(15) NOT NULL,
    alamat TEXT NOT NULL,
    agama VARCHAR(10) NOT NULL,
    status_nikah VARCHAR(15) NOT NULL,
    nik VARCHAR(16) NOT NULL,
    no_kk VARCHAR(16) NOT NULL,
    pendidikan_asal VARCHAR(20) NOT NULL,
    nama_pendidikan_asal VARCHAR(255) NOT NULL,
    nisn VARCHAR(15) NOT NULL,
    pekerjaan VARCHAR(45) NOT NULL,
    instansi_pekerjaan VARCHAR(45),
    penghasilan VARCHAR(45),
--     foto_profil BYTEA,
--     ijazah_sekolah BYTEA,
    is_deleted bool NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (siak_user_id) REFERENCES siak_user(id)
);

CREATE INDEX IF NOT EXISTS idx_mahasiswa_user_id ON public.siak_mahasiswa(siak_user_id);
