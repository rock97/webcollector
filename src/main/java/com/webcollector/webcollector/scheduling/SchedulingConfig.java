package com.webcollector.webcollector.scheduling;

import com.alibaba.fastjson.JSON;
import com.webcollector.webcollector.bean.Top;
import com.webcollector.webcollector.bean.TopHistory;
import com.webcollector.webcollector.mapper.TopDao;
import com.webcollector.webcollector.mapper.TopHistoryDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
public class SchedulingConfig {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TopDao topDao;

    @Autowired
    private TopHistoryDao topHistoryDao;

    @Scheduled(cron = "0/60 * * * * ?")
    public void snycTop(){
        List<Top> topList = topDao.findLastTop(getLastMinute(2));

        if(topList ==null || topList.size() ==0) return;

        List<String> toptitleList = topList.stream().map(Top::getTitle).collect(Collectors.toList());
        Map<String, Top> topMap = new HashMap<>();
        for (Top s : topList) {
            topMap.put(s.getTitle(),s);
        }
        logger.info("toptitleList.size = {}",toptitleList.size());
        List<TopHistory> listHistory = topHistoryDao.getList(toptitleList,getLastMinute(60));
        Map<String, TopHistory> mapTopHistory = new HashMap<>();
        for (TopHistory s : listHistory) {
            mapTopHistory.put(s.getTitle(),s);
        }
        List<TopHistory> newList = new ArrayList<>();
        for (String s : toptitleList) {
            if(mapTopHistory.get(s)==null){
                Top top = topMap.get(s);
                TopHistory topHistory = new TopHistory();
                BeanUtils.copyProperties(top,topHistory);
                newList.add(topHistory);
            }else{
                TopHistory history = mapTopHistory.get(s);
                Top top = topMap.get(s);
                history.setHeat(history.getHeat()+top.getHeat());
                topHistoryDao.update(history);
                logger.info("SchedulingConfig update ={}",JSON.toJSONString(history));
            }
        }

        if(newList.size()>0){
            topHistoryDao.bachInsert(newList);
            logger.info("SchedulingConfig new list = {}", JSON.toJSONString(newList));
        }

        List<Long> idList = topList.stream().map(Top::getId).collect(Collectors.toList());
        topDao.delete(idList);
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
}
