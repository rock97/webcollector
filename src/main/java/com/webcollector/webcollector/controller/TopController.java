package com.webcollector.webcollector.controller;

import com.webcollector.webcollector.bean.Top;
import com.webcollector.webcollector.service.TopServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/weibo")
public class TopController {
    @Autowired
    private TopServiceImpl topService;

    @GetMapping("/top")
    @ResponseBody
    public List<Top> getTop(@RequestParam(value = "top",required = false,defaultValue = "51") int top){
        List<Top> top1 = topService.getTop(top);
        top1.sort(Comparator.comparing(Top::getSequence));
        return top1;

    }
    @GetMapping("/index")
    public String index(){
        return "/templates/index.html";
    }
}
