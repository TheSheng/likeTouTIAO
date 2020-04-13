package com.lisheng.project.user.service;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.lisheng.project.user.domain.User;
import com.lisheng.project.user.repository.UserRespository;
import com.lisheng.project.user.vo.News;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service

public class UserService {
    @Autowired
    UserRespository userRespository;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    RestTemplate restTemplate;

    public User login(String name, String pass) {
        return userRespository.findByUserNameAndPassWord(name, pass);
    }

    public User register(User registerUser) {
        return userRespository.save(registerUser);
    }

    public boolean license(User user) {
        if (null == userRespository.findByUserName(user.getUserName())) {
            return true;
        }
        return false;
    }

    public void saveRate() throws Exception {
        List<String> list = userRespository.find();
        File fout = new File("rate.data");
        if (!fout.exists()) {
            fout.createNewFile();

        }
        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        list.forEach(x -> {

            Set<ZSetOperations.TypedTuple> set = redisTemplate.opsForZSet().rangeWithScores("users:" + x + ":tagNotes", 0, -1);

            set.forEach(rates -> {
                try {
                    bw.write(x + "\t" + rates.getValue().toString() + "\t" + rates.getScore().intValue());
                    bw.newLine();
                }catch (IOException e){

                }
            });


        });
        bw.close();
    }

    /**
     * 验证请求发送短信的用户是否存在
     **/
    public String licenseUser(String username) {
        User byUserName = userRespository.findByUserName(username);
        if (null == byUserName) {
            return "不存在";
        }
        return byUserName.getId().toString();
    }

    public String gai(User user) {
        User byId = userRespository.findById(user.getId());
        if (null == byId) {
            return "该账号不存在";
        }
        if (!byId.getUserName().equals(user.getUserName())) {
            return "账号不匹配";
        }

        userRespository.save(user);
        return "修改成功";

    }

    public List<News> getUsersLike(String userId) {
        User byId = userRespository.findById(new Long(userId));
        ResponseEntity<Set> tags = restTemplate.exchange("http://192.168.1.101:8080/news/getUserTag?username=" + byId.getUserName(), HttpMethod.GET, null, Set.class);
        List<News> list = new ArrayList<>();
        tags.getBody().forEach(x -> {
            Set<Long> set = redisTemplate.opsForZSet().reverseRange("tag:" + x + ":news", 1, -1);
            set.forEach(y -> {
                ResponseEntity<News> result = restTemplate.exchange("http://192.168.1.101:8080/news/getByIdTwo/" + y, HttpMethod.GET, null, News.class);
                list.add(result.getBody());
            });
        });


        return list;

    }

    public User getLoginUser(Long userId) {
        return userRespository.findById(userId);
    }

}
