create table if not exists tags
(
    id          bigserial      not null,
    profile_ids varchar(255)   not null,
    channels    varchar(1048)  not null,
    primary key (id)
);

create table if not exists profiletags
(
    profile_id int not null references profile (id),
    tag_id int not null references tags (id),
    primary key (profile_id, tag_id)
);