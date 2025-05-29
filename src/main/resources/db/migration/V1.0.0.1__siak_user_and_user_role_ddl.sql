-- CREATE EXTENSION IF NOT EXISTS pgcrypto;

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
    ('00000000-0000-0000-0001-000000000001', 'KEUANGAN_UNIV',  'Role Univ',     FALSE, CURRENT_TIMESTAMP, NULL),
    ('00000000-0000-0000-0002-000000000002', 'KEUANGAN_FAK',   'Role Fakultas', FALSE, CURRENT_TIMESTAMP, NULL),
    ('00000000-0000-0000-0003-000000000003', 'KEUANGAN_PRODI', 'Role Prodi',    FALSE, CURRENT_TIMESTAMP, NULL),
    ('00000000-0000-0000-0004-000000000004', 'AKADEMIK_UNIV',  'Role Univ',     FALSE, CURRENT_TIMESTAMP, NULL),
    ('00000000-0000-0000-0005-000000000005', 'AKADEMIK_FAK',   'Role Fakultas', FALSE, CURRENT_TIMESTAMP, NULL),
    ('00000000-0000-0000-0006-000000000006', 'AKADEMIK_PRODI', 'Role Prodi',    FALSE, CURRENT_TIMESTAMP, NULL),
    ('00000000-0000-0000-0007-000000000007', 'MAHASISWA',      'Mahasiswa',     FALSE, CURRENT_TIMESTAMP, NULL),
    ('00000000-0000-0000-0008-000000000008', 'DOSEN',          'Dosen',         FALSE, CURRENT_TIMESTAMP, NULL);

INSERT INTO public.siak_user (id, username, email, password, is_deleted, created_at, updated_at)
VALUES
    ('00000000-0000-0001-0000-000000000001', 'adminakademikuniv', 'adminakademikuniv@mail.co', '$2a$12$SG.ciE5DBctBtbLMO1uSxOA7KydfUdoL731lLrGKhTDAwuHMeTSju', false, current_timestamp, null),
    ('00000000-0000-0002-0000-000000000002', 'adminakademikfakultas', 'adminakademikfakultas@mail.co', '$2a$12$SG.ciE5DBctBtbLMO1uSxOA7KydfUdoL731lLrGKhTDAwuHMeTSju', false, current_timestamp, null),
    ('00000000-0000-0003-0000-000000000003', 'adminakademikprodi', 'adminakademikprodi@mail.co', '$2a$12$SG.ciE5DBctBtbLMO1uSxOA7KydfUdoL731lLrGKhTDAwuHMeTSju', false, current_timestamp, null),
    ('00000000-0000-0004-0000-000000000004', 'adminkeuanganuniv', 'adminkeuanganuniv@mail.co', '$2a$12$SG.ciE5DBctBtbLMO1uSxOA7KydfUdoL731lLrGKhTDAwuHMeTSju', false, current_timestamp, null),
    ('00000000-0000-0005-0000-000000000005', 'adminkeuanganfakultas', 'adminkeuanganfakultas@mail.co', '$2a$12$SG.ciE5DBctBtbLMO1uSxOA7KydfUdoL731lLrGKhTDAwuHMeTSju', false, current_timestamp, null),
    ('00000000-0000-0006-0000-000000000006', 'adminkeuanganprodi', 'adminkeuanganprodi@mail.co', '$2a$12$SG.ciE5DBctBtbLMO1uSxOA7KydfUdoL731lLrGKhTDAwuHMeTSju', false, current_timestamp, null),
    ('00000000-0000-0007-0000-000000000007', 'mahasiswa', 'mahasiswa@mail.co', '$2a$12$SG.ciE5DBctBtbLMO1uSxOA7KydfUdoL731lLrGKhTDAwuHMeTSju', false, current_timestamp, null),
    ('00000000-0000-0008-0000-000000000008', 'dosen', 'dosen@mail.co', '$2a$12$SG.ciE5DBctBtbLMO1uSxOA7KydfUdoL731lLrGKhTDAwuHMeTSju', false, current_timestamp, null);

INSERT INTO public.siak_user_role (siak_user_id, siak_role_id, created_at, updated_at)
VALUES
    ('00000000-0000-0001-0000-000000000001', '00000000-0000-0000-0004-000000000004', current_timestamp, null),
    ('00000000-0000-0002-0000-000000000002', '00000000-0000-0000-0005-000000000005', current_timestamp, null),
    ('00000000-0000-0003-0000-000000000003', '00000000-0000-0000-0006-000000000006', current_timestamp, null),
    ('00000000-0000-0004-0000-000000000004', '00000000-0000-0000-0001-000000000001', current_timestamp, null),
    ('00000000-0000-0005-0000-000000000005', '00000000-0000-0000-0002-000000000002', current_timestamp, null),
    ('00000000-0000-0006-0000-000000000006', '00000000-0000-0000-0003-000000000003', current_timestamp, null),
    ('00000000-0000-0007-0000-000000000007', '00000000-0000-0000-0007-000000000007', current_timestamp, null),
    ('00000000-0000-0008-0000-000000000008', '00000000-0000-0000-0008-000000000008', current_timestamp, null);
