package com.xxxx.crm.model;

/**
 * &#064;Author:  LingWeiBo
 * &#064;Date:  2025/7/4 20:13
 */
public class TreeModule {

    private Integer id;
    private Integer pId;
    private String name;
    private Boolean checked=false;

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {this.checked = checked;}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
