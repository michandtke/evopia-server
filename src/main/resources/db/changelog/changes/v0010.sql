create table if not exists event_tags
(
    event_id     int     not null    references event (id),
    tag_id       int     not null    references tag (id),
    primary key (event_id, tag_id)
);