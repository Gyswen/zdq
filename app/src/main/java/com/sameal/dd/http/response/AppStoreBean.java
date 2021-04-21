package com.sameal.dd.http.response;

public class AppStoreBean {

    /**
     * id : 1
     * is_check : 2
     * android_version : 1.0.0
     * ios_version : 1.0.1
     * remark : 华为应用市场，1开启审核  0 正常未提交  2审核通过
     * app_store : huawei
     */

    private int id;
    private int is_check;
    private String android_version;
    private String ios_version;
    private String remark;
    private String app_store;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIs_check() {
        return is_check;
    }

    public void setIs_check(int is_check) {
        this.is_check = is_check;
    }

    public String getAndroid_version() {
        return android_version;
    }

    public void setAndroid_version(String android_version) {
        this.android_version = android_version;
    }

    public String getIos_version() {
        return ios_version;
    }

    public void setIos_version(String ios_version) {
        this.ios_version = ios_version;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getApp_store() {
        return app_store;
    }

    public void setApp_store(String app_store) {
        this.app_store = app_store;
    }
}
