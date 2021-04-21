package com.sameal.dd.ui.ddActivity;

import android.Manifest;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.widget.AppCompatButton;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.permissions.XXPermissions;
import com.sameal.dd.R;
import com.sameal.dd.aop.SingleClick;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.helper.Identifier;
import com.sameal.dd.helper.SpUtil;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.response.LoginBean;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.other.IntentKey;

/**
 * 实名认证
 */
public class VerifiedActivity extends MyActivity {

    EditText etRealName;
    EditText etIdCard;
    AppCompatButton btnConfirm;

    private String cardFrontPath, cardBackPath;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_verified;
    }

    @Override
    protected void initView() {
        etRealName = (EditText) findViewById(R.id.et_real_name);
        etIdCard = (EditText) findViewById(R.id.et_id_card);
        btnConfirm = (AppCompatButton) findViewById(R.id.btn_confirm);
        setOnClickListener(R.id.btn_confirm);
    }

    @Override
    protected void initData() {

    }

    @SingleClick
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                if (TextUtils.isEmpty(etRealName.getText().toString())) {
                    toast("请输入真实姓名");
                    return;
                } else if (TextUtils.isEmpty(etIdCard.getText().toString())) {
                    toast("请输入身份证号");
                    return;
                } else if (!AppConfig.isLegalId(etIdCard.getText().toString())) {
                    toast("请输入正确的身份证号");
                    return;
                }
                Intent intent = new Intent(getActivity(),AliveActivity.class);
                intent.putExtra(SpUtil.NAME,etRealName.getText().toString());
                intent.putExtra(SpUtil.TYPE,etIdCard.getText().toString());
                startActivity(intent);
//                ocrCheck();
//                verified();
                break;
        }
    }

    private void jumpOcrScanActivity(String type) {
        if (!XXPermissions.hasPermission(this, Manifest.permission.CAMERA)) {
            toast("您未授予相机权限，请到设置中开启权限");
        } else if (!XXPermissions.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            toast("您未授予文件存储权限，请到设置中开启权限");
        } else {
            Intent intent = new Intent(this, OcrScanActivity.class);
            intent.putExtra(IntentKey.SCANTYPE, type);
            if (type.equals("front")) {
                startActivityForResult(intent, 1002);
            } else {
                startActivityForResult(intent, 1003);
            }
        }
    }

    private String backPicPath;
    private String frontalPicPath;

//    private void ocrCheck() {
//        EasyHttp.post(this)
//                .api(new OcrZApi((AppConfig.isDebug() ? new TestServer().getHost() : new ReleaseServer().getHost()) + cardFrontPath,
//                        (AppConfig.isDebug() ? new TestServer().getHost() : new ReleaseServer().getHost()) + cardBackPath))
//                .request(new HttpCallback<OcrInfoBean>(this) {
//                    @Override
//                    public void onSucceed(OcrInfoBean result) {
//                        super.onSucceed(result);
//                        LogUtils.d(TAG, "onSucceed: "+result );
//                        if (result.getCode() == 200) {
//                            if (result.getResult().getStatus() == 1) {
////                                if (!etRealName.getText().toString().equals(result.getResult().getOcrResponseDetail().getOcrName())) {
////                                    toast("身份证姓名与输入真实姓名不符，请确认");
////                                    return;
////                                } else if (!etIdCard.getText().toString().equals(result.getResult().getOcrResponseDetail().getOcrCardNo())) {
////                                    toast("身份证号码与输入身份证号码不符，请确认");
////                                    return;
////                                }
//                                verified();
//                            } else {
//                                switch (result.getResult().getStatus()) {
//                                    case 2:
//                                        toast("非身份证照片或检测不出身份证信息");
//                                        break;
//                                    case 3:
//                                        toast("识别成功，但身份证号校验不通过，请复查用户证件是否作弊");
//                                        break;
//                                    case 4:
//                                        toast("识别成功，但姓名不合规范，请复查用户证件是否作弊");
//                                        break;
//                                    case 5:
//                                        toast("识别成功，但身份证有效期出现错误，请复查用户证件是否作弊");
//                                        break;
//                                    case 6:
//                                        toast("图片下载失败，请重试");
//                                        break;
//                                    default:
//                                        toast("检测异常");
//                                        break;
//                                }
//                            }
//                        } else {
//                            toast(result.getMsg());
//                        }
//                    }
//                });
//    }

//    private void ocrCheck() {
//        EasyHttp.post(this)
//                .api(new OcrApi(1, new ReleaseServer().getHost() + cardFrontPath, new ReleaseServer().getHost() + cardBackPath))
//                .request(new HttpCallback<OcrInfoBean>(this) {
//                    @Override
//                    public void onSucceed(OcrInfoBean result) {
//                        super.onSucceed(result);
//                        if (result.getCode() == 200) {
//                            if (result.getResult().getStatus() == 1) {
//                                if (!etRealName.getText().toString().equals(result.getResult().getOcrResponseDetail().getOcrName())) {
//                                    toast("身份证姓名与输入真实姓名不符，请确认");
//                                    return;
//                                } else if (!etIdCard.getText().toString().equals(result.getResult().getOcrResponseDetail().getOcrCardNo())) {
//                                    toast("身份证号码与输入身份证号码不符，请确认");
//                                    return;
//                                }
//                                verified();
//                            } else {
//                                switch (result.getResult().getStatus()) {
//                                    case 2:
//                                        toast("非身份证照片或检测不出身份证信息");
//                                        break;
//                                    case 3:
//                                        toast("识别成功，但身份证号校验不通过，请复查用户证件是否作弊");
//                                        break;
//                                    case 4:
//                                        toast("识别成功，但姓名不合规范，请复查用户证件是否作弊");
//                                        break;
//                                    case 5:
//                                        toast("识别成功，但身份证有效期出现错误，请复查用户证件是否作弊");
//                                        break;
//                                    case 6:
//                                        toast("图片下载失败，请重试");
//                                        break;
//                                    default:
//                                        toast("检测异常");
//                                        break;
//                                }
//                            }
//                        } else {
//                            toast(result.getMsg());
//                        }
//                    }
//                });
//    }

//    /**
//     * 上传图片
//     */
//    private void upload(String path, int type) {
//        EasyHttp.post(this)
//                .api(new UpLoadApi()
//                        .setFile(new File(path)))
//                .request(new HttpCallback<HttpData<UpLoadBean>>(this) {
//                    @Override
//                    public void onSucceed(HttpData<UpLoadBean> result) {
//                        super.onSucceed(result);
//                        if (result.getCode() == 1) {
//                            if (type == 1) {
//                                cardFrontPath = result.getData().getUrl();
//                                Glide.with(VerifiedActivity.this)
//                                        .load((AppConfig.isDebug() ? new TestServer().getHost() : new ReleaseServer().getHost()) + cardFrontPath)
//                                        .into(ivAvatar);
//                            } else {
//                                cardBackPath = result.getData().getUrl();
//                                Glide.with(VerifiedActivity.this)
//                                        .load((AppConfig.isDebug() ? new TestServer().getHost() : new ReleaseServer().getHost()) + cardBackPath)
//                                        .into(ivEmblem);
//                            }
//                        }
//                    }
//                });
//    }

    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        EasyHttp.get(this)
                .api("api/sists/getUserinfo?uid=" + AppConfig.getLoginBean().getId() + "&client_id=" + util.MD5.md5Str(Identifier.getSN()))
                .request(new HttpCallback<HttpData<LoginBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<LoginBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            AppConfig.setLoginBean(result.getData());
                            finish();
                        }
                    }
                });
    }

    /**
     * 实名认证
     */
    private void verified() {
        EasyHttp.get(this)
                .api("api/User/realName?uid=" + AppConfig.getLoginBean().getId() + "&real_name=" + etRealName.getText().toString()
                        + "&id_card=" + etIdCard.getText().toString() + "&card_front=" + cardFrontPath + "&card_back=" + cardBackPath)
                .request(new HttpCallback<HttpData<String>>(this) {
                    @Override
                    public void onSucceed(HttpData<String> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            getUserInfo();
                        }
                        toast(result.getMessage());
                    }
                });
    }
}