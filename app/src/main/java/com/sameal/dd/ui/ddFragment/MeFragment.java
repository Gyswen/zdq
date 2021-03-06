package com.sameal.dd.ui.ddFragment;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.hjq.widget.view.CircleImageView;
import com.mob.secverify.datatype.VerifyResult;
import com.netease.cloud.nos.android.utils.LogUtil;
import com.netease.nis.quicklogin.QuickLogin;
import com.netease.nis.quicklogin.listener.QuickLoginPreMobileListener;
import com.netease.nis.quicklogin.listener.QuickLoginTokenListener;
import com.sameal.dd.R;
import com.sameal.dd.aop.SingleClick;
import com.sameal.dd.common.MyApplication;
import com.sameal.dd.common.MyFragment;
import com.sameal.dd.helper.ActivityStackManager;
import com.sameal.dd.helper.Identifier;
import com.sameal.dd.helper.LogUtils;
import com.sameal.dd.helper.QuickLoginUiConfig;
import com.sameal.dd.helper.SpUtil;
import com.sameal.dd.http.model.HttpData;
import com.sameal.dd.http.request.AutoLoginApi;
import com.sameal.dd.http.response.AutoLoginBean;
import com.sameal.dd.http.response.BannerListBean;
import com.sameal.dd.http.response.CheckRegBean;
import com.sameal.dd.http.response.LoginBean;
import com.sameal.dd.http.response.MsgBean;
import com.sameal.dd.http.response.UpLoadBean;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.other.IntentKey;
import com.sameal.dd.ui.ddActivity.AboutMineActivity;
import com.sameal.dd.ui.ddActivity.AccountSafetyActivity;
import com.sameal.dd.ui.ddActivity.ExchangeActivity;
import com.sameal.dd.ui.ddActivity.GuessActivity;
import com.sameal.dd.ui.ddActivity.LoginActivity;
import com.sameal.dd.ui.ddActivity.MainActivity;
import com.sameal.dd.ui.ddActivity.MeBannerActivity;
import com.sameal.dd.ui.ddActivity.MoneyDetailActivity;
import com.sameal.dd.ui.ddActivity.MsgCenterActivity;
import com.sameal.dd.ui.ddActivity.MyTgActivity;
import com.sameal.dd.ui.ddActivity.ProgramActivity;
import com.sameal.dd.ui.ddActivity.RechargeActivity;
import com.sameal.dd.ui.ddActivity.RegisterActivity;
import com.sameal.dd.ui.ddActivity.ServiceActivity;
import com.sameal.dd.ui.ddActivity.SettingsActivity;
import com.sameal.dd.ui.ddActivity.ShoppActivity;
import com.sameal.dd.ui.ddActivity.UserInfoActivity;
import com.sameal.dd.ui.ddActivity.VerifiedActivity;
import com.sameal.dd.ui.ddActivity.VipActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import okhttp3.Call;

/**
 * @author zhangj
 * @date 2020/12/16 23:17
 * desc ????????????
 */
public class MeFragment extends MyFragment {

    public static MeFragment newInstance() {
        return new MeFragment();
    }

    private TextView tvNum;
    private TextView tvUserName;
    private ImageView img_level;
    private CircleImageView imgAvatar;
    private TextView tvMoney;
    private BGABanner bgaBanner;

    private String errMsg;
    QuickLogin login = QuickLogin.getInstance(MyApplication.sInstance, "7f2ed00edafe4a9a83d8d394903bc8e2");
    private List<BannerListBean> bannerListBeans;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initView() {
        tvNum = (TextView) findViewById(R.id.tv_num);
        tvUserName = (TextView) findViewById(R.id.tv_user_name);
        tvMoney = (TextView) findViewById(R.id.tv_money);
        img_level = (ImageView) findViewById(R.id.img_level);
        imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);
        bgaBanner = (BGABanner) findViewById(R.id.banner);
        setOnClickListener(R.id.ll_setting, R.id.rl_message, R.id.ll_user_info, R.id.ll_xlsc, R.id.tv_recharge,
                R.id.ll_wdjc, R.id.ll_womx, R.id.ll_zjfa, R.id.ll_vip, R.id.ll_kf, R.id.ll_gywm, R.id.ll_wdtg, R.id.ll_zhyaq);
    }

    @Override
    protected void initData() {
        setData();
    }

    private void setData() {
        bgaBanner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                Glide.with(getActivity())
                        .load(model)
                        .placeholder(R.drawable.avatar_placeholder_ic)
                        .error(R.mipmap.cj_bg)
                        .centerCrop()
                        .dontAnimate()
                        .into(itemView);
            }

        });

        bgaBanner.setDelegate(new BGABanner.Delegate<ImageView, String>() {
            @Override
            public void onBannerItemClick(BGABanner banner, ImageView itemView, @Nullable String model, int position) {
                if (bannerListBeans != null && bannerListBeans.size() > position) {
                    if (!TextUtils.isEmpty(bannerListBeans.get(position).getLinks()) &&  bannerListBeans.get(position).getLinks().startsWith("http")){
                        if (AppConfig.isLogin()) {
                            if (AppConfig.getLoginBean().getReal_status() == 2) {
                                Intent intent = new Intent(getActivity(), MeBannerActivity.class);
                                intent.putExtra(IntentKey.TITLE, " ");
                                intent.putExtra(IntentKey.ADDRESS, bannerListBeans.get(position).getLinks());
                                startActivity(intent);
                            } else {
                                toast("??????????????????");
                                startActivity(VerifiedActivity.class);
                            }
                        } else {
                            startActivity(LoginActivity.class);
                        }
                    }
                }
//                toast("????????????");
            }
        });

        addOnVisible(new OnVisible() {
            @Override
            public void isUsetVisible(boolean isVisible) {
                if (isVisible) {
                    getUserInfo();
                    getMsgList();
                    prefetch();
                }
            }
        });

        getBannerList();

    }

    @Override
    public void onStart(Call call) {
        super.onStart(call);
        hideDialog();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SpUtil.getInstance().getBooleanValue(SpUtil.IS_LOGIN)) {
            getUserInfo();
            getMsgList();
        } else {
            tvUserName.setText(R.string.no_login);
        }
        prefetch();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            if (requestCode == 0) {
                getUserInfo();
                getMsgList();
                getBannerList();
            }
        }
    }

    /**
     * ????????????-?????????
     */
    private void prefetch() {
        login.setUnifyUiConfig(QuickLoginUiConfig.getUiConfig(getActivity(), new QuickLoginUiConfig.OnListener() {
            @Override
            public void otherLogin() {
                login.quitActivity();
                startActivity(LoginActivity.class);
            }

            @Override
            public void onClickEventListener(int viewType) {

            }
        }));
        login.prefetchMobileNumber(new QuickLoginPreMobileListener() {
            @Override
            public void onGetMobileNumberSuccess(String YDToken, final String mobileNumber) {
                // ???:2.0.0?????????????????????????????????????????????????????????onePass??????
                LogUtils.d(TAG, "onGetMobileNumberSuccess: " + YDToken + ":" + mobileNumber);
                errMsg = "";
            }

            @Override
            public void onGetMobileNumberError(String YDToken, final String msg) {
                LogUtils.d(TAG, "onGetMobileNumberError: " + YDToken + ":" + msg);
                errMsg = msg;
            }
        });
    }

    /**
     * ????????????????????????
     *
     * @param token
     * @param acessToken
     */
    private void autoLogin(String token, String acessToken) {
        EasyHttp.post(this)
                .api(new AutoLoginApi(token, acessToken))
                .request(new HttpCallback<HttpData<AutoLoginBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<AutoLoginBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 200) {
                            checkReg(result.getData().getPhone());
                        } else {
                            toast(result.getMessage());
                        }
                    }
                });
    }

    /**
     * ????????????????????????
     */
    private void checkReg(String phone) {
        EasyHttp.get(this)
                .api("api/sists/checkReg?mobile="+phone)
                .request(new HttpCallback<HttpData<CheckRegBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<CheckRegBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1){
                            if (result.getData().getIs_reg() == 1){
                                autoLoginInfo(phone);
                            } else {
                                toast(result.getMessage());
                                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            toast(result.getMessage());
                        }
                    }
                });
    }

    /**
     * ?????????????????????????????????
     */
    private void autoLoginInfo(String phone) {
        EasyHttp.get(this)
                .api("api/sists/autoLogininfo?mobile=" + phone + "&client_id=" + util.MD5.md5Str(Identifier.getSN()))
                .request(new HttpCallback<HttpData<LoginBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<LoginBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            SpUtil.getInstance().setBooleanValue(SpUtil.IS_LOGIN, true);
                            SpUtil.getInstance().setStringValue(SpUtil.UID, result.getData().getId() + "");
                            AppConfig.setLoginBean(result.getData());
                            startActivity(MainActivity.class);
                            // ????????????????????????????????????????????????????????????
                            ActivityStackManager.getInstance().finishAllActivities(MainActivity.class);
                        } else {
                            toast(result.getMessage());
                            startActivity(RegisterActivity.class);
                        }
                    }
                });
    }

    @SingleClick
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_setting://??????
                startActivity(SettingsActivity.class);
                break;
            case R.id.rl_message://??????
                if (AppConfig.isLogin()) {
                    startActivity(MsgCenterActivity.class);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.ll_user_info://????????????
                if (SpUtil.getInstance().getBooleanValue(SpUtil.IS_LOGIN)) {
                    startActivity(UserInfoActivity.class);
                } else {
                    if (TextUtils.isEmpty(errMsg)) {
                        login.onePass(new QuickLoginTokenListener() {
                            @Override
                            public void onGetTokenSuccess(String s, String s1) {
                                LogUtils.d(TAG, "onGetTokenSuccess: " + s + ":" + s1);
                                login.quitActivity();
                                autoLogin(s, s1);
                            }

                            @Override
                            public void onGetTokenError(String s, String s1) {
                                LogUtils.d(TAG, "onGetTokenSuccess: " + s + ":" + s1);
                                startActivity(LoginActivity.class);
                                toast(s1);
                            }
                        });
                    } else {
                        startActivity(LoginActivity.class);
                    }
                }
                break;
            case R.id.tv_recharge://??????
                if (AppConfig.isLogin()) {
                    if (AppConfig.getLoginBean().getReal_status() == 2) {
                        //??????H5???????????????
//                        Intent intent = new Intent(getActivity(),ShoppActivity.class);
//                        intent.putExtra(SpUtil.TYPE,1);
//                        startActivityForResult(intent,0);
                        startActivity(RechargeActivity.class);
                    } else {
                        toast("??????????????????");
                        startActivity(VerifiedActivity.class);
                    }
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.ll_xlsc://?????????
                if (AppConfig.isLogin()) {
                    if (AppConfig.getLoginBean().getIs_test() != 1){
                        if (AppConfig.getLoginBean().getReal_status() == 2){
                            Intent intent = new Intent(getActivity(),ShoppActivity.class);
                            intent.putExtra(SpUtil.TYPE,0);
                            startActivityForResult(intent,0);
                        }else {
                            toast("??????????????????");
                            startActivity(VerifiedActivity.class);
                        }
                    } else {
                        toast("??????????????????????????????");
                    }
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.ll_wdjc://????????????
                if (AppConfig.isLogin()) {
                    startActivity(GuessActivity.class);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.ll_womx://????????????
                if (AppConfig.isLogin()) {
                    startActivity(MoneyDetailActivity.class);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.ll_zjfa://????????????
                if (AppConfig.isLogin()) {
                    startActivity(ProgramActivity.class);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.ll_vip://VIP
                if (AppConfig.isLogin()) {
                    startActivity(VipActivity.class);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.ll_kf://??????
                startActivity(ServiceActivity.class);
                break;
            case R.id.ll_gywm://????????????
                startActivity(AboutMineActivity.class);

                break;
            case R.id.ll_wdtg://????????????
                if (AppConfig.isLogin()) {
                    startActivity(MyTgActivity.class);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.ll_zhyaq://???????????????
                if (AppConfig.isLogin()) {
                    startActivity(AccountSafetyActivity.class);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
        }
    }

    /**
     * ??????????????????
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
                            tvUserName.setText(AppConfig.getLoginBean().getNickname());
                            Glide.with(getActivity()).load(AppConfig.getLoginBean().getAvatar())
                                    .error(R.mipmap.icon_contact_avatar_default)
                                    .placeholder(R.mipmap.icon_contact_avatar_default)
                                    .into(imgAvatar);
                            tvMoney.setText(AppConfig.getLoginBean().getMoney());
                            img_level.setVisibility(View.VISIBLE);
                            switch (result.getData().getLevel_status()) {
                                case 0://??????
                                    img_level.setImageResource(R.mipmap.bronze_chat);
                                    break;
                                case 1://??????
                                    img_level.setImageResource(R.mipmap.silver_chat);
                                    break;
                                case 2://??????
                                    img_level.setImageResource(R.mipmap.gold_chat);
                                    break;
                                case 3://??????
                                    img_level.setImageResource(R.mipmap.platinum_chat);
                                    break;
                                case 4://??????
                                    img_level.setImageResource(R.mipmap.diamond_chat);
                                    break;
                            }
                        }
                    }
                });
    }


    /**
     * ??????????????????
     */
    private void getMsgList() {
        EasyHttp.get(this)
                .api("api/User/getMessage?code=0")
                .request(new HttpCallback<HttpData<List<MsgBean>>>(this) {
                    @Override
                    public void onSucceed(HttpData<List<MsgBean>> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 0) {
                            if (result.getData() != null && result.getData().size() > 0) {
                                tvNum.setText(result.getData().size() + "");
                            } else {
                                tvNum.setVisibility(View.GONE);
                            }
                        } else {
                            tvNum.setVisibility(View.GONE);
                        }
                    }
                });
    }

    /**
     * ?????????????????????
     */
    private void getBannerList() {
        EasyHttp.get(this)
                .api("api/sists/getBannerList?code=2")
                .request(new HttpCallback<HttpData<List<BannerListBean>>>(this) {
                    @Override
                    public void onSucceed(HttpData<List<BannerListBean>> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            bannerListBeans = result.getData();
                            List<String> imgs = new ArrayList<>();
                            for (BannerListBean listBean : result.getData()) {
                                imgs.add(listBean.getImage());
                            }
                            if (imgs.size() < 2) {
                                bgaBanner.setAutoPlayAble(false);
                            }
                            bgaBanner.setData(imgs, imgs);
                        }
                    }
                });
    }

//    /**
//     * ???????????????
//     */
//    private void getBottomPic() {
//        EasyHttp.get(this)
//                .api("api/sists/getHomepic")
//                .request(new HttpCallback<HttpData<UpLoadBean>>(this) {
//                    @Override
//                    public void onSucceed(HttpData<UpLoadBean> result) {
//                        super.onSucceed(result);
//                        if (result.getCode() == 1) {
//                            bannerListBeans = result.getData();
//                            List<String> imgs = new ArrayList<>();
////                            for (BannerListBean listBean : result.getData()) {
////                                imgs.add(listBean.getImage());
////                            }
//                            if (imgs.size() < 2) {
//                                bgaBanner.setAutoPlayAble(false);
//                            }
//                            imgs.add(result.getData().getMyex());
//                            bgaBanner.setData(imgs, imgs);
//                        }
//                    }
//                });
//    }

    private boolean isPreVerifyDone = true;
    private boolean devMode = false;

    /**
     * ????????????
     */
    private void mobLogin(VerifyResult data) {
        EasyHttp.get(this)
                .api("auth.php?token=" + data.getToken() + "&optoken=" + data.getOpToken() + "&operator=" + data.getOperator())
                .request(new HttpCallback<HttpData<LoginBean>>(this) {
                    @Override
                    public void onSucceed(HttpData<LoginBean> result) {
                        super.onSucceed(result);
                        if (result.getCode() == 1) {
                            SpUtil.getInstance().setBooleanValue(SpUtil.IS_LOGIN, true);
                            SpUtil.getInstance().setStringValue(SpUtil.UID, result.getData().getId() + "");
                            AppConfig.setLoginBean(result.getData());
                            getUserInfo();
                        } else {
                            toast(result.getMessage());
                        }
                    }
                });
    }
}