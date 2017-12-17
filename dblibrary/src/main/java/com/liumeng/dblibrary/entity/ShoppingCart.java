package com.liumeng.dblibrary.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/11/13.
 */
@Entity
public class ShoppingCart {

    @Id
    private Long id; // id
    private String name; // 名字
    private String title; // 标题
    private double price; // 价格
    private String quantifier;//量词 箱/袋/盒 ...
    private int reserve;// 库存
    private int sales; // 销量
    private String imgUrl;// 图片链接
    private String num;// 购买数量
    @Generated(hash = 1941311161)
    public ShoppingCart(Long id, String name, String title, double price,
            String quantifier, int reserve, int sales, String imgUrl, String num) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.price = price;
        this.quantifier = quantifier;
        this.reserve = reserve;
        this.sales = sales;
        this.imgUrl = imgUrl;
        this.num = num;
    }
    @Generated(hash = 629579973)
    public ShoppingCart() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public double getPrice() {
        return this.price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getQuantifier() {
        return this.quantifier;
    }
    public void setQuantifier(String quantifier) {
        this.quantifier = quantifier;
    }
    public int getReserve() {
        return this.reserve;
    }
    public void setReserve(int reserve) {
        this.reserve = reserve;
    }
    public int getSales() {
        return this.sales;
    }
    public void setSales(int sales) {
        this.sales = sales;
    }
    public String getImgUrl() {
        return this.imgUrl;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    public String getNum() {
        return this.num;
    }
    public void setNum(String num) {
        this.num = num;
    }
}
