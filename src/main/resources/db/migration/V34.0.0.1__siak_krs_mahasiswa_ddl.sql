-- SIAK KRS MAHASISWA TABLE --
CREATE TABLE IF NOT EXISTS public.siak_krs_mahasiswa (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    siak_mahasiswa_id UUID NOT NULL,
    siak_periode_akademik_id UUID NOT NULL,
    status VARCHAR(45) NOT NULL,
    jumlah_sks_diambil INT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (siak_mahasiswa_id) REFERENCES siak_mahasiswa(id),
    FOREIGN KEY (siak_periode_akademik_id) REFERENCES siak_periode_akademik(id)
);

-- SIAK RINCIAN KRS MAHASISWA TABLE --
CREATE TABLE IF NOT EXISTS public.siak_rincian_krs_mahasiswa (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    siak_krs_mahasiswa_id UUID NOT NULL,
    siak_kelas_kuliah_id UUID NOT NULL,
    kategori VARCHAR(10) NOT NULL,
    status VARCHAR(15),
    kehadiran NUMERIC(5,2),
    tugas NUMERIC(5,2),
    uts NUMERIC(5,2),
    uas NUMERIC(5,2),
    nilai NUMERIC(5,2),
    huruf_mutu VARCHAR(5),
    angka_mutu NUMERIC(5,2),
    nilai_akhir NUMERIC(5,2),
    FOREIGN KEY (siak_krs_mahasiswa_id) REFERENCES siak_krs_mahasiswa(id),
    FOREIGN KEY (siak_kelas_kuliah_id) REFERENCES siak_kelas_kuliah(id)
);

-- TAMBAH RELASI KE SIAK INVOICE PEMBAYARAN KOMPONEN TABLE --
ALTER TABLE public.siak_invoice_pembayaran_komponen
    ADD COLUMN siak_krs_mahasiswa_id UUID,
    ADD COLUMN siak_rincian_krs_mahasiswa_id UUID;

ALTER TABLE public.siak_invoice_pembayaran_komponen
    ADD FOREIGN KEY (siak_krs_mahasiswa_id) REFERENCES siak_krs_mahasiswa(id),
    ADD FOREIGN KEY (siak_rincian_krs_mahasiswa_id) REFERENCES siak_rincian_krs_mahasiswa(id);