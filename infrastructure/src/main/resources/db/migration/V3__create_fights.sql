create table fights (
    id VARCHAR (255) not null primary key,
    first_boxer_id varchar(255) not null,
    second_boxer_id varchar(255) not null,
    number_of_rounds int not null,
    place varchar(255),
    happen_at date not null
);
