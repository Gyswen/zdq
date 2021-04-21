package com.sameal.dd.http.request;

import com.hjq.http.config.IRequestApi;
import com.hjq.http.config.IRequestType;
import com.hjq.http.model.BodyType;

import java.io.File;

public class UpLoadApi implements IRequestApi {
    @Override
    public String getApi() {
        return "api/Common/upload";
    }

    private File file;

    public UpLoadApi setFile(File file) {
        this.file = file;
        return this;
    }
}
