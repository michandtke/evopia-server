DROP TABLE profilechannels;

create table if not exists userchannel
(
    user_id     int         not null references useraccount (id),
    channel_id  int         not null references channel (id),
    value       varchar(255),
    primary key (user_id, channel_id)
);
