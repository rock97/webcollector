package com.webcollector.webcollector.scheduling;

import com.webcollector.webcollector.bean.Top;
import com.webcollector.webcollector.bean.TopHistory;
import com.webcollector.webcollector.mapper.TopDao;
import com.webcollector.webcollector.mapper.TopHistoryDao;
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

    private int limit = 0;

    @Autowired
    private TopDao topDao;

    @Autowired
    private TopHistoryDao topHistoryDao;

    @Scheduled(fixedRate = 100)
    public void snycTop(){
        List<Top> topList = topDao.getTop(limit, limit + 50);

        Date lastMinute = getLastMinute(30);
        if(topList ==null || topList.size() ==0 || topList.get(0).getCreateTime().after(lastMinute)) return;

        List<String> toptitleList = topList.stream().map(Top::getTitle).collect(Collectors.toList());
        Map<String, Top> topMap = topList.stream().collect(Collectors.toMap(v -> v.getTitle(), v -> v));
        List<TopHistory> listHistory = topHistoryDao.getList(toptitleList);
        List<String> topHistorytitleList = listHistory.stream().map(TopHistory::getTitle).collect(Collectors.toList());
        Map<String, String> mapTopHistory = topHistorytitleList.stream().collect(Collectors.toMap(v -> v, v -> v));

        List<TopHistory> newList = new ArrayList<>();
        for (String s : toptitleList) {
            if(mapTopHistory.get(s)==null){
                Top top = topMap.get(s);
                TopHistory topHistory = new TopHistory();
                BeanUtils.copyProperties(top,topHistory);
                newList.add(topHistory);
            }
        }

        if(newList.size()>0){
            topHistoryDao.bachInsert(newList);
        }

        List<Long> idList = topList.stream().map(Top::getId).collect(Collectors.toList());
        topDao.delete(idList);

        limit+=50;
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
