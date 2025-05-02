-- PROGRAM STUDI TABLE --
CREATE TABLE IF NOT EXISTS public.siak_program_studi (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    siak_fakultas_id UUID NOT NULL,
    nama_program_studi VARCHAR(255) NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (siak_fakultas_id) REFERENCES siak_fakultas(id)
);

CREATE INDEX IF NOT EXISTS idx_siak_program_studi_id ON public.siak_program_studi(siak_fakultas_id);

-- Program Studi untuk Fakultas Agama Islam --
INSERT INTO public.siak_program_studi (siak_fakultas_id, nama_program_studi) VALUES
     ('00000000-0000-0000-0000-000000000001', 'Komunikasi dan Penyiaran Islam'),
     ('00000000-0000-0000-0000-000000000001', 'Hukum Keluarga Islam'),
     ('00000000-0000-0000-0000-000000000001', 'Pendidikan Agama Islam'),
     ('00000000-0000-0000-0000-000000000001', 'Ekonomi Syariah'),
     ('00000000-0000-0000-0000-000000000001', 'Pendidikan Guru Madrasah Ibtidaiyah'),
     ('00000000-0000-0000-0000-000000000001', 'Bimbingan dan Konseling Pendidikan Islam');

-- Program Studi untuk Fakultas Keguruan dan Ilmu Pendidikan --
INSERT INTO public.siak_program_studi (siak_fakultas_id, nama_program_studi) VALUES
     ('00000000-0000-0000-0000-000000000002', 'Pendidikan Masyarakat'),
     ('00000000-0000-0000-0000-000000000002', 'Pendidikan Bahasa Inggris'),
     ('00000000-0000-0000-0000-000000000002', 'Teknologi Pendidikan'),
     ('00000000-0000-0000-0000-000000000002', 'Pendidikan Vokasional Desain Fashion');

-- Program Studi untuk Fakultas Hukum --
INSERT INTO public.siak_program_studi (siak_fakultas_id, nama_program_studi) VALUES
    ('00000000-0000-0000-0000-000000000003', 'Ilmu Hukum');

-- Program Studi untuk Fakultas Ekonomi dan Bisnis --
INSERT INTO public.siak_program_studi (siak_fakultas_id, nama_program_studi) VALUES
    ('00000000-0000-0000-0000-000000000004', 'Manajemen'),
    ('00000000-0000-0000-0000-000000000004', 'Akuntansi'),
    ('00000000-0000-0000-0000-000000000004', 'Keuangan Perbankan'),
    ('00000000-0000-0000-0000-000000000004', 'Ekonomi Syariah');

-- Program Studi untuk Fakultas Ilmu Kesehatan --
INSERT INTO public.siak_program_studi (siak_fakultas_id, nama_program_studi) VALUES
    ('00000000-0000-0000-0000-000000000005', 'Kesehatan Masyarakat');

-- Program Studi untuk Fakultas Teknik dan Sains --
INSERT INTO public.siak_program_studi (siak_fakultas_id, nama_program_studi) VALUES
     ('00000000-0000-0000-0000-000000000006', 'Teknik Informatika'),
     ('00000000-0000-0000-0000-000000000006', 'Teknik Sipil'),
     ('00000000-0000-0000-0000-000000000006', 'Teknik Mesin'),
     ('00000000-0000-0000-0000-000000000006', 'Teknik Elektro');
