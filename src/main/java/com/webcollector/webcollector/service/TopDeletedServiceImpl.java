package com.webcollector.webcollector.service;

import com.webcollector.webcollector.bean.TopDeleted;
import com.webcollector.webcollector.mapper.TopDeletedDao;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopDeletedServiceImpl implements TopDeletedService {

    @Autowired
    private TopDeletedDao topDeletedDao;
    @Override
    public void addinc(List<String> titleList) {
        topDeletedDao.addinc(titleList);
    }

    @Override
    public List<String> getTitleList(List<String> titleList) {
        return topDeletedDao.getTitleList(titleList);
    }

    @Override
    public void bachInsert(List<TopDeleted> deletedList) {
        topDeletedDao.bachInsert(deletedList);
    }

    @Override
    public List<TopDeleted> getTop(int top) {
        return topDeletedDao.getTop(top);
    }

    @Override
    public List<String> getTitleListAll(List<String> titleList) {
        return topDeletedDao.getTitleListAll(titleList);
    }

    @Override
    public List<Long> findLastMinuteDeleted(Date date) {
        return topDeletedDao.findLastMinuteDeleted(date);
    }
}
