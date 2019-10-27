package com.webcollector.webcollector.service;

import com.webcollector.webcollector.bean.Top;

import java.time.LocalTime;
import java.util.List;

public interface TopService {
    public List<Top> getTop(int top);

    public List<Top> getList(List<String> list);

    public void insert(int order,String title,int heat);

    public void bachInsert(List<Object> urls,List<Object> heatList);

    List<Top> findDeleted(List<String> list);

    List<Top> findRealTop();

    List<Top> findDeletedTop(int top);

}
