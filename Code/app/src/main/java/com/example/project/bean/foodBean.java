package com.example.project.bean;

import java.io.Serializable;

public class foodBean implements Serializable {
    private String title;
    private String detail;
    private String desc;
    private int picId;
    private String type;
    private int numberInCart;

    public foodBean(String title, String detail, String desc, int picId, String type) {
        this.title = title;
        this.detail = detail;
        this.desc = desc;
        this.picId = picId;
        this.type = type;
    }

    public foodBean(String title, String detail, String desc, int picId, String type, int numberInCart) {
        this.title = title;
        this.detail = detail;
        this.desc = desc;
        this.picId = picId;
        this.type = type;
        this.numberInCart = numberInCart;
    }

    public foodBean(String title, String detail, String desc, int picId, int numberInCart) {
        this.title = title;
        this.detail = detail;
        this.desc = desc;
        this.picId = picId;
        this.numberInCart = numberInCart;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getPicId() {
        return picId;
    }

    public void setPicId(int picId) {
        this.picId = picId;
    }

    public String getType() {
        return type;
    }

    public int getNumberInCart() {
        return numberInCart;
    }
    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }
}
