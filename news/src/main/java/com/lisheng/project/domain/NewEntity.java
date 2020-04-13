package com.lisheng.project.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "news")

public class NewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "author")
    private String author;
    @Column(name="article")
    private String article;
    @Column(name = "tag")
    private String tag;
    @Column(name="time")
    private String time;
    @Column(name = "url")
    private String url;
    @Column(name="title")
    private String title;
    @Column(name = "imgurl")
    private String imgurl;
    @Column(name = "type")
    private String type;

    public NewEntity() {
        
    }

    public NewEntity(String author, String article, String tag, String time, String url, String title, String imgurl, String type) {
        this.author = author;
        this.article = article;
        this.tag = tag;
        this.time = time;
        this.url = url;
        this.title = title;
        this.imgurl = imgurl;
        this.type = type;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getArticle() {
        return article;
    }

    public String getTag() {
        return tag;
    }

    public String getTime() {
        return time;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getType() {
        return type;
    }
}
