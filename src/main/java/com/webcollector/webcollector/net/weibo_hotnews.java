package com.webcollector.webcollector.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.TargetAuthenticationStrategy;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
public class weibo_hotnews {
    public static String webheader="http://s.weibo.com/weibo/";

    public static void main(String []args) throws UnsupportedEncodingException {
        String [][]result=patter_script(Analysis_page(get_page("http://s.weibo.com/top/summary?cate=realtimehot")));
        for (int i = 0; i < result.length; i++) {
            System.out.println((i+1)+" "+result[i][0]+" "+result[i][1]);
        }
    }


    public static String download_page(String url){
        String content=null;
        //创建客户端
        DefaultHttpClient httpClient=new DefaultHttpClient();
        HttpGet httpGet=new HttpGet(url);
        HttpResponse response;
        try {
            response = httpClient.execute(httpGet);
            HttpEntity entity=response.getEntity();
            if (entity!=null) {
                content= EntityUtils.toString(entity,"utf-8");
                EntityUtils.consume(entity);
            }
        } catch (ClientProtocolException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }finally {
            httpClient.getConnectionManager().shutdown();
        }

        return content;
    }

    public static Document get_page(String url){
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
            System.out.println("url或网络连接错误");
            return null;
        }
    }
    public static String Analysis_page(Document page_html){
        Elements links=page_html.getElementsByClass("td-02");

        String allstring=null;
        int tag=0;
        for(Element link:links){
            String script=link.toString();
            if (script.length()>tag) {
                tag=script.length();
                allstring=script;
            }
        }
        return allstring;
    }

    public static String[][] patter_script(String tag){
        String [][] result=new String[50][2];
        int i=0,j=0;
        Pattern pattern = Pattern.compile("class=\\\\\"star_name\\\\\">\\\\n   <a href=\\\\\"\\\\/weibo\\\\/(.*?)&Refer=top\\\\\" target=\\\\\"_blank\\\\\"   suda-data=\\\\\"key=tblog_search_list&value=list_realtimehot\\\\\">(.*?)<\\\\/a>");
        Matcher matcher = pattern.matcher(tag);
        while (matcher.find()) {
            result[i][j]= StringEscapeUtils.unescapeJava(matcher.group(2));
            j++;
            result[i][j]=webheader+matcher.group(1);
            j=0;i++;
        }
        return result;
    }
}
