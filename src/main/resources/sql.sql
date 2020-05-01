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


/*
ssh -i weibors.xyz.pem root@47.105.69.108 1997yjj.
ssh -p22022 root@149.129.127.41 N86iuTz7JuafhUoQ
scp -i weibors.xyz.pem ../Documents/webcollector-1.6.5.jar root@47.105.69.108:/home
netstat -ltunp*/
