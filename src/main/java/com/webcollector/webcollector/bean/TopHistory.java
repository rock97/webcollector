package com.webcollector.webcollector.bean;


import lombok.Data;

import java.util.Date;

@Data
public class TopHistory {
    private long id;
    private int sequence;
    private String title;
    private int heat;
    private String type;
    private int status;
    private Date createTime;
}
