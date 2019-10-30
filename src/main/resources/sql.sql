create table weibo.top
(
  id          bigint auto_increment
    primary key,
  sequence    int         null,
  create_time datetime    null,
  status      int(4)      null,
  heat        int         null,
  type        varchar(50) null,
  title       varchar(50) null
)
  charset = utf8;

create index idx_createTime
  on weibo.top (create_time);

create index idx_title
  on weibo.top (title);


create table weibo.top_deleted
(
  id          bigint auto_increment
    primary key,
  top_id      bigint                              not null,
  update_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
  count       int                                 null,
  create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
  title       varchar(50)                         null
)
  comment '被删除的热搜';

create index idx_title
  on weibo.top_deleted (title);

create index idx_top_id
  on weibo.top_deleted (top_id);

create index idx_update_time
  on weibo.top_deleted (update_time);

