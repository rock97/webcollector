package com.webcollector.webcollector.service;

import com.alibaba.fastjson.JSON;
import com.webcollector.webcollector.bean.Top;
import com.webcollector.webcollector.cache.LocalCache;
import com.webcollector.webcollector.mapper.TopDao;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
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
        top.setType("new");
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

        List<Top> deletedTop = this.findDeleted(titleList,Integer.valueOf(heatList.get(heatList.size()-5).toString()));

        for (int i = 0; i < heatList.size(); i++) {
            String url = urlList.get(i).toString();
            Top top = new Top();
            top.setSequence(i);
            top.setHeat(Integer.valueOf(heatList.get(i).toString()));
            top.setType("new");
            top.setStatus(1);
            top.setTitle(titleList.get(i));
            top.setUrl("http://s.weibo.com"+url);
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
        List<Long> oldListids = this.findlastMinuteTop();
        if(oldListids == null || oldListids.size() ==0){
            return new ArrayList<>();
        }
        List<Top> oldList = topDao.findListByIds(oldListids);
        List<Top> deleteTop = new ArrayList();
        Map<String, String> nowMap = list.stream().collect(Collectors.toMap(v -> v, v -> v));
        for (Top oldTop : oldList) {
            if(nowMap.get(oldTop.getTitle())==null){
                if(oldTop.getHeat() > oldList.get(45).getHeat()) {
                    deleteTop.add(oldTop);
                }
            }
        }

        return deleteTop;
    }


    public List<Top> findDeleted(List<String> list,int heat) {
        List<Long> oldListIds = this.findlastMinuteTop();
        if(oldListIds == null || oldListIds.size() ==0){
            return new ArrayList<>();
        }
        List<Top> oldList = topDao.findListByIds(oldListIds);

        List<Top> deleteTop = new ArrayList();

        Map<String, String> nowMap = list.stream().collect(Collectors.toMap(v -> v, v -> v));
        for (Top oldTop : oldList) {
            if(nowMap.get(oldTop.getTitle())==null){
                if(oldTop.getHeat() > heat) {
                    deleteTop.add(oldTop);
                }
            }
        }

        return deleteTop;
    }

    @Override
    public List<Top> findRealTop() {
        List<Long> lastMinute = this.findlastMinuteTop();
        List<Long> lastMinuteDeleted = topDao.findLastMinuteDeleted(getLastMinute(15));
        lastMinute.addAll(lastMinuteDeleted);
        if(lastMinute == null || lastMinute.size() ==0){
            return new ArrayList<>();
        }
        return topDao.findListByIds(lastMinute);
    }

    @Override
    public List<Top> findDeletedTop(int top) {
        return null;
    }

    @Override
    public List<Top> findHistoryBurst(int index, int top) {
        List<Long> historyBurst = topDao.findHistoryBurst(index, Math.max(0, top - 100), top);
        if(historyBurst == null || historyBurst.size() ==0){
            return new ArrayList<>();
        }
        return topDao.findListByIds(historyBurst);
    }

    @Override
    public List<Top> findLastDayDeletedTop(int day) {
        List<Long> deletedTopIds = topDao.findLastDayDeletedTop(getLastMinute(60*24*day),getLastMinute(60*24*(day-1)));
        if(deletedTopIds == null || deletedTopIds.size() ==0){
            return new ArrayList<>();
        }
        List<Top> listByIds = topDao.findListByIds(deletedTopIds);
        Top top = new Top();
        top.setTitle("最近"+day+"*24小时被删热搜");
        top.setStatus(3);
        listByIds.add(0,top);
        return listByIds;
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

    private List<Long> findlastMinuteTop(){
        List<Long> lastMinute = topDao.getLastMinute(getLastMinute(0));
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

        List<Top> deletedTop = this.findLastDayDeletedTop(1);
        localCache.put(LocalCache.FINDDELETETOP+":1",deletedTop);

        List<Top> historyBurst = this.findHistoryBurst(0, 100);
        localCache.put(LocalCache.FINDHISTORYBURST+":3:100",historyBurst);

    }
}
