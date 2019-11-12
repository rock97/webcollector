create table weibo.top
(
  id          bigint auto_increment
    primary key,
  sequence    int           not null,
  create_time datetime      null,
  status      int(4)        null,
  heat        int           null,
  type        varchar(50)   null,
  title       varchar(50)   null,
  url         varchar(1000) null
)
  charset = utf8;

create index idx_createTime
  on weibo.top (create_time);

create index idx_title
  on weibo.top (title);

