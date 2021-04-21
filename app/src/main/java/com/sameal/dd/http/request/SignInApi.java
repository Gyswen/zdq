package com.sameal.dd.http.request;

import com.hjq.http.config.IRequestApi;
import com.sameal.dd.other.AppConfig;

public class SignInApi implements IRequestApi {

    @Override
    public String getApi() {
        return "api/sists/signIn";
    }

    private int uid = AppConfig.getLoginBean().getId();
}
