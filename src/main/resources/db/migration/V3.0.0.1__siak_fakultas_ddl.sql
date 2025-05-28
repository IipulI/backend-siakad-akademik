-- FAKULTAS TABLE --
CREATE TABLE IF NOT EXISTS public.siak_fakultas (
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   nama_fakultas VARCHAR(255) NOT NULL,
   is_deleted bool NULL DEFAULT FALSE,
   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP
);