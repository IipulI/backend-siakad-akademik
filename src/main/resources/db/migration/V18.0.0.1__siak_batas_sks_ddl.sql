-- SIAK BATAS SKS TABLE --
CREATE TABLE IF NOT EXISTS public.siak_batas_sks (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    siak_jenjang_id UUID NOT NULL,
    ips_min NUMERIC(3,2) NOT NULL,
    ips_max NUMERIC(3,2) NOT NULL,
    batas_sks INT NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (siak_jenjang_id) REFERENCES siak_jenjang(id)
);
