package com.liumeng.dblibrary.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Keep;

/**
 * Created by Administrator on 2017/11/13.
 */
@Entity
public class User {
    @Id(autoincrement = true)
    private Long id;
    private String username;
    private String name;
    private String address;
    private String password;
    private String avatar;
    private boolean login;
    private String sex;
    private String contact;
    private String QQ;

    @Generated(hash = 1782278833)
    public User(Long id, String username, String name, String address, String password, String avatar,
            boolean login, String sex, String contact, String QQ) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.address = address;
        this.password = password;
        this.avatar = avatar;
        this.login = login;
        this.sex = sex;
        this.contact = contact;
        this.QQ = QQ;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getQQ() {
        return QQ;
    }

    public void setQQ(String QQ) {
        this.QQ = QQ;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getLogin() {
        return this.login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


}
