package com.lisheng.project.user.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name ="user")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "userName")
    private String userName;
    @Column(name="imgUrl")
    private String imgUrl;
    @Column(name = "passWord")
    private String passWord;
    @Column(name = "loginCount",columnDefinition = "int default 0")
    private Integer loginCount;
    public User(){

    }
    public User(String userName, String imgUrl, String passWord, Integer loginCount) {
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
}

