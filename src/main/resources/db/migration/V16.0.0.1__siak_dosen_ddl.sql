-- SIAK DOSEN TABLE --
CREATE TABLE IF NOT EXISTS public.siak_dosen (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    siak_user_id UUID NOT NULL,
    nama VARCHAR(225) NOT NULL,
    nidn VARCHAR(15) NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (siak_user_id) REFERENCES siak_user(id)
);

CREATE INDEX IF NOT EXISTS idx_dosen_user_id ON public.siak_dosen(siak_user_id);
