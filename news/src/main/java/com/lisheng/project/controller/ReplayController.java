package com.lisheng.project.controller;

import com.lisheng.project.service.ReplayService;
import com.lisheng.project.vo.Replay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
public class ReplayController {

    @Autowired
    ReplayService replayService;
    @PostMapping("/pinglun")
    public List pinglun(@RequestBody Replay replay){
       return  replayService.pinglun(replay);
    }
    @PostMapping("/huifu")
    public  List  huifu(@RequestBody Replay replay){
        return  replayService.pinglunhuifu(replay);
    }
    @GetMapping("/articlePL/{articleId}")
    public  List getArticlePL(@PathVariable  String articleId){
        return  replayService.getArticlePL(articleId);
    }
    @GetMapping("/userPL/{userId}")
    public Set getUserPL(@PathVariable String userId){
        return  replayService.getUserPL(userId);
    }
}
