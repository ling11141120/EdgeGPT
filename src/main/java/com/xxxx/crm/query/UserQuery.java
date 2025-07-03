package com.xxxx.crm.query;

import com.xxxx.crm.base.BaseQuery;

/**
 * &#064;Author:  LingWeiBo
 * &#064;Date:  2025/7/3 14:33
 */
public class UserQuery extends BaseQuery {

    private String userName;
    private String email;
    private String phone;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
