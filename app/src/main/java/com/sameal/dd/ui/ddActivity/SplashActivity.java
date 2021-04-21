package com.sameal.dd.ui.ddActivity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.sameal.dd.BuildConfig;
import com.sameal.dd.R;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.helper.Identifier;
import com.sameal.dd.helper.LogUtils;
import com.sameal.dd.helper.SpUtil;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.response.LoginBean;
import com.sameal.dd.http.response.UpLoadBean;
import com.sameal.dd.other.AppConfig;

import java.util.Arrays;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * @author zhangj
 * @date 2020/12/16 19:40
 * desc 起始页
 */
public class SplashActivity extends MyActivity {

    RelativeLayout rlOne;
    BGABanner banner;
    LinearLayout llTwo;
    CountDownTimer timer,timer2;

    private String channel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        rlOne = findViewById(R.id.rl_one);
        llTwo = findViewById(R.id.ll_two);
        banner = findViewById(R.id.banner);
    }

    @Override
    protected void initData() {
//        getAdpic();
        LogUtils.d(TAG, BuildConfig.FLAVOR);
        if (SpUtil.getInstance().getBooleanValue(SpUtil.IS_LOGIN) && !TextUtils.isEmpty(SpUtil.getInstance().getStringValue(SpUtil.UID))) {
            getUserInfo();
        }
        timer = new CountDownTimer(1300,1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                startActivity(MainActivity.class);
                finish();
            }
        };
        timer.start();
        timer2 = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long l) {
                //间隔回调
            }

            @Override
            public void onFinish() {
                //倒计时结束回调
                startActivity(MainActivity.class);
                finish();
            }
        };
        banner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                Glide.with(SplashActivity.this)
                        .load(model)
                        .placeholder(R.drawable.avatar_placeholder_ic)
                        .error(R.drawable.hint_error_ic)
                        .centerCrop()
                        .dontAnimate()
                        .into(itemView);
            }
        });
    }

    /**
     * 登录
     */
    private void login(String account, String password) {
        EasyHttp.get(this)
                .api("api/sists/autoLogin?mobile=" + account + "&password=" + password)
                .request(new HttpCallback<HttpData<LoginBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<LoginBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            SpUtil.getInstance().setBooleanValue(SpUtil.IS_LOGIN, true);
                            SpUtil.getInstance().setStringValue(SpUtil.UID, result.getData().getId() + "");
                            AppConfig.setLoginBean(result.getData());
                        }
                        toast(result.getMessage());
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                        toast(e.toString());
                    }
                });
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        EasyHttp.get(this)
                .api("api/sists/getUserinfo?uid=" + SpUtil.getInstance().getStringValue(SpUtil.UID) + "&client_id=" + util.MD5.md5Str(Identifier.getSN()))
                .request(new HttpCallback<HttpData<LoginBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<LoginBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            AppConfig.setLoginBean(result.getData());
                        }else{
                            SpUtil.getInstance().setBooleanValue(SpUtil.IS_LOGIN, false);
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                        SpUtil.getInstance().setBooleanValue(SpUtil.IS_LOGIN, false);
                    }
                });
    }

    /**
     * 获取启动图广告
     */
    private void getAdpic() {
        EasyHttp.get(this)
                .api("api/sists/getAdpic")
                .request(new HttpCallback<HttpData<UpLoadBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<UpLoadBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            banner.setData(Arrays.asList(result.getData().getAdpic()),
                                    Arrays.asList(""));
                            rlOne.setVisibility(View.GONE);
                            llTwo.setVisibility(View.VISIBLE);
                            timer2.start();
                        } else {
                            startActivity(MainActivity.class);
                            finish();
                        }
                    }

                    @Override
                    public void onFail(Exception e) {
                        super.onFail(e);
                        startActivity(MainActivity.class);
                        finish();
                    }
                });
    }
}