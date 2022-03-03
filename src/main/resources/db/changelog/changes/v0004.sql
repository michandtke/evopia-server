
alter table profile
DROP COLUMN tags;

alter table profile
DROP COLUMN channels;

create table if not exists channel
(
    id          bigserial      not null,
    name        varchar(1048)  not null,
    primary key (id)
);

create table if not exists profilechannels
(
    profile_id int not null references profile (id),
    channel_id int not null references channel (id),
    primary key (profile_id, channel_id)
);