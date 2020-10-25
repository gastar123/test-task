create table CAR (
  id IDENTITY primary key,
  brand VARCHAR2(150),
  model VARCHAR2(200),
  power DOUBLE,
  year_of_issue YEAR,
);

create table AIRPLANE (
  id IDENTITY primary key,
  brand VARCHAR2(150),
  model VARCHAR2(200),
  manufacturer VARCHAR2(500),
  year_of_issue YEAR,
  fuel_capacity INT,
  seats INT
);

create table ESTIMATE (
  id IDENTITY primary key,
  assessed_value DEC(20),
  assessed_date DATE
);

create table AIRPLANE_ESTIMATE (
  estimate_id INT unique,
  airplane_id INT,
  foreign key (estimate_id) references estimate (id),
  foreign key (airplane_id) references airplane (id)
);

create index on AIRPLANE_ESTIMATE(airplane_id);

create table CAR_ESTIMATE (
  estimate_id INT unique,
  car_id INT,
  foreign key (estimate_id) references estimate (id),
  foreign key (car_id) references car (id)
);

create index on CAR_ESTIMATE(car_id);