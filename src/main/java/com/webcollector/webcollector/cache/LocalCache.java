package com.webcollector.webcollector.cache;

import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class LocalCache {
    public static final String GETTOP = "TopController.getTop";
    public static final String FINDDELETETOP = "TopController.findDeleteTop";
    private static ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap();
    public void put(String key,Object value){
        cache.put(key, value);
    }

    public Object get(String key){
        return cache.get(key);
    }

    public void clear(){
        cache.clear();
    }

    public void remove(String key){
        cache.remove(key);
    }
}
