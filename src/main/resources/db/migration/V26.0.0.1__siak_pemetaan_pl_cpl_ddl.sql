-- SIAK PEMETAAN PL CPL TABLE --
CREATE TABLE IF NOT EXISTS public.siak_pemetaan_pl_cpl (
    siak_profil_lulusan_id UUID NOT NULL,
    siak_capaian_pembelajaran_id UUID NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (siak_profil_lulusan_id) REFERENCES siak_profil_lulusan(id),
    FOREIGN KEY (siak_capaian_pembelajaran_id) REFERENCES siak_capaian_pembelajaran_lulusan(id)
);
