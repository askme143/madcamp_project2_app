package com.example.madcampserverapp.ui.home;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Post {
    /*image, goods name, goods location, goods price*/
    private ArrayList<Bitmap> goods_images;
    private String goods_name;
    private int goods_price;
    private String goods_location;
    private String goods_detail;

    private String post_id;

    private String name;
    /*like count*/
    private int like_cnt;

    public Post(ArrayList<Bitmap> goods_images, String goods_name, int goods_price, String goods_location, String goods_detail, int like_cnt, String name, String post_id){
        this.goods_name = goods_name;
        this.goods_price = goods_price;
        this.goods_location = goods_location;
        this.goods_images = goods_images;
        this.goods_detail = goods_detail;
        this.name = name;
        this.like_cnt = like_cnt;
        this.post_id = post_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public int getGoods_price() {
        return goods_price;
    }

    public String getGoods_location() {
        return goods_location;
    }

    public ArrayList<Bitmap> getGoods_images() {
        return goods_images;
    }

    public String getGoods_detail() {
        return goods_detail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLike_cnt() {
        return like_cnt;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public void setGoods_price(int goods_price) {
        this.goods_price = goods_price;
    }

    public void setGoods_location(String goods_location) {
        this.goods_location = goods_location;
    }

    public void setGoods_images(ArrayList<Bitmap> goods_images) {
        this.goods_images = goods_images;
    }

    public void setGoods_detail(String goods_detail) {
        this.goods_detail = goods_detail;
    }

    public void setLike_cnt(int like_cnt) {
        this.like_cnt = like_cnt;
    }

    public String getPostID() { return post_id; }

    public void setPostID(String postID) { this.post_id = postID; }

    public void increaseLikeCnt() { this.like_cnt++; }

    public void decreaseLikeCnt() { this.like_cnt--; }
}
