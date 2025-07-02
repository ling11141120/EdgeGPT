package com.xxxx.crm.enums;

public enum StateStatus {
    UNSTATE(0),
    STATED(1);

    private  Integer type;
    StateStatus(Integer type) {
        this.type = type;
    }
    public Integer getType() {
        return type;
    }
}
