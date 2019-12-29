
package com.linkb.jstx.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.linkb.R;

public class ListFooterView extends RelativeLayout {
    private View footerProgressBar;
    private View footerHintView;

    public ListFooterView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        footerProgressBar = findViewById(R.id.footer_progressBar);
        footerHintView = findViewById(R.id.footer_hint);
    }

    public void hideProgressBar() {
        footerProgressBar.setVisibility(INVISIBLE);
    }

    public void showProgressBar() {
        setVisibility(VISIBLE);
        footerProgressBar.setVisibility(VISIBLE);
    }

    public void hideHintView() {
        footerHintView.setVisibility(INVISIBLE);
    }

    public void showHintView() {
        setVisibility(VISIBLE);
        footerHintView.setVisibility(VISIBLE);
    }
}
