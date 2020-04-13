package com.lisheng.project.constant;



public class ResponseBody<T> {
    private  String message;
    private   T  body;
    private  Integer code;

    public ResponseBody(String message, T body, Integer code) {
        this.message = message;
        this.body = body;
        this.code = code;
    }
    public  ResponseBody(){

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
