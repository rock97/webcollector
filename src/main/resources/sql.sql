create table top
(
    id          bigint auto_increment
        primary key,
    sequence    int         null,
    create_time datetime    null,
    status      int(4)      null,
    heat        int         null,
    type        varchar(50) null,
    title       varchar(50) null
)charset = utf8;
create index idx_createTime
    on weibo.top (create_time);
