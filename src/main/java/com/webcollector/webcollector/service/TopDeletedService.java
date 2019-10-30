package com.webcollector.webcollector.service;

import com.webcollector.webcollector.bean.TopDeleted;
import java.util.Date;
import java.util.List;

public interface TopDeletedService {
    void addinc(List<String> titleList);
    List<String> getTitleList(List<String> titleList);
    void bachInsert(List<TopDeleted> deletedList);
    List<TopDeleted> getTop(int top);
    List<String> getTitleListAll(List<String> titleList);
    List<Long> findLastMinuteDeleted(Date date);

}
