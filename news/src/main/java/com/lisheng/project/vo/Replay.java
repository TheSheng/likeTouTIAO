package com.lisheng.project.vo;


import java.io.Serializable;
import java.util.List;


public class Replay implements Serializable {
    //评论id
    private Integer id;
    //文章ID
    private Integer articleId;
    //评论文id
    private Integer userId;
    //用户头像
    private String  img;
    //回复人
    private String  replyName;
    //被回复人
    private String beReplyName;
    //回复内容
    private  String content;
    //回复时间
    private String time;
    //回复地址
    private String  address;
    //系统
    private String  osname;
    //浏览器
    private String browse;
    //评论回复
    private List<Replay> replyBody;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getReplyName() {
        return replyName;
    }

    public void setReplyName(String replyName) {
        this.replyName = replyName;
    }

    public String getBeReplyName() {
        return beReplyName;
    }

    public void setBeReplyName(String beReplyName) {
        this.beReplyName = beReplyName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOsname() {
        return osname;
    }

    public void setOsname(String osname) {
        this.osname = osname;
    }

    public String getBrowse() {
        return browse;
    }

    public void setBrowse(String browse) {
        this.browse = browse;
    }

    public List<Replay> getReplyBody() {
        return replyBody;
    }

    public void setReplyBody(List<Replay> replyBody) {
        this.replyBody = replyBody;
    }

    public Replay(Integer id, Integer articleId, Integer userId, String img, String replyName, String beReplyName, String content, String time, String address, String osname, String browse, List<Replay> replyBody) {
        this.id = id;
        this.articleId = articleId;
        this.userId = userId;
        this.img = img;
        this.replyName = replyName;
        this.beReplyName = beReplyName;
        this.content = content;
        this.time = time;
        this.address = address;
        this.osname = osname;
        this.browse = browse;
        this.replyBody = replyBody;
    }
    public  Replay(){}
}
