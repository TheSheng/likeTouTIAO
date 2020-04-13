package com.lisheng.project.service;

import com.lisheng.project.vo.Zan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ZanService {
    @Autowired
    RedisTemplate redisTemplate;
    public void zan(Zan zan){
        if("zan".equals(zan.getType())||"shoucang".equals(zan.getType())||"cai".equals(zan.getType())){
            Object rs=redisTemplate.opsForValue().get("news:" + zan.getArticleId() +":"+ zan.getType());
            Integer count=rs==null?0:new Integer(rs.toString());

            System.out.println("进来了");
            count++;

            redisTemplate.opsForValue().set("news:" + zan.getArticleId() + ":"+zan.getType(),count.toString());
            redisTemplate.opsForSet().add("user:"+zan.getUserId()+":"+zan.getType(),zan.getArticleId());
        }else if("quxiaozan".equals(zan.getType())||"quxiaoshoucang".equals(zan.getType())||"quxiaocai".equals(zan.getType())){
            if("quxiaozan".equals(zan.getType())){
                zan.setType("zan");
            }else if ("quxiaoshoucang".equals(zan.getType())){
                zan.setType("shoucang");
            }else {
                zan.setType("cai");

            }


            Object rs1 =redisTemplate.opsForValue().get("news:" + zan.getArticleId() +":"+ zan.getType());
            Integer count = rs1==null?0:new Integer(rs1.toString());
            count--;
            redisTemplate.opsForValue().set("news:" + zan.getArticleId() + ":"+zan.getType(),count.toString());
            redisTemplate.opsForSet().remove("user:"+zan.getUserId()+":"+zan.getType(),zan.getArticleId());
        }

    }
    public Map<String,Integer> getNews(String articleId){
        Map<String,Integer> map=new HashMap<>();
        Integer zan=0;
        Object rs=redisTemplate.opsForValue().get("news:" + articleId + ":" + "zan");
        zan=rs==null?0:new Integer(rs.toString());

        map.put("zan",zan);
        Integer cai=0;
        Object rs1=redisTemplate.opsForValue().get("news:" + articleId + ":" + "cai");
        cai=rs1==null?0:new Integer(rs1.toString());
        map.put("cai",cai);
        Integer shoucang=0;
        Object rs2=redisTemplate.opsForValue().get("news:" + articleId + ":" + "shoucang");
        shoucang=rs2==null?0:new Integer(rs2.toString());
        map.put("shoucang",shoucang);
        return  map;
    }
    public Map<String,Boolean>  getUserForArticle(String articleId,String userId){
        Map<String,Boolean> map=new HashMap<>();
        map.put("isZan",redisTemplate.opsForSet().isMember("user:"+userId+":"+"zan",articleId));
        map.put("isCai",redisTemplate.opsForSet().isMember("user:"+userId+":"+"cai",articleId));
        map.put("isShoucang",redisTemplate.opsForSet().isMember("user:"+userId+":"+"shoucang",articleId));
        return  map;

    }
    public  Map<String, Set> getUserAllInfo(String userId){
        Map<String,Set> map=new HashMap<>();
        map.put("zan",redisTemplate.opsForSet().members("user:"+userId+":"+"zan"));
        map.put("cai",redisTemplate.opsForSet().members("user:"+userId+":"+"cai"));
        map.put("shoucang",redisTemplate.opsForSet().members("user:"+userId+":"+"shoucang"));
        map.put("pinglun",redisTemplate.opsForSet().members("user:"+userId+":replay"));
        return map;
    }

}
