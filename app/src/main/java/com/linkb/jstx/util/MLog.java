
package com.linkb.jstx.util;

import android.util.Log;

import com.linkb.BuildConfig;


public class MLog {

    public static void i(String tag, String log) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, log);
        }
    }

    public static void d(String tag, String log) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, log);
        }
    }

    public static void w(String tag, String log) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, log);
        }
    }

    public static void e(String tag, String log) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, log);
        }
    }

    public static void e(String tag, String log, Throwable throwable) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, log, throwable);
        }
    }
}
