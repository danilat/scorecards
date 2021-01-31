create table scorecards (
    id VARCHAR (255) not null primary key,
    fight_id VARCHAR (255) not null,
    account_id VARCHAR (255) not null,
    first_boxer_id varchar(255) not null,
    second_boxer_id varchar(255) not null,
    first_boxer_scores json not null,
    second_boxer_scores json not null,
    scored_at timestamp
);
