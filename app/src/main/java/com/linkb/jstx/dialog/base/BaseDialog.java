package com.linkb.jstx.dialog.base;

import android.app.Dialog;
import android.content.Context;

import com.linkb.R;


/**
 * Dialog基类
 */
public class BaseDialog extends Dialog {

    public BaseDialog(Context context) {
        this(context, R.style.DialogTheme);
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
    }

}