-- TAHUN AJARAN TABLE --
CREATE TABLE IF NOT EXISTS public.siak_tahun_ajaran (
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   tahun VARCHAR(20) NOT NULL,
   nama VARCHAR(20) NOT NULL,
   is_deleted bool NULL DEFAULT FALSE,
   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP
);