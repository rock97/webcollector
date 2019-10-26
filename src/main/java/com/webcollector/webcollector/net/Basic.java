package com.webcollector.webcollector.net;

import cn.wanghaomiao.seimi.annotation.Crawler;
import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.struct.Request;
import cn.wanghaomiao.seimi.struct.Response;
import com.alibaba.fastjson.JSON;
import java.util.List;
import org.seimicrawler.xpath.JXDocument;
import org.springframework.scheduling.annotation.Scheduled;

@Crawler(name = "basic")

public class Basic extends BaseSeimiCrawler {
    @Override
    public String[] startUrls() {
        return new String[]{"http://s.weibo.com/top/summary?cate=realtimehot"};
    }

    @Override
    public void start(Response response) {
        JXDocument doc = response.document();
        try {

            List<Object> urls = doc.sel("//td[@class='td-02']/a/text()");
            logger.info("doc {}", JSON.toJSONString(doc));
            for (int i = 0; i < urls.size(); i++) {
                logger.info(i+"."+urls.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Scheduled(cron = "0/5 * * * * ?")
    public void callByCron(){
        logger.info("我是一个根据cron表达式执行的调度器，5秒一次");
        // 可定时发送一个Request
         push(Request.build(startUrls()[0],"start").setSkipDuplicateFilter(true));
    }
}
