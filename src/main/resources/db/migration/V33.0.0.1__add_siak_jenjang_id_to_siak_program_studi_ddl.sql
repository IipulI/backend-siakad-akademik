ALTER TABLE public.siak_program_studi
    ADD COLUMN siak_jenjang_id UUID;

ALTER TABLE public.siak_program_studi
    ADD FOREIGN KEY (siak_jenjang_id) REFERENCES siak_jenjang(id);