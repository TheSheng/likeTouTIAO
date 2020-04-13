package com.lisheng.project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


public class UserTags {
    private List<String> tags;
    private String username;

    public UserTags(List<String> tags, String username) {
        this.tags = tags;
        this.username = username;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public  UserTags(){}
}
