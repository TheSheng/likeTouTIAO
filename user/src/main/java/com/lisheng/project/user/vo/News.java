package com.lisheng.project.user.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


public class News implements Serializable {

    private Long id;

    private String author;

    private String article;

    private String tag;

    private String time;

    private String url;

    private String title;

    private String imgurl;

    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public News(Long id, String author, String article, String tag, String time, String url, String title, String imgurl, String type) {
        this.id = id;
        this.author = author;
        this.article = article;
        this.tag = tag;
        this.time = time;
        this.url = url;
        this.title = title;
        this.imgurl = imgurl;
        this.type = type;
    }
    public News(){

    }
}
