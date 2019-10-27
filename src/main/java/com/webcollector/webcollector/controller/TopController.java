package com.webcollector.webcollector.controller;

import com.webcollector.webcollector.bean.Top;
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

    @GetMapping("/top")
    @ResponseBody
    public List<Top> getTop(){
        List<Top> top1 = topService.findRealTop();
        top1.sort(Comparator.comparing(Top::getHeat).reversed());
        return top1;
    }

    @GetMapping("/findDeleteTop")
    @ResponseBody
    public List<Top> findDeleteTop(@RequestParam(value = "top",required = false,defaultValue = "50") int top){
        List<Top> top1 = topService.findDeletedTop(top);
        top1.sort(Comparator.comparing(Top::getHeat).reversed());
        return top1;
    }
    @GetMapping("/index")
    public String index(Model model ){
        return "index";
    }
}
