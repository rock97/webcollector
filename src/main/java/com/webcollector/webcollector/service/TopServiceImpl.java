package com.webcollector.webcollector.service;

import com.webcollector.webcollector.bean.Top;
import com.webcollector.webcollector.cache.LocalCache;
import com.webcollector.webcollector.mapper.TopDao;
import java.util.Calendar;
import java.util.Comparator;
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
    private LocalCache localCache;

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
        topDao.insert(top,getLastMinute(0));
    }

    public void bachInsert(List<Object> urls,List<Object> heatList,List<Object> urlList) {
        if(urls == null){
            return;
        }
        List<Top> topList = new ArrayList<>();
        List<String> titleList = new ArrayList();

        for (Object url : urls) {
            titleList.add(url.toString());
        }

        List<Top> deletedTop = findDeleted(titleList);

        for (int i = 0; i < heatList.size(); i++) {
            Top top = new Top();
            top.setSequence(i);
            top.setHeat(Integer.valueOf(heatList.get(i).toString()));
            top.setType("new");
            top.setStatus(1);
            top.setTitle(titleList.get(i));
            top.setUrl("http://s.weibo.com"+urlList.get(i).toString());
            topList.add(top);
        }

        for (Top top : deletedTop) {
            top.setStatus(2);
            top.setType("deleted");
            topList.add(top);
        }


        topDao.bachInsert(topList,getLastMinute(0));

        this.putCache();
    }

    @Override
    public List<Top> findDeleted(List<String> list) {
        List<Top> topList = this.findlastMinuteTop();
        List<Top> deleteTop = new ArrayList();
        Map<String, String> map = list.stream().collect(Collectors.toMap(v -> v, v -> v));

        for (Top top : topList) {
            if(map.get(top.getTitle())==null){
                if(top.getHeat() > topList.get(30).getHeat()) {
                    deleteTop.add(top);
                }
            }
        }

        return deleteTop;
    }

    @Override
    public List<Top> findRealTop() {
        List<Top> lastMinute = this.findlastMinuteTop();
        List<Top> lastMinuteDeleted = topDao.findLastMinuteDeleted(getLastMinute(15));
        lastMinute.addAll(lastMinuteDeleted);
        return lastMinute;
    }

    @Override
    public List<Top> findDeletedTop(int top) {
        return topDao.findDeletedTop(top);
    }

    private Date getLastMinute(int i){
        Date date = new Date();
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);
        cal1.set(Calendar.SECOND,0);
        cal1.set(Calendar.MINUTE,cal1.get(Calendar.MINUTE)-i);
        cal1.set(Calendar.MILLISECOND,0);
        return cal1.getTime();
    }

    private List<Top> findlastMinuteTop(){
        List<Top> lastMinute = topDao.getLastMinute(getLastMinute(0));
        if(lastMinute == null || lastMinute.size()==0){
            lastMinute = topDao.getLastMinute(getLastMinute(1));
        }
        return lastMinute;
    }

    /**
     * 添加缓存
     */
    private void putCache(){
        List<Top> topList = findRealTop();
        topList.sort(Comparator.comparing(Top::getHeat).reversed());
        localCache.put(LocalCache.GETTOP,topList);

        List<Top> deletedTop1 = topDao.findLastMinuteDeleted(getLastMinute(60));
        localCache.put(LocalCache.FINDDELETETOP,deletedTop1);
    }
}
