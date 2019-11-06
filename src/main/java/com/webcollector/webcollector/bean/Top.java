package com.webcollector.webcollector.bean;


import lombok.Data;

import java.util.Date;

@Data
public class Top {
    private long id;
    private int sequence;
    private String title;
    private String url;
    private int heat;
    private String type;
    private int status;
    private Date createTime;
}
