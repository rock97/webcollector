package com.webcollector.webcollector.controller;

import com.webcollector.webcollector.bean.Top;
import com.webcollector.webcollector.bean.TopHistory;
import com.webcollector.webcollector.cache.LocalCache;
import com.webcollector.webcollector.mapper.TopHistoryDao;
import com.webcollector.webcollector.service.TopServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/weibo")
public class TopController {
    @Autowired
    private TopServiceImpl  topService;

    @Autowired
    private LocalCache localCache;

    @Autowired
    private TopHistoryDao topHistoryDao;

    @GetMapping("/top")
    @ResponseBody
    public List<Top> getTop(){

        return topService.findRealTop();
    }

    @GetMapping("/findDeleteTop")
    @ResponseBody
    public List<TopHistory> findDeleteTop(@RequestParam(value = "day",required = false,defaultValue = "1") int day){
        List<TopHistory> deletedTop = topHistoryDao.findDeletedTop(100);
        return deletedTop;
    }

    @GetMapping("/findHistoryBurst")
    @ResponseBody
    public List<TopHistory> findHistoryBurst(int index,int top){
        List<TopHistory> historyBurst = topHistoryDao.findHistoryBurst(3, 0, 100);
        return historyBurst;
    }

    @GetMapping("/index")
    public String index(Model model ){
        return "index";
    }
}
