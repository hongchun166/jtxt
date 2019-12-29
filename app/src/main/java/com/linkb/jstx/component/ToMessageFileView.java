
package com.linkb.jstx.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.linkb.R;

public class ToMessageFileView extends BaseToMessageView {
    private CircleProgressView circleProgressView;

    public ToMessageFileView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        circleProgressView = findViewById(R.id.circleProgressView);
    }

    @Override
    public View getContentView() {
        return fileView;
    }

    @Override
    public void displayMessage() {
        circleProgressView.setVisibility(GONE);
        fileView.initView(message);
        fileView.setOnClickListener(fileView);
        sendProgressbar.setVisibility(GONE);
        fileView.setSendProgressView(circleProgressView);
    }

}
