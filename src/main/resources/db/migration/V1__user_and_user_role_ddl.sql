----------------
-- USER TABLE --
----------------
CREATE TABLE IF NOT EXISTS public.users (
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   username VARCHAR(100) NOT NULL UNIQUE,
   password VARCHAR(255) NOT NULL,
   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP,
   deleted_at TIMESTAMP
);

----------------
-- ROLE TABLE --
----------------
CREATE TABLE IF NOT EXISTS public.roles (
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   name VARCHAR(30) NOT NULL,
   description TEXT,
   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP,
   deleted_at TIMESTAMP
);

------------------------------
-- USER ROLE JUNCTION TABLE --
------------------------------
CREATE TABLE IF NOT EXISTS public.user_role (
   user_id UUID NOT NULL,
   role_id UUID NOT NULL,
   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP,
   deleted_at TIMESTAMP,
   PRIMARY KEY (user_id, role_id),
   FOREIGN KEY (user_id) REFERENCES users(id),
   FOREIGN KEY (role_id) REFERENCES roles(id)
);

-------------------------
-- USER ACTIVITY TABLE --
-------------------------
CREATE TABLE IF NOT EXISTS public.user_activity (
   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
   user_id UUID NOT NULL,
   activity TEXT,
   ip_address VARCHAR(15),
   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP,
   deleted_at TIMESTAMP,
   FOREIGN KEY (user_id) REFERENCES users(id)
);