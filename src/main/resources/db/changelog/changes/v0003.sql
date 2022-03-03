create table if not exists tag
(
    id          bigserial      not null,
    name        varchar(255)   not null,
    primary key (id)
);

create table if not exists profiletags
(
    profile_id int not null references profile (id),
    tag_id int not null references tags (id),
    primary key (profile_id, tag_id)
);