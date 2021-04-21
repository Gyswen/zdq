package com.sameal.dd.http.request;

import com.hjq.http.config.IRequestApi;
import com.sameal.dd.other.AppConfig;

public class PublicApi implements IRequestApi {

    private String api;

    private int uid;

    public PublicApi(String api) {
        this.api = api;
        this.uid = AppConfig.getLoginBean().getId();
    }

    @Override
    public String getApi() {
        return api;
    }
}
