package com.xxxx.crm.query;

import com.xxxx.crm.base.BaseQuery;
import org.springframework.stereotype.Component;

/**
 * &#064;Author:  LingWeiBo
 * &#064;Date:  2025/6/30 09:03
 *  查询条件
 */
@Component
public class SaleChanceQuery extends BaseQuery {

    // 营销机会管理查询条件
    private String customerName;
    private String createMan;
    private Integer state;

    //客户开发计划条件查询
    private String devResult;//开发状态
    private Integer assignMan;//支配人

    public Integer getAssignMan() {
        return assignMan;
    }

    public void setAssignMan(Integer assignMan) {
        this.assignMan = assignMan;
    }

    public String getDevResult() {
        return devResult;
    }

    public void setDevResult(String devResult) {
        this.devResult = devResult;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
