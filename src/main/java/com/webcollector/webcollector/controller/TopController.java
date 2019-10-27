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
    public List<Top> getTop(@RequestParam(value = "top",required = false,defaultValue = "51") int top){
        List<Top> top1 = topService.getTop(top);
        top1.sort(Comparator.comparing(Top::getHeat));
        return top1;
    }
    @GetMapping("/index")
    public String index(Model model ){
        return "index";
    }
}
