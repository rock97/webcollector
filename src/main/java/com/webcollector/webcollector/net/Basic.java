package com.webcollector.webcollector.net;

import cn.wanghaomiao.seimi.annotation.Crawler;
import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.struct.Request;
import cn.wanghaomiao.seimi.struct.Response;

import java.util.List;

import com.webcollector.webcollector.service.TopServiceImpl;
import org.seimicrawler.xpath.JXDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

@Crawler(name = "basic")
public class Basic extends BaseSeimiCrawler {

    @Autowired
    private TopServiceImpl topService;

    @Override
    public String[] startUrls() {
        return new String[]{"http://s.weibo.com/top/summary?cate=realtimehot"};
    }

    @Override
    public void start(Response response) {
        JXDocument doc = response.document();
        try {
            List<Object> titleList = doc.sel("//td[@class='td-02']/a/text()");
            List<Object> heatList = doc.sel("//td[@class='td-02']/span/text()");
            bachInstet(titleList,heatList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Scheduled(cron = "0/60 * * * * ?")
    public void callByCron(){
        // 可定时发送一个Request
         push(Request.build(startUrls()[0],"start").setSkipDuplicateFilter(true));
    }

    private void bachInstet( List<Object> urls,List<Object> heatList){
        topService.bachInsert(urls.subList(1,urls.size()),heatList);
        logger.info("bachInsert size={}",urls.size());
    }
}
