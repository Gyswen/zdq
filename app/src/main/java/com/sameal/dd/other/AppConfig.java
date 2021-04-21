package com.sameal.dd.other;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.sameal.dd.BuildConfig;
import com.sameal.dd.helper.SpUtil;
import com.sameal.dd.http.response.LoginBean;

import java.lang.reflect.Method;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2019/09/02
 * desc   : App 配置管理类
 */
public final class AppConfig {

    private static AppConfig instance;
    public static final String ocrBusinessId = "97f8870d1ac24edabf8ed89aaadd75ee";//网易易盾 身份证ocr业务ID

    public static AppConfig getInstance() {
        if (instance == null) {
            return new AppConfig();
        }
        return instance;
    }

    public static int clickPosi = 1;
    public static String APP_ID = "wxd3259b7872b02e13";
    public static boolean isClick = false;

    public static boolean isIsClick() {
        return isClick;
    }

    public static void setIsClick(boolean isClick) {
        AppConfig.isClick = isClick;
    }

    public static int getClickPosi() {
        return clickPosi;
    }

    public static void setClickPosi(int clickPosi) {
        AppConfig.clickPosi = clickPosi;
    }

    private static LoginBean loginBean;

    public static LoginBean getLoginBean() {
        return loginBean == null ? new LoginBean() : loginBean;
    }

    public static void setLoginBean(LoginBean loginBean) {
        AppConfig.loginBean = loginBean;
    }

    /**
     * 是否登录
     *
     * @return
     */
    public static boolean isLogin() {
        return SpUtil.getInstance().getBooleanValue(SpUtil.IS_LOGIN);
    }

    /**
     *
     * 是否审核
     *
     * @return
     */
    public static boolean isCheck() {
        return SpUtil.getInstance().getBooleanValue(SpUtil.IS_CHECK);
    }

    /**
     *
     * 是否第一次打开APP
     *
     * @return
     */
    public static boolean isFirst() {
        return SpUtil.getInstance().getBooleanValue(SpUtil.IS_FIRST);
    }

    /**
     * 当前是否为 Debug 模式
     */
    public static boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    /**
     * 获取当前应用的包名
     */
    public static String getPackageName() {
        return BuildConfig.APPLICATION_ID;
    }

    /**
     * 获取当前应用的版本名
     */
    public static String getVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    /**
     * 获取当前应用的版本码
     */
    public static int getVersionCode() {
        return BuildConfig.VERSION_CODE;
    }

    /**
     * 获取 BuglyId
     */
    public static String getBuglyId() {
        return BuildConfig.BUGLY_ID;
    }

    /**
     * 判断是否开启GPRS
     *
     * @param context
     * @return
     */
    public static boolean gprsIsEnable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State gprsState = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if (gprsState == NetworkInfo.State.CONNECTED || gprsState == NetworkInfo.State.CONNECTING) {
            return true;
        }
        return false;
    }

    /**
     * 获取SN码
     *
     * @return
     */
    public static String getSerialNumber() {
        String serial = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serial;
    }

    /**
     * 验证输入的身份证号是否合法
     */
    public static boolean isLegalId(String id) {
        if (id.toUpperCase().matches("(^\\d{15}$)|(^\\d{17}([0-9]|X)$)")) {
            return true;
        } else {
            return false;
        }
    }

    //判断手机号格式
    public static boolean isMobileNO(String mobiles) {
        //"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "[1][3456789]\\d{9}";
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }
    /**
     * 生成随机数
     *
     * @return
     */
    public static String getRandNum(int num) {
        String strRand = "";
        for (int i = 0; i < num; i++) {
            strRand += String.valueOf((int) (Math.random() * 10));
        }
        return strRand;
    }
}