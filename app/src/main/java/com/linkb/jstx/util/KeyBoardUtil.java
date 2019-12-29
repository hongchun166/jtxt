package com.linkb.jstx.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.linkb.jstx.app.LvxinApplication;

public class KeyBoardUtil {
    /**
     * 弹出软键盘
     */
    public static void onPopSoftInput(final View view) {
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) LvxinApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 200);


    }
}
