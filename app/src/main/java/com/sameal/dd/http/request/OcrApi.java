package com.sameal.dd.http.request;

import com.hjq.http.config.IRequestApi;
import com.hjq.http.config.IRequestHost;
import com.hjq.http.config.IRequestType;
import com.hjq.http.model.BodyType;
import com.sameal.dd.helper.OcrSign;
import com.sameal.dd.helper.TimeUtil;
import com.sameal.dd.other.AppConfig;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OcrApi implements IRequestApi, IRequestType, IRequestHost {
    @Override
    public String getApi() {
        return "";
    }

    @Override
    public String getHost() {
        return "https://verify.dun.163.com/v1/ocr/check";
    }

    @Override
    public BodyType getType() {
        return BodyType.FORM;
    }

    private int picType;

    private String frontPicture;
    private String backPicture;
    private String secretId = "58e10d514939dbe960591d4ce5ee15e2";
    private String businessId = AppConfig.ocrBusinessId;
    private String version = "v1";
    private long timestamp = TimeUtil.getCurrentTimeMillis();
    private String nonce = String.valueOf(new Random().nextInt());
    private String signature;

    public OcrApi(int picType, String frontPicture, String backPicture) {
        this.picType = picType;
        this.frontPicture = frontPicture;
        this.backPicture = backPicture;
        Map<String, String> map = new HashMap<>();
        map.put("secretId", this.secretId);
        map.put("businessId", this.businessId);
        map.put("version", this.version);
        map.put("timestamp", this.timestamp + "");
        map.put("nonce", this.nonce);
        map.put("picType", this.picType + "");
        map.put("frontPicture", this.frontPicture);
        map.put("backPicture", this.backPicture);
        try {
            this.signature = OcrSign.genSignature("9edca92e22a240a42443e04f3ee1ecb6", map);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
