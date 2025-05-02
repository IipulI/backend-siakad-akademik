-- SIAK INVOICE MAHASISWA TABLE --
CREATE TABLE IF NOT EXISTS public.siak_invoice_mahasiswa (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    siak_mahasiswa_id UUID NOT NULL,
    kode_invoice VARCHAR(15) NOT NULL,
    total_tagihan NUMERIC(20, 2) NOT NULL,
    tanggal_tenggat DATE NOT NULL,
    status VARCHAR(10) NOT NULL,
    tahap VARCHAR(10) NOT NULL,
    total_bayar NUMERIC(20, 2),
    tanggal_bayar DATE,
    metode_bayar VARCHAR(15),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (siak_mahasiswa_id) REFERENCES siak_mahasiswa(id)
);