package com.webcollector.webcollector.service;

import com.webcollector.webcollector.bean.Top;

import java.util.Date;
import java.util.List;

public interface TopService {
    public List<Top> getList(List<String> list);

    public void insert(int order,String title,int heat);

    public void bachInsert(List<Object> titleList,List<Object> heatList,List<Object> urlList);

    List<Top> findRealTop();

    List<Top> findDeletedTop(int top);

    List<Top> findHistoryBurst(int index,int top);

    List<Top> findMinute(Date start);

    void addDelete(String title);

}
