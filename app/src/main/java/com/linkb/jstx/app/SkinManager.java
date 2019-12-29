package com.linkb.jstx.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.linkb.R;

public class SkinManager {
    private static final String SKIN_MODEL = "SKIN_MODEL";
    private static final String KEY_SKIN_NAME = "KEY_SKIN_NAME";

    private static final String THEME_BLACK = "black";
    private static final String THEME_RED = "red";
    private static final String THEME_BLUE = "blue";
    private static final String THEME_GREEN = "green";
    private static final String THEME_PURPLE = "purple";
    private static final String THEME_TEAL = "teal";

    public static String getThemeName() {
        return LvxinApplication.getInstance().getSharedPreferences(SKIN_MODEL, Context.MODE_PRIVATE)
                .getString(KEY_SKIN_NAME, THEME_BLUE);
    }

    public static void onThemeChanged(String name) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(SKIN_MODEL, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_SKIN_NAME, name).apply();
    }

    public static int getSkinTheme() {
        String name = getThemeName();
        if (THEME_RED.equals(name)) {
            return R.style.Red_Theme;
        } else if (THEME_BLUE.equals(name)) {
            return R.style.Blue_Theme;
        } else if (THEME_GREEN.equals(name)) {
            return R.style.Green_Theme;
        } else if (THEME_PURPLE.equals(name)) {
            return R.style.Purple_Theme;
        } else if (THEME_TEAL.equals(name)) {
            return R.style.Teal_Theme;
        } else {
            return R.style.Black_Theme;
        }
    }

}
