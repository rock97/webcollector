create table top
(
    id          bigint auto_increment
        primary key,
    sequence    int        not null,
    create_time datetime   not null,
    status      int(4)     not null,
    heat        int        not null,
    type        varchar(50) not null,
    title       varchar(60) not null
)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
create index idx_createTime
    on weibo.top (create_time);
create index top_create_time_title_index on top (create_time, title);