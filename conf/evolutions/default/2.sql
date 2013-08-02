
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

# --- !Downs
 
DROP TABLE stocks;


# insert into stocks (symbol, company) values ('AAPL', 'Apple');
# insert into stocks (symbol, company) values ('GOOG', null);

