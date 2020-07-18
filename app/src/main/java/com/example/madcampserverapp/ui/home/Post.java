package com.example.madcampserverapp.ui.home;

public class Post {
    //image, goods name, goods location, goods price
    private String goods_name;
    private int goods_price;
    private String goods_location;
    private long goods_photoID;
    //person info
    private int fb_id;
    private String email;
    private String name;
    //like count
    private int like_cnt;

    public Post(long goods_photoID, String goods_name, int goods_price, String goods_location, String email){
        this.goods_location=goods_location;
        this.goods_name=goods_name;
        this.goods_price=goods_price;
        this.goods_photoID=goods_photoID;
        this.email=email;
    }

    public String getGoods_name() { return goods_name; }

    public void setGoods_name(String goods_name) { this.goods_name = goods_name; }

    public int getGoods_price() { return goods_price; }

    public void setGoods_price(int goods_price) { this.goods_price = goods_price; }

    public String getGoods_location() { return goods_location; }

    public void setGoods_location(String goods_location) { this.goods_location = goods_location; }

    public long getGoods_photoID() { return goods_photoID; }

    public void setGoods_photoID(long goods_photoID) { this.goods_photoID = goods_photoID; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }




}
