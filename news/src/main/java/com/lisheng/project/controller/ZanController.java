package com.lisheng.project.controller;

import com.lisheng.project.service.ZanService;
import com.lisheng.project.vo.Zan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ZanController {
    @Autowired
    ZanService zanService;
    @GetMapping("/getNewsInfo/{newsId}")
    public Map getNewsInfo(@PathVariable String newsId){
        return  zanService.getNews(newsId);
    }
    @PostMapping("/zan")
    public void getNewsInfo(@RequestBody Zan zan){
       zanService.zan(zan);
    }
    @GetMapping("/userForNews/{articleId}/{userId}")
    public Map userForNews(@PathVariable String articleId,@PathVariable String userId){
        return  zanService.getUserForArticle(articleId,userId);
    }
    @GetMapping("/getUserInfo/{userId}")
    public  Map userInfo(@PathVariable String userId){
        return  zanService.getUserAllInfo(userId);
    }

}
