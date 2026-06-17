create table if not exists tenant_requests (
  id bigserial primary key,
  company_name varchar(150) not null,
  contact_name varchar(150) not null,
  email varchar(200) not null,
  phone varchar(50),
  status varchar(30) not null,
  created_at timestamp not null default now()
);

create table if not exists tenants (
  id bigserial primary key,
  company_name varchar(150) not null,
  db_name varchar(200),
  status varchar(30) not null,
  created_at timestamp not null default now()
);
