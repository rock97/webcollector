package com.webcollector.webcollector;

import org.ansj.splitWord.analysis.ToAnalysis;

import java.io.*;
import java.util.*;

/**
 * Created by lizhihuamobike on 2020/3/30.
 */
public class Test {
    public static void main(String[] args) throws IOException {
        wordFrequency();
    }

    public static void wordFrequency() throws IOException {

        Map<String, Integer> map = new HashMap<>();
        String article = getString();
        String result = ToAnalysis.parse(article).toStringWithOutNature();
        String[] words = result.split(",");
        for(String word: words){
            String str = word.trim();
            // 过滤空白字符
            if (str.equals(""))
                continue;
                // 过滤一些高频率的符号
            else if(str.matches("[）|（|.|，|。|+|-|“|”|：|？|\\s]"))
                continue;
                // 此处过滤长度为1的str
            else if (str.length() < 2)
                continue;
            if (!map.containsKey(word)){
                map.put(word, 1);
            } else {
                int n = map.get(word);
                map.put(word, ++n);
            }
        }
        Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
        Double count = 0.0;
        while (iterator.hasNext()){
            Map.Entry<String, Integer> entry = iterator.next();
            count +=entry.getValue();
        }
        List<Map.Entry<String, Integer>> list = new ArrayList<>();

        Map.Entry<String, Integer> entry;
        while ((entry = getMax(map)) != null){
            list.add(entry);
        }

        for (Map.Entry<String, Integer> enty : list) {
            System.out.println(enty.getKey()+" = " + enty.getValue() +" 占比（千分之）: "+((enty.getValue() / count)*1000)/10);

        }



    }

    /**
     * 找出map中value最大的entry, 返回此entry, 并在map删除此entry
     * @param map
     * @return
     */
    public static Map.Entry<String, Integer> getMax(Map<String, Integer> map){
        if (map.size() == 0){
            return null;
        }
        Map.Entry<String, Integer> maxEntry = null;
        boolean flag = false;
        Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Integer> entry = iterator.next();
            if (!flag){
                maxEntry = entry;
                flag = true;
            }
            if (entry.getValue() > maxEntry.getValue()){
                maxEntry = entry;
            }
        }
        map.remove(maxEntry.getKey());
        return maxEntry;
    }
    /**
     * 从文件中读取待分割的文章素材.
     　　* 文件内容来自简书热门文章: https://www.jianshu.com/p/5b37403f6ba6
     * @return
     * @throws IOException
     */

    public static String getString() throws IOException {

        FileInputStream inputStream = new FileInputStream(new File("/Users/lizhihuamobike/text.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder strBuilder = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null){
            strBuilder.append(line);
        }
        reader.close();
        inputStream.close();
        return strBuilder.toString();

    }

}
