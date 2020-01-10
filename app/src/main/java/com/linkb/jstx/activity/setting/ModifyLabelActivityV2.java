package com.linkb.jstx.activity.setting;

import android.support.v7.widget.RecyclerView;

import com.gyf.barlibrary.ImmersionBar;
import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyLabelActivityV2 extends BaseActivity {
    @BindView(R.id.lv_labels)
    RecyclerView lvLabels;


    @Override
    protected void initComponents() {
        ButterKnife.bind(this);
        ImmersionBar.with(this)
                .keyboardEnable(true)
                .statusBarDarkFont(true)
                .init();
        initUI();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_modify_label_v2;
    }

    @OnClick(R.id.back_btn)
    public void onBack() {
        finish();
    }

    private void initUI() {

    }
}
