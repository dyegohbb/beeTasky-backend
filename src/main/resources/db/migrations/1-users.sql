-- POSTGRES AUTO-GENERATED CREATE-TABLE

-- Table: public.users

-- DROP TABLE IF EXISTS public.users;

CREATE TABLE IF NOT EXISTS public.users
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
    created_on timestamp(6) without time zone NOT NULL,
    deleted boolean NOT NULL,
    deleted_on timestamp(6) without time zone,
    email character varying(255) COLLATE pg_catalog."default" NOT NULL,
    identifier character varying(255) COLLATE pg_catalog."default" NOT NULL,
    password character varying(255) COLLATE pg_catalog."default" NOT NULL,
    username character varying(255) COLLATE pg_catalog."default" NOT NULL,
    updated_on timestamp(6) without time zone,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email),
    CONSTRAINT ukqxbxprw69rusb9ap6ydm5ndue UNIQUE (identifier),
    CONSTRAINT ukr43af9ap4edm43mmtq01oddj6 UNIQUE (username)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.users
    OWNER to root;
-- Index: idx_users_email

-- DROP INDEX IF EXISTS public.idx_users_email;

CREATE INDEX IF NOT EXISTS idx_users_email
    ON public.users USING btree
    (email COLLATE pg_catalog."default" ASC NULLS LAST)
    TABLESPACE pg_default;
-- Index: idx_users_identifier

-- DROP INDEX IF EXISTS public.idx_users_identifier;

CREATE INDEX IF NOT EXISTS idx_users_identifier
    ON public.users USING btree
    (identifier COLLATE pg_catalog."default" ASC NULLS LAST)
    TABLESPACE pg_default;
-- Index: idx_users_username

-- DROP INDEX IF EXISTS public.idx_users_username;

CREATE INDEX IF NOT EXISTS idx_users_username
    ON public.users USING btree
    (username COLLATE pg_catalog."default" ASC NULLS LAST)
    TABLESPACE pg_default;