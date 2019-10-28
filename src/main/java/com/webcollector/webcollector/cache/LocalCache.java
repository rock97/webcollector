package com.webcollector.webcollector.cache;

import com.webcollector.webcollector.bean.Top;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class LocalCache {
    public static final String GETTOP = "TopController.getTop";
    public static final String FINDDELETETOP = "TopController.findDeleteTop";
    private static ConcurrentHashMap<String, List<Top>> cache = new ConcurrentHashMap();
    public void put(String key,List<Top> value){
        cache.put(key, value);
    }

    public List<Top> get(String key){
        return cache.get(key);
    }

    public void clear(){
        cache.clear();
    }

    public void remove(String key){
        cache.remove(key);
    }
}
