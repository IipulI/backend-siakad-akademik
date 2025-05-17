-- SIAK PEMETAAN PL CPL TABLE --
CREATE TABLE IF NOT EXISTS public.siak_pemetaan_cpl_cpmk (
   siak_capaian_mata_kuliah_id UUID NOT NULL,
   siak_capaian_pembelajaran_id UUID NOT NULL,
   is_deleted BOOLEAN DEFAULT FALSE,
   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP,
   FOREIGN KEY (siak_capaian_mata_kuliah_id) REFERENCES siak_capaian_mata_kuliah(id),
   FOREIGN KEY (siak_capaian_pembelajaran_id) REFERENCES siak_capaian_pembelajaran_lulusan(id)
);
