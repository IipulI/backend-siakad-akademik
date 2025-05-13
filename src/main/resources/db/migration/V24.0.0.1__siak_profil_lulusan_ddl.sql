-- SIAK PRDFIL LULUSAN --
CREATE TABLE IF NOT EXISTS public.siak_profil_lulusan (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    siak_program_studi_id UUID NOT NULL,
    siak_tahun_kurikulum_id UUID NOT NULL,
    kode_pl VARCHAR(20) NOT NULL,
    profil VARCHAR(255) NOT NULL,
    profesi VARCHAR(255) NOT NULL,
    deskripsi_pl TEXT NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (siak_program_studi_id) REFERENCES siak_program_studi(id),
    FOREIGN KEY (siak_tahun_kurikulum_id) REFERENCES siak_tahun_kurikulum(id)
);
