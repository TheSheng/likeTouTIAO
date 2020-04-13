package com.lisheng.project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class UserNotes {
    private  String userId;
    private  String newsId;
    private  String  tags;
    private  String  type;

    public UserNotes(String userId, String newsId, String tags, String type) {
        this.userId = userId;
        this.newsId = newsId;
        this.tags = tags;
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public UserNotes(){}
}
