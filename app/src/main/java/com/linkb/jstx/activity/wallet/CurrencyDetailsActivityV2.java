package com.linkb.jstx.activity.wallet;

import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.gyf.barlibrary.ImmersionBar;
import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CurrencyDetailsActivityV2 extends BaseActivity {
    @BindView(R.id.ll_aa_bb)
    LinearLayout llAABB;
    @BindView(R.id.ll_top)
    LinearLayout llTop;

    private int aa = 0;
    private int ma = 0;

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);
        ImmersionBar.with(this)
                .keyboardEnable(true)
                .statusBarDarkFont(true)
                .init();
        ViewTreeObserver vto = llTop.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                aa = llTop.getHeight();
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_currency_details_v2;
    }

    @OnClick(R.id.ll_aa_bb)
    public void aabb() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) llAABB.getLayoutParams();
        ma = ma == 0 ? -aa : 0;
        params.topMargin = ma;
        llAABB.setLayoutParams(params);
    }
}
