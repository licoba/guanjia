package com.example.dibage.accountb.entitys;

import java.io.Serializable;

/**
 * Created by zjw on 2020/3/12.
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * _id : 5e68ce69aa0652e5cb5994a9
     * username : licoba
     * password : 123456
     * phone : 17322309201
     * __v : 0
     */

    private String _id;
    private String username;
    private String password;
    private String phone;
    private int __v;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    public User() {
    }

    public User(String username, String password, String phone) {
        this.username = username;
        this.password = password;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
