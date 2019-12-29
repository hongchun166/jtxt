
package com.linkb.jstx.activity.setting;

import android.content.Intent;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.view.ViewGroup;

import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.app.SkinManager;
import com.linkb.R;

public class ThemeSettingActivity extends BaseActivity {
    private GridLayout mGridLayout;

    @Override
    public void initComponents() {
        mGridLayout = findViewById(R.id.gridLayout);
        showAngleMark();
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_theme_setting;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.title_theme_setting;
    }


    public void onThemeChanged(View view) {
        SkinManager.onThemeChanged(view.getTag().toString());
        LvxinApplication.sendLocalBroadcast(new Intent(Constant.Action.ACTION_THEME_CHANGED));
        clearAngleMark();
        showAngleMark();
    }

    private void showAngleMark() {
        String name = SkinManager.getThemeName();
        ((ViewGroup) mGridLayout.findViewWithTag(name)).getChildAt(0).setVisibility(View.VISIBLE);
    }

    private void clearAngleMark() {
        for (int i = 0; i < mGridLayout.getChildCount(); i++) {
            ((ViewGroup) mGridLayout.getChildAt(i)).getChildAt(0).setVisibility(View.GONE);
        }
    }
}
