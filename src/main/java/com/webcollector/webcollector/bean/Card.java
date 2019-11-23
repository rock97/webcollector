package com.webcollector.webcollector.bean;

import lombok.Data;

@Data
public class Card {
    private int card_type;
    private String itemid;
    private String title;
    private String scheme;
    private String desc;

}
