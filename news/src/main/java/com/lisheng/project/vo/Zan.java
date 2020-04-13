package com.lisheng.project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

public class Zan implements Serializable {
    private String type;
    private String userId;
    private String articleId;

    public Zan(String type, String userId, String articleId) {
        this.type = type;
        this.userId = userId;
        this.articleId = articleId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }
    public Zan(){}
}
