-- SIAK JENJANG TABLE --
CREATE TABLE IF NOT EXISTS public.siak_jenjang (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nama VARCHAR(45) NOT NULL,
    jenjang VARCHAR(5) NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);
