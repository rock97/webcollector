package com.webcollector.webcollector.service;

import com.webcollector.webcollector.bean.Top;
import com.webcollector.webcollector.mapper.TopDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TopServiceImpl implements TopService{

    @Autowired
    private TopDao topDao;

    public List<Top> getTop(int top) {
        return topDao.getTop(top);
    }

    public List<Top> getList(long timeId) {
        //return topDao.getList(timeId);
        return null;
    }

    @Override
    public void insert(int order,String title,int heat) {
        Top top = new Top();
        top.setSequence(order);
        top.setHeat(heat);
        top.setType("insert");
        top.setStatus(1);
        top.setTitle(title);
        topDao.insert(top);
    }

    public void bachInsert(List<String> listTitle) {
        if(listTitle == null){
            return;
        }
        Date date = new Date();
        List<Top> topList = new ArrayList<>();

        for (String title : listTitle) {
            Top top = new Top();
            top.setType("insert");
            top.setStatus(1);
            top.setTitle(title);
            topList.add(top);
        }
        topDao.bachInsert(topList);
    }
}
