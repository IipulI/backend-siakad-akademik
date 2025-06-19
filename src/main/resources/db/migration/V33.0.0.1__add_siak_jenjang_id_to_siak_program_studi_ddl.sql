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
INSERT INTO public.siak_program_studi (id, siak_fakultas_id, siak_jenjang_id, nama_program_studi) VALUES
  ('ba54f264-2948-439c-85eb-559154c8e3c8','00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000011', 'Komunikasi dan Penyiaran Islam'),
  ('1f4b7367-9b2c-4abb-9d79-3a2e3c7f2859','00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000011', 'Hukum Keluarga Islam'),
  ('f88c3c24-857f-4fad-a080-63f08fdd9e19','00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000011', 'Pendidikan Agama Islam'),
  ('c153b190-9080-4b18-826e-09a7043b7ade','00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000011', 'Ekonomi Syariah'),
  ('6bbc60aa-5e1b-4424-b671-4bab94fcaea0','00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000011', 'Pendidikan Guru Madrasah Ibtidaiyah'),
  ('16d89f25-8299-4c07-920e-4cb7cebfa5ad','00000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000011', 'Bimbingan dan Konseling Pendidikan Islam');

-- Fakultas Keguruan dan Ilmu Pendidikan (S1)
INSERT INTO public.siak_program_studi (id, siak_fakultas_id, siak_jenjang_id, nama_program_studi) VALUES
  ('2eec7e3f-daf5-43ff-937a-c62a611044d1', '00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000011', 'Pendidikan Masyarakat'),
  ('681dac84-fcaa-4f84-8eff-2293c9b504dd', '00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000011', 'Pendidikan Bahasa Inggris'),
  ('0c7ae20a-6dbc-4c9e-b7a3-a6bb47265893', '00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000011', 'Teknologi Pendidikan'),
  ('3d6fca2f-d8be-4775-a7ad-8bce0381693f', '00000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000011', 'Pendidikan Vokasional Desain Fashion');

-- Fakultas Hukum (S1)
INSERT INTO public.siak_program_studi (id, siak_fakultas_id, siak_jenjang_id, nama_program_studi) VALUES
  ('583948ae-a57e-4184-858f-cbbfb6f1d761', '00000000-0000-0000-0000-000000000003', '00000000-0000-0000-0000-000000000011', 'Ilmu Hukum');

-- Fakultas Ekonomi dan Bisnis
INSERT INTO public.siak_program_studi (id, siak_fakultas_id, siak_jenjang_id, nama_program_studi) VALUES
  ('79447d55-4f01-42e2-b9d9-00538f319a49', '00000000-0000-0000-0000-000000000004', '00000000-0000-0000-0000-000000000011', 'Manajemen'),
  ('b1e9a667-4811-4d41-9bb5-7ba1fa539ba2', '00000000-0000-0000-0000-000000000004', '00000000-0000-0000-0000-000000000011', 'Akuntansi'),
  ('ec8cd1eb-190c-4da1-8241-ca3f3a47a321', '00000000-0000-0000-0000-000000000004', '00000000-0000-0000-0000-000000000010', 'Keuangan Perbankan'), -- D3
  ('0d48c7ed-9006-46da-b6f5-0d1d5dcbdbb2', '00000000-0000-0000-0000-000000000004', '00000000-0000-0000-0000-000000000011', 'Ekonomi Syariah');

-- Fakultas Ilmu Kesehatan (S1)
INSERT INTO public.siak_program_studi (id, siak_fakultas_id, siak_jenjang_id, nama_program_studi) VALUES
   ('7a35d188-4b57-4071-a0d0-2851912fecee', '00000000-0000-0000-0000-000000000005', '00000000-0000-0000-0000-000000000011', 'Kesehatan Masyarakat');

-- Fakultas Teknik dan Sains (S1)
INSERT INTO public.siak_program_studi (id, siak_fakultas_id, siak_jenjang_id, nama_program_studi) VALUES
  ('14bab8ca-c3ab-44f2-88f5-e5065144c9e2', '00000000-0000-0000-0000-000000000006', '00000000-0000-0000-0000-000000000011', 'Teknik Informatika'),
  ('7f58051d-9efd-4b9d-b128-28da1c4dd570', '00000000-0000-0000-0000-000000000006', '00000000-0000-0000-0000-000000000011', 'Teknik Sipil'),
  ('3a2d6568-493e-4c74-bb8a-2c30aec60ade', '00000000-0000-0000-0000-000000000006', '00000000-0000-0000-0000-000000000011', 'Teknik Mesin'),
  ('715b7b03-ac5f-488f-ba94-5579faa843bb', '00000000-0000-0000-0000-000000000006', '00000000-0000-0000-0000-000000000011', 'Teknik Elektro');
