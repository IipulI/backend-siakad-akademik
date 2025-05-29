-- SIAK PENGUMUMAN TABLE --
CREATE TABLE IF NOT EXISTS public.siak_pengumuman (
     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     siak_user_id UUID NOT NULL,
     judul VARCHAR(255) NOT NULL,
     isi TEXT NOT NULL,
     is_active BOOLEAN NOT NULL,
     is_priority BOOLEAN NOT NULL,
     banner BYTEA NOT NULL,
     is_deleted BOOLEAN DEFAULT FALSE,
     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP,
     FOREIGN KEY (siak_user_id) REFERENCES siak_user(id)
);