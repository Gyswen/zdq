package com.sameal.dd.helper;

import android.util.Log;

import com.sameal.dd.BuildConfig;

public class LogUtils {
    public static boolean ISOPEN = BuildConfig.LOG_DEBUG;

    public static void v(String tag, String msg) {
        if (ISOPEN) {
            Log.v(tag, msg);
        }

    }

    public static void d(String tag, String msg) {
        if (ISOPEN) {
            Log.d(tag, msg);
        }

    }

    public static void i(String tag, String msg) {
        if (ISOPEN) {
            Log.i(tag, msg);
        }

    }

    public static void w(String tag, String msg) {
        if (ISOPEN) {
            Log.w(tag, msg);
        }

    }

    public static void e(String tag, String msg) {
        if (ISOPEN) {
            Log.e(tag, msg);
        }

    }
}
