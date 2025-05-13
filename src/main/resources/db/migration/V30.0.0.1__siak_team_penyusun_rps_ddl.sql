-- SIAK TEAM PENYUSUN RPS --
CREATE TABLE IF NOT EXISTS public.siak_team_penyusun_rps (
   siak_rps_id UUID NOT NULL,
   siak_dosen_id UUID NOT NULL,
   is_deleted BOOLEAN DEFAULT FALSE,
   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP,
   FOREIGN KEY (siak_rps_id) REFERENCES siak_rps(id),
   FOREIGN KEY (siak_dosen_id) REFERENCES siak_dosen(id)
);
