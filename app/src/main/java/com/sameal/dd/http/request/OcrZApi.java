package com.sameal.dd.http.request;

import com.hjq.http.config.IRequestApi;

public class OcrZApi implements IRequestApi {
    @Override
    public String getApi() {
        return "ocr.php";
    }

    private String cardNo;
    private String name;
    private String token;

    public OcrZApi(String cardNo, String name, String token) {
        this.cardNo = cardNo;
        this.name = name;
        this.token = token;
    }
}
