CREATE TABLE person
(
    id varchar not null constraint persona_pk primary key,
    person_name varchar,
    gender varchar,
    age integer,
    address varchar,
    phone varchar
);

CREATE TABLE client
(
    client_id bigint not null constraint client_pk primary key,
    client_password varchar,
    status bool,
    person_id varchar not null unique
);

CREATE TABLE account
(
    account_id bigint not null constraint account_pk primary key,
    client_id bigint not null,
    account_number bigint not null unique,
    account_type varchar,
    initial_amount float8,
    status bool
);

CREATE TABLE movement
(
    movement_id bigint not null constraint movement_pk primary key,
    account_id bigint,
    movement_date date not null,
    movement_type varchar,
    movement_value float8,
    balance float8
);