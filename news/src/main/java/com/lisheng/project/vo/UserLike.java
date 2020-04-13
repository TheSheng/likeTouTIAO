package com.lisheng.project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class UserLike {
    private String username;
    private Integer size;
    private Integer page;

    public UserLike(String username, Integer size, Integer page) {
        this.username = username;
        this.size = size;
        this.page = page;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
    public UserLike(){}
}
