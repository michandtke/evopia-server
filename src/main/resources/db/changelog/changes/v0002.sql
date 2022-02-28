create table if not exists profile
(
    id          bigserial      not null,
    user_id     int            not null      references useraccount (id),
    image       varchar(255)   not null,
    tags        varchar(1048)  not null,
    channels    varchar(1048)  not null,
    primary key (id)
);