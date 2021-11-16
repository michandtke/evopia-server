create table useraccount
(
    id                   bigserial   not null,
    first_name           varchar(50) not null,
    last_name            varchar(50) not null,
    date_of_registration varchar(50),
    email                varchar(255),
    password             varchar(255),
    primary key (id)
);

create table role
(
    id   bigserial   not null,
    name varchar(50) not null,
    primary key (id)
);

create table userroles
(
    user_id int not null references useraccount (id),
    role_id int not null references role (id),
    primary key (user_id, role_id)
);

create table event
(
    id          bigserial    not null,
    name        varchar(255) not null,
    description varchar(1048),
    date        varchar(255) not null,
    time        varchar(255),
    place       varchar(255),
    tags        varchar(255) not null,
    image       varchar(255),
    primary key (id)
);