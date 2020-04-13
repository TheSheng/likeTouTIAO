package com.lisheng.project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class User {

    private Long id;

    private String userName;

    private String imgUrl;

    private String passWord;

    private Integer loginCount;

    public User(Long id, String userName, String imgUrl, String passWord, Integer loginCount) {
        this.id = id;
        this.userName = userName;
        this.imgUrl = imgUrl;
        this.passWord = passWord;
        this.loginCount = loginCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }
    public User(){}
}
