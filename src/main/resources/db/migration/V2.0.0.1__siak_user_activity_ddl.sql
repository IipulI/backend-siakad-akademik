-- SIAK USER ACTIVITY TABLE --
CREATE TABLE IF NOT EXISTS public.siak_user_activity (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    siak_user_id UUID NOT NULL,
    ip_address VARCHAR(15),
    activity TEXT, -- Buat contant
    waktu TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_deleted bool NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (siak_user_id) REFERENCES siak_user(id)
);

CREATE INDEX IF NOT EXISTS idx_user_activity_user_id ON public.siak_user_activity(siak_user_id);
