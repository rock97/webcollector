create table top_history
(
  id          bigint auto_increment
    primary key,
  sequence    int           not null,
  create_time datetime      null,
  status      int(4)        null,
  heat        int           null,
  type        varchar(50)   null,
  title       varchar(50)   null
)ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT='历史热点';


