-- ADD FK TO SIAK KOMPOSISI NILAI MATA KULIAH TABLE --
ALTER TABLE public.siak_komposisi_nilai_mata_kuliah
    ADD CONSTRAINT fk_siak_mata_kuliah
        FOREIGN KEY (siak_mata_kuliah_id) REFERENCES public.siak_mata_kuliah(id);

ALTER TABLE public.siak_komposisi_nilai_mata_kuliah
    ADD CONSTRAINT fk_siak_komposisi_nilai
        FOREIGN KEY (siak_komposisi_nilai_id) REFERENCES public.siak_komposisi_nilai(id);