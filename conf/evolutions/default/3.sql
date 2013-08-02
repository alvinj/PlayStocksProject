
# STOCK

# Notes: 
# - Ups and Downs comments are important.
# - made 'company' not required to get through the tutorial


# --- !Ups

create table stocks (
  id int auto_increment not null,
  symbol varchar(10) not null,
  company varchar(32),
  primary key (id),
  constraint unique index idx_stock_unique (symbol)
);

insert into stocks (symbol, company) values ('AAPL', 'Apple');
insert into stocks (symbol, company) values ('GOOG', null);

create table stock_prices (
  id int auto_increment not null,
  stock_id int not null,
  date_time timestamp not null default now(),
  price decimal(15,2) not null default 0,
  primary key (id),
  foreign key (stock_id) references stocks (id) on delete cascade
);


# --- !Downs

SET FOREIGN_KEY_CHECKS = 0;
drop table if exists stock_prices; 
drop table if exists stocks;
SET FOREIGN_KEY_CHECKS = 1;


