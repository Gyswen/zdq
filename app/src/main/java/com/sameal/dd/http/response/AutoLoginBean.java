package com.sameal.dd.http.response;

public class AutoLoginBean {

    /**
     * phone : 15552868599
     * resultCode : 0
     */

    private String phone;
    private String resultCode;

    public String getPhone() {
        return phone == null ? "" : phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getResultCode() {
        return resultCode == null ? "" : resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }
}
