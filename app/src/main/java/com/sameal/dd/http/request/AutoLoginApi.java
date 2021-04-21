package com.sameal.dd.http.request;

import com.hjq.http.config.IRequestApi;

public class AutoLoginApi implements IRequestApi {
    @Override
    public String getApi() {
        return "OneclickDemo.php";
    }

    private String token;
    private String accessToken;

    public AutoLoginApi(String token, String accessToken) {
        this.token = token;
        this.accessToken = accessToken;
    }
}
