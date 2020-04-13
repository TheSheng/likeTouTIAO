package com.lisheng.project.controller;

import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.concurrent.TimeUnit;
@Controller
public class MaController {
    @Autowired
    RedisTemplate redisTemplate;
    @GetMapping("/changMa")
    public String  changeMa()
    {
      redisTemplate.opsForValue().set("erweima","true",1l, TimeUnit.MINUTES);
      return "return";
    }

}
