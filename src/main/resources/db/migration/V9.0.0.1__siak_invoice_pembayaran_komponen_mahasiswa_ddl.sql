-- SIAK INVOICE PEMBAYARAN KOMPONEN TABLE  --
CREATE TABLE IF NOT EXISTS public.siak_invoice_pembayaran_komponen (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    siak_invoice_mahasiswa_id UUID NOT NULL,
    siak_invoice_komponen_id UUID NOT NULL,
    tagihan NUMERIC(20, 2),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (siak_invoice_mahasiswa_id) REFERENCES siak_invoice_mahasiswa(id),
    FOREIGN KEY (siak_invoice_komponen_id) REFERENCES siak_invoice_komponen(id)
);