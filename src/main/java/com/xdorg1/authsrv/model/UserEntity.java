package com.xdorg1.authsrv.model;

import java.util.Date;

public class UserEntity {

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

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int id) { user_id = id; }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "User [id=" + user_id + ", name=" + username + ", password=" + password + ", mobile=" + mobile + ", email=" + email + ", status=" + status + "]" ;
    }

    private String username;
    private String password;
    private Integer user_id;
    private String mobile;
    private String email;
    private Integer status;

}
