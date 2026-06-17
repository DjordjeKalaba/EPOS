create table if not exists users (
  id bigserial primary key,
  email varchar(200) not null unique,
  password_hash varchar(300) not null,
  role varchar(50) not null,
  tenant_id bigint null,
  created_at timestamp not null default now()
);
