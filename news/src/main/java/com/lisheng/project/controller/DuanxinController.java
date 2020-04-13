package com.lisheng.project.controller;

import com.lisheng.project.service.NewService;
import com.lisheng.project.util.HttpUtils;
import com.lisheng.project.vo.Mobie;
import com.lisheng.project.vo.Response;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
@RestController
public class DuanxinController {
    @Autowired
    RedisTemplate redisTemplate;

    public  String ma(){
        Random rd=new Random();
        StringBuilder str=new StringBuilder();
        for(int i=0;i<4;i++){
            str.append(rd.nextInt(10));
        }
        return  str.toString();
    }
    public String times(){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        return  simpleDateFormat.format(new Date());
    }
    @PostMapping("/sendDuanxin")
    public  Response<String> send(@RequestBody Mobie mobie) throws Exception{
        Double score = redisTemplate.opsForZSet().score(times(), mobie.getUsername());
        Integer scores=null==score?0:score.intValue();
        if(scores>3){
            return new Response<String>("每天最多只能发三次哦","不要发了，我穷");
        }
        String host = "https://cdcxdxjk.market.alicloudapi.com";
        String path = "/chuangxin/dxjk";
        String method = "POST";
        String ma=ma();
        String appcode = "1038d8ebf621457eae4a310349d09e21";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("content", "【创信】你的验证码是："+ma+"，3分钟内有效！");
        //注意测试可用：【创信】你的验证码是：#code#，3分钟内有效！，发送自定义内容联系旺旺或QQ：726980650报备
        querys.put("mobile", mobie.getUsername());
        Map<String, String> bodys = new HashMap<String, String>();
        HttpResponse response=null;

        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
             response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
             redisTemplate.opsForZSet().incrementScore(times(),mobie.getUsername(),1);

            //System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Response<String>(EntityUtils.toString(response.getEntity()),ma);
    }
    @GetMapping("/getUserSendCount/{userName}")
    public  Response<Integer> getCount(@PathVariable String userName){
        Double score = redisTemplate.opsForZSet().score(times(), userName);
        return  new Response<Integer>(null==score?0:score.intValue(),"");
    }
}
