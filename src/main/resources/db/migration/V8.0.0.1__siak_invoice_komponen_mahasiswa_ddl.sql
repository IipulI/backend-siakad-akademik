-- SIAK INVOICE KOMPONEN MAHASISWA TABLE --
CREATE TABLE IF NOT EXISTS public.siak_invoice_komponen_mahasiswa (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    kode_komponen VARCHAR(10) NOT NULL,
    nama VARCHAR(40),
    nominal NUMERIC(20, 2),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);
