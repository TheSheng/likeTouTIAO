package com.lisheng.project.service;

import com.alibaba.fastjson.JSON;
import com.lisheng.project.domain.NewEntity;
import com.lisheng.project.repository.NewRepository;
import com.lisheng.project.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.RestTemplate;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@EnableScheduling

public class NewService {
    @Autowired
    NewRepository newRepository;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    KafkaTemplate kafkaTemplate;



    public  Long dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();

        return ts;
    }

    public List<NewEntity> maxID(){
        return  newRepository.findAllByIdBetween(460l,463l);
    }
//    @Async
//    @Scheduled(cron = "0 0/1 * * *  ?")
//    public void getTopNTag(){
//        Object rs=redisTemplate.opsForValue().get("newsIndex");
//        Long index= null==rs?0l:new Long(rs.toString());
//
//        Long maxId=newRepository.findMaxId();
//        if(index<maxId) {
//
//            List<NewEntity> all = newRepository.findAllByIdBetween(index, maxId+1);
//
//            Map<String, Integer> tags = new HashMap<>();
//            for (NewEntity news : all
//            ) {
//                Arrays.asList(news.getTag().split(";")).forEach(x -> {
//                    Integer count = tags.getOrDefault(x, 1);
//                    count++;
//                    tags.put(x, count);
//
//
//
//                        redisTemplate.opsForSet().add("tag:" + x + ":news", news.getId().toString());
//
//                });
//            }
//            tags.forEach((x, y) -> {
//
//                redisTemplate.opsForZSet().incrementScore("tagTop",x,y.doubleValue());
//
//
//            });
//            redisTemplate.opsForValue().set("newsIndex",maxId.toString());
//        }
//
//
//    }
//    @Async
//    @Scheduled(cron = "0/5 * * * *  ?")
//    public void sendSpark(){
//        Object rs = redisTemplate.opsForValue().get("sparkIndex");
//        Integer maxId=newRepository.findMaxId().intValue();
//        Object rs1=redisTemplate.opsForValue().get("newsIndex");
//        Long index2= null==rs1?0l:new Long(rs1.toString());
//        Integer index=null==rs?1:new Integer(rs.toString());
//        if(index<maxId&&index<index2) {
//            index++;
//            NewEntity byId = newRepository.findById(index.longValue());
//            if (null!=byId&&!byId.getArticle().equals("")) {
//                byId.setArticle("");
//                byId.setAuthor("");
//                byId.setImgurl("");
//                byId.setTitle("");
//                byId.setUrl("");
//                byId.setType("");
//
//
//                kafkaTemplate.send("newsRecommad", JSON.toJSONString(byId));
//                redisTemplate.opsForValue().set("sparkIndex", index.toString());
//
//
//
//            }else{
//
//                redisTemplate.opsForValue().set("sparkIndex", index.toString());
//            }
//        }
//
//
//    }
    public List<NewEntity> getNewsLike(String newsID){
        List members =redisTemplate.opsForSet().randomMembers("news:" + newsID + ":recommand",6);
        Set<Long> par=new HashSet();

        if(null!=members&&members.size()>0){
            members.forEach(x->{
                par.add(new Long(x.toString()));
            });
            return  newRepository.findAllByIdIn(par);
        }

        return new ArrayList<>();

    }
    public Page<NewEntity> getUsersLike(String userName,Pageable pageable){
        Set members = redisTemplate.opsForSet().members("users:" + userName + ":recommand");
        Set<Long> par=new HashSet();
        if(null!=members&&members.size()>0){
            members.forEach(x->{
                par.add(new Long(x.toString()));
            });
            return  newRepository.findAllByIdIn(par,pageable);
        }

        return null;


    }


    public Page<NewEntity> getNewsByType(String type,Pageable pageable){
        return newRepository.findAllByType(type,pageable);
//       return newRepository.findAll(pageable);

    }
    public Page<NewEntity> getNewsDay(Pageable pageable){
        return newRepository.findAll(pageable);
//       return newRepository.findAll(pageable);

    }
    public NewEntity getNewsById(Long id){
        return newRepository.findById(id);
    }
    public void addMoreTag(List<String> tags,String username){
        tags.forEach(x->redisTemplate.opsForSet().add("users:"+username+":tag",x));

       // redisTemplate.opsForSet().intersect("users:"+username+":tag",tags); 交集


    }
    public Set getUserTag(String username){
        return redisTemplate.opsForSet().members("users:"+username+":tag");
    }
}
