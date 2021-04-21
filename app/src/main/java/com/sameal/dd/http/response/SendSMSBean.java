package com.sameal.dd.http.response;

import java.io.Serializable;

public class SendSMSBean implements Serializable {

    /**
     * code : 916724
     */

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
