package com.lisheng.project.controller;

import com.alibaba.fastjson.JSON;
import com.lisheng.project.constant.ResponseBody;
import com.lisheng.project.domain.NewEntity;
import com.lisheng.project.repository.NewRepository;
import com.lisheng.project.service.NewService;
import com.lisheng.project.vo.SortSize;
import com.lisheng.project.vo.UserLike;
import com.lisheng.project.vo.UserNotes;
import com.lisheng.project.vo.UserTags;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController

public class NewsController {
    @Autowired
    NewService newService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    KafkaTemplate kafkaTemplate;
    @Autowired
    NewRepository newRepository;
    @GetMapping("/getTagTop")
    public ResponseBody getTagTop(){
        Set tag = redisTemplate.opsForZSet().reverseRange("tagTop", 1, 20);
        return  new ResponseBody("ok",tag,200);

    }
    @PostMapping("/getUserAction/{userid}/{type}")
    public List<NewEntity> getUserAction(@PathVariable String userid,@PathVariable String type,@RequestBody SortSize sortSize){
        Set members=new HashSet();
        if(type.equals("liulan")){



            members=redisTemplate.opsForSet().members("users:"+userid+":notes");
        }
        else {
            members = redisTemplate.opsForSet().members("user:" + userid + ":" + type);
        }
        Set<Long> rs=new HashSet<>();
         members.forEach(x-> {
                 if(!x.toString().equals(""))
                 rs.add(new Long(x.toString()));
             }
         );

         Sort sort=new Sort(Sort.Direction.DESC,"time");
         Pageable pageable=PageRequest.of(sortSize.getPage(),sortSize.getSize(),sort);
         return  newRepository.findAllByIdIn(rs,pageable).getContent();

    }
    @PostMapping("/addMoreTag")
    public ResponseBody addMoreTag(@RequestBody UserTags userTags){
        newService.addMoreTag(userTags.getTags(),userTags.getUsername());
        return  new ResponseBody("ok","",200);

    }
    @GetMapping("/saveMa")
    public void saveMa(){
        redisTemplate.opsForValue().set("erweima","false",1l, TimeUnit.MINUTES);


    }

    @GetMapping("/getMa")
     public Object getMa()
    {

        Object erweima = redisTemplate.opsForValue().get("erweima");
        return null==erweima?"过期":erweima;

    }
    @PostMapping("/getUserLike")
    public  Page<NewEntity> getUserLike(@RequestBody UserLike userLike){
        Sort sort=new Sort(Sort.Direction.DESC,"time");
         Pageable pageable=PageRequest.of(userLike.getPage(),userLike.getSize(),sort);
        return newService.getUsersLike(userLike.getUsername(),pageable);
    }
    @GetMapping("/getUserTag")
    public Set getUserTag(@RequestParam String username){
       return  newService.getUserTag(username);

    }
    @PostMapping("/getNewsByType")
    public Page<NewEntity> getUserTag(@RequestBody SortSize sortSize){
        Sort sort=new Sort(Sort.Direction.DESC,"time");

        Pageable pageable=PageRequest.of(sortSize.getPage(),sortSize.getSize(),sort);

        return  newService.getNewsByType(sortSize.getType(),pageable);

    }
    @PostMapping("/getDay")
    public Page<NewEntity> getDay(@RequestBody SortSize sortSize){
        Sort sort=new Sort(Sort.Direction.DESC,"time");

        Pageable pageable=PageRequest.of(sortSize.getPage(),sortSize.getSize(),sort);

        return  newService.getNewsDay(pageable);

    }
    @GetMapping("/getTagNews/{tag}")
    public  Set getTagNew(@PathVariable String tag){
        return redisTemplate.opsForZSet().reverseRange("tag:" + tag + ":news", 1, -1);
    }

    @PostMapping("/userNotes")
    public  void  addUserNotes(@RequestBody UserNotes userNotes){
        redisTemplate.opsForSet().add("users:"+userNotes.getUserId()+":notes",userNotes.getNewsId());
        kafkaTemplate.send("userNote", JSON.toJSONString(userNotes));


    }
    @GetMapping("/search/{tag}")
    public List<NewEntity>  search(@PathVariable String tag){
        Set list = redisTemplate.opsForSet().members("tag:" + tag + ":news");
        Set<Long> ids=new HashSet<>();
        list.forEach(x->{
            if(null!=x&&!"".equals(x.toString())){
                ids.add(new Long(x.toString()));

            }
        });
        Sort sort=new Sort(Sort.Direction.DESC,"time");
        return  newRepository.findAllByIdIn(ids,sort);
    }
    @GetMapping("/maxId")
    public List<NewEntity>  getMaxID(){
        return newService.maxID();
    }
    @PostMapping("/getById")
    public NewEntity getById(@RequestBody NewEntity newEntity){
        return  newService.getNewsById(newEntity.getId());
    }
    @GetMapping("/getByIdTwo/{id}")
    public NewEntity getByIdTwo(@PathVariable String id){
        return  newService.getNewsById(new Long(id));
    }
    @GetMapping("/getNewsLike/{newsId}")
    public  List<NewEntity> getNewsLike(@PathVariable String newsId){
      return   newService.getNewsLike(newsId);
    }
}
