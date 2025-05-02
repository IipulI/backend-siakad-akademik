-- SIAK USER TABLE --
CREATE TABLE IF NOT EXISTS public.siak_user (
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   username VARCHAR(100) NOT NULL UNIQUE,
   email VARCHAR(100) NOT NULL UNIQUE,
   password VARCHAR(255) NOT NULL,
   is_deleted bool NULL DEFAULT FALSE,
   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP
);

-- SIAK ROLE TABLE --
CREATE TABLE IF NOT EXISTS public.siak_role (
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   nama VARCHAR(30) NOT NULL,
   deskripsi TEXT NOT NULL,
   is_deleted bool NULL DEFAULT FALSE,
   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP
);

-- SIAK USER ROLE JUNCTION TABLE --
CREATE TABLE IF NOT EXISTS public.siak_user_role (
   siak_user_id UUID NOT NULL,
   siak_role_id UUID NOT NULL,
   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP,
   PRIMARY KEY (siak_user_id, siak_role_id),
   FOREIGN KEY (siak_user_id) REFERENCES siak_user(id),
   FOREIGN KEY (siak_role_id) REFERENCES siak_role(id)
);

CREATE INDEX IF NOT EXISTS idx_siak_user_role_user_id ON public.siak_user_role(siak_user_id);
CREATE INDEX IF NOT EXISTS idx_siak_user_role_role_id ON public.siak_user_role(siak_role_id);

-- INSERT SIAK ROLE TABLE --
INSERT INTO public.siak_role (id, nama, deskripsi, is_deleted, created_at, updated_at)
VALUES
    (gen_random_uuid(), 'KEUANGAN_UNIV',  'Role Univ',     FALSE, CURRENT_TIMESTAMP, NULL),
    (gen_random_uuid(), 'KEUANGAN_FAK',   'Role Fakultas', FALSE, CURRENT_TIMESTAMP, NULL),
    (gen_random_uuid(), 'KEUANGAN_PRODI', 'Role Prodi',    FALSE, CURRENT_TIMESTAMP, NULL),
    (gen_random_uuid(), 'AKADEMIK_UNIV',  'Role Univ',     FALSE, CURRENT_TIMESTAMP, NULL),
    (gen_random_uuid(), 'AKADEMIK_FAK',   'Role Fakultas', FALSE, CURRENT_TIMESTAMP, NULL),
    (gen_random_uuid(), 'AKADEMIK_PRODI', 'Role Prodi',    FALSE, CURRENT_TIMESTAMP, NULL),
    (gen_random_uuid(), 'MAHASISWA',      'Mahasiswa',     FALSE, CURRENT_TIMESTAMP, NULL),
    (gen_random_uuid(), 'DOSEN',          'Dosen',         FALSE, CURRENT_TIMESTAMP, NULL);
