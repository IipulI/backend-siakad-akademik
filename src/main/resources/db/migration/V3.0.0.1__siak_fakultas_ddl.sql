-- FAKULTAS TABLE --
CREATE TABLE IF NOT EXISTS public.siak_fakultas (
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   nama_fakultas VARCHAR(255) NOT NULL,
   is_deleted bool NULL DEFAULT FALSE,
   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP
);

-- INSERT FAKULTAS
INSERT INTO public.siak_fakultas (id, nama_fakultas) VALUES
   ('00000000-0000-0000-0000-000000000001', 'Fakultas Agama Islam'),
   ('00000000-0000-0000-0000-000000000002', 'Fakultas Keguruan dan Ilmu Pendidikan'),
   ('00000000-0000-0000-0000-000000000003', 'Fakultas Hukum'),
   ('00000000-0000-0000-0000-000000000004', 'Fakultas Ekonomi dan Bisnis'),
   ('00000000-0000-0000-0000-000000000005', 'Fakultas Ilmu Kesehatan'),
   ('00000000-0000-0000-0000-000000000006', 'Fakultas Teknik dan Sains');
