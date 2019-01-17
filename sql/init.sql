CREATE SEQUENCE IF NOT EXISTS sq_entity INCREMENT BY 1 START WITH 0;

CREATE TABLE IF NOT EXISTS tb_entity
(
    entity              integer DEFAULT nextval('sq_entity') NOT NULL PRIMARY KEY,
    first_name          varchar,
    last_name           varchar,
    email               varchar UNIQUE
);

CREATE SEQUENCE IF NOT EXISTS sq_state INCREMENT BY 1 START WITH 0;

CREATE TABLE IF NOT EXISTS tb_state
(
    state       integer DEFAULT nextval('sq_state') NOT NULL PRIMARY KEY,
    iso_code    varchar(2) UNIQUE,
    description varchar UNIQUE
);

CREATE SEQUENCE IF NOT EXISTS sq_business INCREMENT BY 1 START WITH 0;

CREATE TABLE IF NOT EXISTS tb_business
(
    business            integer DEFAULT nextval('sq_business') NOT NULL PRIMARY KEY,
    owner               integer REFERENCES tb_entity NOT NULL,
    name                varchar NOT NULL,
    description         varchar NOT NULL,
    about               varchar NOT NULL,
    address_line_one    varchar NOT NULL,
    address_line_two    varchar,
    address_line_three  varchar,
    zip_code            varchar NOT NULL,
    city                varchar NOT NULL,
    state               integer NOT NULL REFERENCES tb_state

    UNIQUE( name, owner ); 
);

CREATE SEQUENCE IF NOT EXISTS sq_review INCREMENT BY 1 START WITH 0;

CREATE TABLE IF NOT EXISTS tb_review
(
    review          integer DEFAULT nextval('sq_review') NOT NULL PRIMARY KEY,
    entity          integer REFERENCES tb_entity NOT NULL,
    business        integer REFERENCES tb_business NOT NULL,
    description     varchar NOT NULL,
);

CREATE SEQUENCE IF NOT EXISTS sq_catagory INCREMENT BY 1 START WITH 0;

CREATE TABLE IF NOT EXISTS tb_catagory
(
    catagory        integer DEFAULT nextval('sq_catagory') NOT NULL PRIMARY KEY,
    description     varchar NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS sq_business_catagory INCREMENT BY 1 START WITH 0;

CREATE TABLE IF NOT EXISTS tb_business_catagory
(
    business_catagory       integer DEFAULT nextval('sq_catagory') NOT NULL PRIMARY KEY,
    business                integer REFERENCES tb_business NOT NULL,
    catagory                integer REFERENCES tb_catagory NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS sq_rating INCREMENT BY 1 START WITH 0;

CREATE TABLE IF NOT EXISTS tb_rating
(
    rating          integer DEFAULT nextval('sq_rating') NOT NULL PRIMARY KEY,
    avg_rating      numeric,
    num_ratings     integer
);