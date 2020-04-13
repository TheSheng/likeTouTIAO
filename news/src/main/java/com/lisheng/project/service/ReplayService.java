package com.lisheng.project.service;

import com.alibaba.fastjson.JSON;
import com.lisheng.project.vo.Replay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ReplayService {
    @Autowired
    RedisTemplate redisTemplate;


    public List pinglun(Replay replay){
        redisTemplate.opsForHash().put("article:"+replay.getArticleId()+":replay",replay.getId().toString(),JSON.toJSONString(replay));
        redisTemplate.opsForSet().add("user:"+replay.getUserId()+":replay",replay.getArticleId().toString());
        return redisTemplate.opsForHash().multiGet("article:"+replay.getArticleId()+":replay",redisTemplate.opsForHash().keys("article:"+replay.getArticleId()+":replay"));
    }
    public List pinglunhuifu(Replay replay){
        Replay allreplay = JSON.parseObject(redisTemplate.opsForHash().get("article:" + replay.getArticleId() + ":replay", replay.getId().toString()).toString(),Replay.class);
        allreplay.getReplyBody().add(replay);
        return pinglun(allreplay);


    }

    public List getArticlePL(String articleId){
        return redisTemplate.opsForHash().multiGet("article:"+articleId+":replay",redisTemplate.opsForHash().keys("article:"+articleId+":replay"));

    }
    public  Set getUserPL(String userId){
        return redisTemplate.opsForSet().members("user:"+userId+":replay");
    }


}
