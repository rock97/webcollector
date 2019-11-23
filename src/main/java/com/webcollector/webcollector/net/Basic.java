package com.webcollector.webcollector.net;

import cn.wanghaomiao.seimi.annotation.Crawler;
import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.struct.Request;
import cn.wanghaomiao.seimi.struct.Response;

import com.alibaba.fastjson.JSONObject;
import com.webcollector.webcollector.bean.Card;
import com.webcollector.webcollector.bean.Top;
import java.util.List;

import com.webcollector.webcollector.service.TopServiceImpl;
import java.util.Map;
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
            List<Object> urlList = doc.sel("//td[@class='td-02']/a/[@href]");
            bachInstet(titleList,heatList,urlList);

            List<Top> topList = topService.findMinute(topService.getLastMinute(2));
            for (Top top : topList) {
               String url =  "https://m.weibo.cn/api/container/getIndex?containerid=231522type%3D1%26q%3D%23"+top.getTitle().replaceAll(" ","%20")+"%23&page_type=searchall";
               push(Request.build(url,Basic::findDelete));
               Thread.sleep(500);
            }

        } catch (Exception e) {
        }
    }
    @Scheduled(cron = "0/60 * * * * ?")
    public void callByCron(){
        push(Request.build(startUrls()[0],"start").setSkipDuplicateFilter(true));
    }

    public void findDelete(Response response){
        JXDocument doc = response.document();
        try {
            List<Object> sel = doc.sel("//body/text()");
            JSONObject jsonObject = JSONObject.parseObject(sel.get(0).toString());
            String data = jsonObject.getString("data");
            String item = JSONObject.parseObject(data).getString("cards");
            List<Card> list = JSONObject.parseArray(item, Card.class);
           for (Card card : list) {
                if(7==card.getCard_type() || card.getDesc().contains("话题页未予显示")){
                    String cardlistInfo = JSONObject.parseObject(data).getString("cardlistInfo");
                    String cardlist_head_cards = JSONObject.parseObject(cardlistInfo).getString("cardlist_head_cards");
                    List<Object> cardlistInfolist = JSONObject.parseArray(cardlist_head_cards, Object.class);
                    Map<String,Object> map = (Map<String,Object>)cardlistInfolist.get(0);
                    List channel_list = (List)map.get("channel_list");
                    Map<String,String> o = (Map<String,String>)channel_list.get(0);
                    String containerid = o.get("containerid");
                    String[] split = containerid.split("#");
                    String title = split[1];
                    topService.addDelete(title);
                    logger.info("被删除的热搜 = {}",title);
                    return;
                }
            }
        } catch (Exception e) {

        }
    }
    private void bachInstet( List<Object> titleList,List<Object> heatList,List<Object> urlList){
        topService.bachInsert(titleList.subList(1,titleList.size()),heatList,urlList.subList(1,urlList.size()));
        logger.info("bachInsert size={}",titleList.size());
    }
}
