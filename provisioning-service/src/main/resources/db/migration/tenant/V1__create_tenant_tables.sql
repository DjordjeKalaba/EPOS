create table if not exists clients (
  id bigserial primary key,
  name varchar(200) not null,
  email varchar(200),
  phone varchar(50),
  address varchar(300)
);

create table if not exists invoices (
  id bigserial primary key,
  invoice_number varchar(50) not null,
  client_id bigint not null references clients(id),
  issue_date date not null,
  total_amount numeric(12,2) not null
);

create table if not exists invoice_items (
  id bigserial primary key,
  invoice_id bigint not null references invoices(id),
  description varchar(300) not null,
  quantity numeric(12,2) not null,
  unit_price numeric(12,2) not null,
  line_total numeric(12,2) not null
);
