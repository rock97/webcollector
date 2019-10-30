package com.webcollector.webcollector.controller;

import com.webcollector.webcollector.bean.Top;
import com.webcollector.webcollector.bean.TopDeleted;
import com.webcollector.webcollector.cache.LocalCache;
import com.webcollector.webcollector.service.TopDeletedService;
import com.webcollector.webcollector.service.TopService;
import com.webcollector.webcollector.service.TopServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/weibo")
public class TopController {
    @Autowired
    private TopService topService;

    @Autowired
    private LocalCache localCache;

    @Autowired
    private TopDeletedService topDeletedService;

    @GetMapping("/top")
    @ResponseBody
    public List<Top> getTop(){
        List<Top> top1 = (List<Top>)localCache.get(LocalCache.GETTOP);
        if(top1 == null) {
            topService.findRealTop();
            top1.sort(Comparator.comparing(Top::getHeat).reversed());
        }
        return top1;
    }

    @GetMapping("/findDeleteTop")
    @ResponseBody
    public List<TopDeleted> findDeleteTop(@RequestParam(value = "top",required = false,defaultValue = "50") int top){
        List<TopDeleted> top1 = (List<TopDeleted>)localCache.get(LocalCache.FINDDELETETOP);
        if(top1 == null) {
            top1 = topDeletedService.getTop(top);
            localCache.put(LocalCache.FINDDELETETOP,top1);
        }
        return top1;
    }
    @GetMapping("/index")
    public String index(Model model ){
        return "index";
    }
}
