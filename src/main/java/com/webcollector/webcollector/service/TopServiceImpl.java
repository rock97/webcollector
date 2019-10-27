package com.webcollector.webcollector.service;

import com.webcollector.webcollector.bean.Top;
import com.webcollector.webcollector.mapper.TopDao;
import java.util.Calendar;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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

    public List<Top> getList(List<String> list) {
        return topDao.getList(list);
    }

    @Override
    public void insert(int order,String title,int heat) {
        Top top = new Top();
        top.setSequence(order);
        top.setHeat(heat);
        top.setType("insert");
        top.setStatus(1);
        top.setTitle(title);
        topDao.insert(top,getLastMinute());
    }

    public void bachInsert(List<Object> urls,List<Object> heatList) {
        if(urls == null){
            return;
        }
        List<Top> topList = new ArrayList<>();
        List<String> titleList = new ArrayList();

        for (Object url : urls) {
            titleList.add(url.toString());
        }

        List<Top> deletedTop = findDeletedTop(titleList);

        for (int i = 0; i < heatList.size(); i++) {
            Top top = new Top();
            top.setSequence(i);
            top.setHeat(Integer.valueOf(heatList.get(i).toString()));
            top.setType("new");
            top.setStatus(1);
            top.setTitle(titleList.get(i+1));
            topList.add(top);
        }

        for (Top top : deletedTop) {
            top.setStatus(2);
            top.setType("deleted");
            topList.add(top);
        }


        topDao.bachInsert(topList,getLastMinute());
    }

    @Override
    public List<Top> findDeletedTop(List<String> list) {
        List<Top> topList = topDao.getLastMinute();
        List<Top> deleteTop = new ArrayList();
        Map<String, String> map = list.stream().collect(Collectors.toMap(v -> v, v -> v));

        for (Top top : topList) {
            if(map.get(top.getTitle())==null){
                deleteTop.add(top);
            }
        }

        return deleteTop;
    }

    private Date getLastMinute(){
        Date date = new Date();
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);
        cal1.set(Calendar.SECOND,0);
        return cal1.getTime();
    }
}
