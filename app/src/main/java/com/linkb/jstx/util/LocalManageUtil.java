package com.linkb.jstx.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.util.Log;

import com.linkb.R;
import com.linkb.jstx.app.ClientConfig;

import java.util.Locale;

public class LocalManageUtil {

    private static final String TAG = LocalManageUtil.class.getName();

    public static Locale getSystemLocal(Context context) {
        return ClientConfig.getInstance().getSystemCurrentLocal();
    }

    public static String getSelectLanguage(Context context) {
        switch (ClientConfig.getLanguageType(context)){
            case 0:
                return context.getString(R.string.language_auto);
            case 1:
                return context.getString(R.string.language_cn);
            case 2:
                return context.getString(R.string.language_traditional);
            case 3:
            default:
                return context.getString(R.string.language_en);
        }
    }

    /**
     * 获取选择的语言设置
     *
     * @param context
     * @return
     */
    public static Locale getSetLanguageLocale(Context context) {

        switch (ClientConfig.getLanguageType(context)) {
            case 0:
                return getSystemLocal(context);
            case 1:
                return Locale.CHINA;
            case 2:
                return Locale.TAIWAN;
            case 3:
            default:
                return Locale.ENGLISH;
        }
    }

    public static void saveSelectLanguage(Context context, int select) {
        ClientConfig.setLanguageType(select);
        setApplicationLanguage(context);
    }

    public static Context setLocal(Context context) {
        return updateResources(context, getSetLanguageLocale(context));
    }
    /**
     * 设置语言类型
     */
    public static void setApplicationLanguage(Context context) {
        Resources resources = context.getApplicationContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();

        Locale locale = getSetLanguageLocale(context);
        configuration.locale = locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList localeList = new LocaleList(locale);
            LocaleList.setDefault(localeList);
            configuration.setLocales(localeList);
            context.getApplicationContext().createConfigurationContext(configuration);
            Locale.setDefault(locale);
        }
        resources.updateConfiguration(configuration, dm);
    }

    private static Context updateResources(Context context, Locale locale) {
         Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        }else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return context;
    }

    public static void saveSystemCurrentLanguage(Context context) {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = Locale.getDefault();
        }
        Log.d(TAG, locale.getLanguage());
        ClientConfig.getInstance().setSystemCurrentLocal(locale);
    }

    public static void onConfigurationChanged(Context context){
        saveSystemCurrentLanguage(context);
        setLocal(context);
        setApplicationLanguage(context);
    }
}
