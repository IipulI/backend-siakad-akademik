ALTER TABLE public.siak_program_studi
    ADD COLUMN siak_jenjang_id UUID NOT NULL;

ALTER TABLE public.siak_program_studi
    ADD FOREIGN KEY (siak_jenjang_id) REFERENCES siak_jenjang(id);


-- DATA FAKULTAS
INSERT INTO public.siak_fakultas (id, nama_fakultas) VALUES
 ('00000000-0000-0000-0000-000000000001', 'Fakultas Agama Islam'),
 ('00000000-0000-0000-0000-000000000002', 'Fakultas Keguruan dan Ilmu Pendidikan'),
 ('00000000-0000-0000-0000-000000000003', 'Fakultas Hukum'),
 ('00000000-0000-0000-0000-000000000004', 'Fakultas Ekonomi dan Bisnis'),
 ('00000000-0000-0000-0000-000000000005', 'Fakultas Ilmu Kesehatan'),
 ('00000000-0000-0000-0000-000000000006', 'Fakultas Teknik dan Sains');

INSERT INTO public.siak_jenjang (id, nama, jenjang) VALUES
 ('00000000-0000-0000-0000-000000000010', 'Diploma 3', 'D3'),
 ('00000000-0000-0000-0000-000000000011', 'Sarjana', 'S1'),
 ('00000000-0000-0000-0000-000000000012', 'Magister', 'S2');


-- Fakultas Agama Islam (S1)
INSERT INTO public.siak_program_studi (siak_fakultas_id, siak_jenjang_id, nama_program_studi) VALUES
  ('00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000011', 'Komunikasi dan Penyiaran Islam'),
  ('00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000011', 'Hukum Keluarga Islam'),
  ('00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000011', 'Pendidikan Agama Islam'),
  ('00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000011', 'Ekonomi Syariah'),
  ('00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000011', 'Pendidikan Guru Madrasah Ibtidaiyah'),
  ('00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000011', 'Bimbingan dan Konseling Pendidikan Islam');

-- Fakultas Keguruan dan Ilmu Pendidikan (S1)
INSERT INTO public.siak_program_studi (siak_fakultas_id, siak_jenjang_id, nama_program_studi) VALUES
  ('00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000011', 'Pendidikan Masyarakat'),
  ('00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000011', 'Pendidikan Bahasa Inggris'),
  ('00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000011', 'Teknologi Pendidikan'),
  ('00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000011', 'Pendidikan Vokasional Desain Fashion');

-- Fakultas Hukum (S1)
INSERT INTO public.siak_program_studi (siak_fakultas_id, siak_jenjang_id, nama_program_studi) VALUES
  ('00000000-0000-0000-0000-000000000003', '00000000-0000-0000-0000-000000000011', 'Ilmu Hukum');

-- Fakultas Ekonomi dan Bisnis
INSERT INTO public.siak_program_studi (siak_fakultas_id, siak_jenjang_id, nama_program_studi) VALUES
  ('00000000-0000-0000-0000-000000000004', '00000000-0000-0000-0000-000000000011', 'Manajemen'),
  ('00000000-0000-0000-0000-000000000004', '00000000-0000-0000-0000-000000000011', 'Akuntansi'),
  ('00000000-0000-0000-0000-000000000004', '00000000-0000-0000-0000-000000000010', 'Keuangan Perbankan'), -- D3
  ('00000000-0000-0000-0000-000000000004', '00000000-0000-0000-0000-000000000011', 'Ekonomi Syariah');

-- Fakultas Ilmu Kesehatan (S1)
INSERT INTO public.siak_program_studi (siak_fakultas_id, siak_jenjang_id, nama_program_studi) VALUES
   ('00000000-0000-0000-0000-000000000005', '00000000-0000-0000-0000-000000000011', 'Kesehatan Masyarakat');

-- Fakultas Teknik dan Sains (S1)
INSERT INTO public.siak_program_studi (siak_fakultas_id, siak_jenjang_id, nama_program_studi) VALUES
  ('00000000-0000-0000-0000-000000000006', '00000000-0000-0000-0000-000000000011', 'Teknik Informatika'),
  ('00000000-0000-0000-0000-000000000006', '00000000-0000-0000-0000-000000000011', 'Teknik Sipil'),
  ('00000000-0000-0000-0000-000000000006', '00000000-0000-0000-0000-000000000011', 'Teknik Mesin'),
  ('00000000-0000-0000-0000-000000000006', '00000000-0000-0000-0000-000000000011', 'Teknik Elektro');
