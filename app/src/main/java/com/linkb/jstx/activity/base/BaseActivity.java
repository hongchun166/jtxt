
package com.linkb.jstx.activity.base;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.SkinManager;
import com.linkb.jstx.util.AppTools;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.dialog.CustomProgressDialog;
import com.linkb.R;
import com.linkb.jstx.util.InputSoftUtils;
import com.linkb.jstx.util.LocalManageUtil;

public abstract class BaseActivity extends AppCompatActivity implements OnClickListener {
    protected Toolbar toolbar;
    private TextView toolbarTitle;

    private CustomProgressDialog progressDialog;
    private BaseInnerReceiver mBaseInnerReceiver;
    private boolean mDestroyed;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(SkinManager.getSkinTheme());
        super.onCreate(savedInstanceState);
        setImmersionBar();
        setContentView(getContentLayout());
        toolbar = findViewById(R.id.TOOLBAR);
        if (toolbar != null) {
            toolbarTitle = toolbar.findViewById(R.id.title);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setElevation(0);
            }
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            setDisplayHomeAsUpEnabled(true);
            toolbar.setElevation(0);
        }
        if (getToolbarTitle() > 0) {
            setToolbarTitle(getString(getToolbarTitle()));
        }

        mBaseInnerReceiver = new BaseInnerReceiver();
        LvxinApplication.registerLocalReceiver(mBaseInnerReceiver, mBaseInnerReceiver.getIntentFilter());
        initComponents();
    }

    public void setImmersionBar() {
        // 所有子类都将继承这些相同的属性,请在设置界面之后设置
        ImmersionBar.with(this)
                .keyboardEnable(true)
                .init();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }

    protected boolean getSwipeBackEnable() {
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    protected void setBackIcon(int resId) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(resId);
        }
    }

    protected void setToolbarTitle(String t) {
        if (toolbarTitle != null) {
            toolbarTitle.setText(t);
        }
    }

    public void setTitle(int t) {
        setToolbarTitle(getString(t));
    }

    protected void setDisplayHomeAsUpEnabled(boolean f) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(f);
        }
    }


    protected void clearTitleDrawableRight() {
        ((TextView) toolbar.findViewById(R.id.title)).setCompoundDrawables(null, null, null, null);

    }

    protected void setTitleDrawableRight(@DrawableRes int resId) {
        Drawable image = ContextCompat.getDrawable(this, resId);
        image.setBounds(0, 0, (int) (Resources.getSystem().getDisplayMetrics().density * 20), (int) (Resources.getSystem().getDisplayMetrics().density * 20));
        ((TextView) toolbar.findViewById(R.id.title)).setCompoundDrawables(null, null, image, null);
    }

    public void setLogo(int rid) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setLogo(rid);
        }
    }

    protected abstract void initComponents();

    protected abstract int getContentLayout();

    protected int getToolbarTitle() {
        return 0;
    }

    private void showProgressDialog(String content, boolean cancancelable) {
        if (progressDialog != null && progressDialog.isShowing()) {
            return;
        }
        progressDialog = new CustomProgressDialog(this, R.style.CustomDialogStyle);
        progressDialog.show();
        progressDialog.setMessage(content);
    }

    public void showProgressDialog(String content) {
        showProgressDialog(content, true);
    }

    public void hideProgressDialog() {

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }

    public void showToastView(String text) {
        AppTools.showToastView(this, text);
    }

    public void showToastView(@StringRes int id) {
        showToastView(getString(id));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 必须调用该方法，防止内存泄漏
        ImmersionBar.with(this).destroy();
/*
        if (getSwipeBackEnable()){
            SwipeBackHelper.onDestroy(this);
        }*/
        mDestroyed = true;
        LvxinApplication.unregisterLocalReceiver(mBaseInnerReceiver);
        hideProgressDialog();
    }

    @Override
    public boolean isDestroyed() {
        return mDestroyed;
    }

    @Override
    public void finish() {
        InputSoftUtils.hideSoftInput(this);
        super.finish();
        Global.saveAppInBackground(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        Global.saveAppInBackground(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        Global.saveAppInBackground(false);
        Global.saveTopActivityClassName(this.getClass());
    }

    public void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    public void setWindowFullscreen() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        getWindow().setAttributes(params);
    }

    protected void hideNavicationBar() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void setTheme(int resid) {
        super.setTheme(resid);
        onThemeChanged();
    }

    protected void onThemeChanged() {
        TypedArray statusbarArray = obtainStyledAttributes(new int[]{android.R.attr.colorPrimary});
        setStatusBarColor(statusbarArray.getColor(0, 0));
        statusbarArray.recycle();

        if (toolbar != null) {
            TypedArray toolbarArray = obtainStyledAttributes(new int[]{R.attr.toolbarColor});
            toolbar.setBackgroundColor(toolbarArray.getColor(0, 0));
            toolbarArray.recycle();
        }
    }


    public class BaseInnerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(Constant.Action.ACTION_FINISH_ACTIVITY)) {
                finish();
            } else if (intent.getAction().equals(Constant.Action.ACTION_THEME_CHANGED)) {

                setTheme(SkinManager.getSkinTheme());
                onThemeChanged();
            }

        }

        IntentFilter getIntentFilter() {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.Action.ACTION_FINISH_ACTIVITY);
            filter.addAction(Constant.Action.ACTION_THEME_CHANGED);
            return filter;
        }
    }
}
