package com.lisheng.project.user.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.lisheng.project.user.constant.ResponseBody;
import com.lisheng.project.user.domain.User;
import com.lisheng.project.user.service.UserService;
import com.lisheng.project.user.vo.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController

public class Login {
    final
    UserService userService;
    final
    RedisTemplate redisTemplate;
    @Autowired
    public Login(UserService userService, RedisTemplate redisTemplate) {
        this.userService = userService;
        this.redisTemplate = redisTemplate;
    }
    @GetMapping("/duanxinyanzheng/{username}")
    public  ResponseBody<String> registerUser(@PathVariable String username){
        return new ResponseBody<>("短信验证",userService.licenseUser(username),200);
    }

    @PostMapping("/login")
    public ResponseBody<User> login(@RequestBody User user){

        User login = userService.login(user.getUserName(), user.getPassWord());

        if(null==login){
            return new ResponseBody<User>("用户名或密码错误",new User(),500);
        }

        redisTemplate.opsForZSet().incrementScore("activeUser",user.getUserName(),1);
        return  new ResponseBody<User>("登录成功",login,200);
    }
    @PostMapping("/register")
    public ResponseBody<User> register(@RequestBody User user){
        if(!userService.license(user)){
            return new ResponseBody<User>("该用户已存在",new User(),500);
        }
        user.setLoginCount(1);
        User register = userService.register(user);

        if(null==register){
            return new ResponseBody<User>("注册失败",new User(),500);
        }

        return  new ResponseBody<User>("注册成功",register,200);
    }
    @PostMapping("/gai")
    public ResponseBody<String> gai(@RequestBody User user){
        return new ResponseBody<String>(userService.gai(user),"修改密码",200);
    }
    @GetMapping("/getUserLike/{userId}")
    public List<News> getUserLike(@PathVariable String userId){
        return userService.getUsersLike(userId);

    }
    @PostMapping("/loginUser")
    public ResponseBody<User> getLoginUser(@RequestBody User user ){

        User loginUser = userService.getLoginUser(user.getId());
        if(null!=loginUser){
            return new ResponseBody<User>("",loginUser,200);
        }
        return new ResponseBody<User>("错误，请重新登录",loginUser,500);
    }
    @GetMapping("/saveRate")
    public  void saveRate() throws Exception{
          userService.saveRate();
    }
}
