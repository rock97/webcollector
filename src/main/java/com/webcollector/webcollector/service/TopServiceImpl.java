package com.webcollector.webcollector.service;
import com.webcollector.webcollector.bean.Top;
import com.webcollector.webcollector.cache.LocalCache;
import com.webcollector.webcollector.mapper.TopDao;
import com.webcollector.webcollector.mapper.TopHistoryDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class TopServiceImpl implements TopService{
    @Autowired
    private LocalCache localCache;

    @Autowired
    private TopDao topDao;

    @Autowired
    private TopHistoryDao topHistoryDao;

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

        for (int i = 0; i < heatList.size(); i++) {
            Top top = new Top();
            top.setSequence(i);
            top.setHeat(Integer.valueOf(heatList.get(i).toString()));
            top.setType("new");
            top.setStatus(1);
            top.setTitle(titleList.get(i));
            topList.add(top);
            //topDao.insert(top,new Date());
        }


    }

    @Override
    public List<Top> findRealTop() {
        List<Top> lastMinute = this.findlastMinuteTop();
        return lastMinute;
    }

    @Override
    public List<Top> findDeletedTop(int top) {
        return topDao.findDeletedTop(top);
    }

    @Override
    public List<Top> findHistoryBurst(int index, int top) {
        return topDao.findHistoryBurst(index,Math.max(0,top-100),top);
    }

    @Override
    public List<Top> findMinute(Date start) {
        return topDao.findMinute(start);
    }

    @Override
    public void addDelete(String title) {
        Top topByTitle = topDao.getTopByTitle(title);
        topByTitle.setStatus(2);
        topByTitle.setType("deleted");
        List<Top> list = new ArrayList<>();
        list.add(topByTitle);
        topDao.bachInsert(list,getLastMinute(0));
    }

    public Date getLastMinute(int i){
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

        List<Top> historyBurst = topDao.findHistoryBurst(3,0, 500);
        localCache.put(LocalCache.FINDHISTORYBURST+":3:100",historyBurst);

    }

}
