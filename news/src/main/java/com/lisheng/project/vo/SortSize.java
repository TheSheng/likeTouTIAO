package com.lisheng.project.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class SortSize {
    private int page;
    private int size;
    private String type;

    public SortSize(int page, int size, String type) {
        this.page = page;
        this.size = size;
        this.type = type;
    }
    public SortSize() {

    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
