package com.xxxx.crm.model;

/**
 * &#064;Author:  LingWeiBo
 * &#064;Date:  2025/6/27 20:26
 */
public class UserModel {

    //private  Integer userId;
    private  String userName;
    private  String trueName;
    private  String userIdStr;
   /* public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }*/

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getUserIdStr() {
        return userIdStr;
    }

    public void setUserIdStr(String userIdStr) {
        this.userIdStr = userIdStr;
    }
}
