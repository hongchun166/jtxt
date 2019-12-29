
package com.linkb.jstx.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class FromMessageFileView extends BaseFromMessageView {

    public FromMessageFileView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public View getContentView() {
        return fileView;
    }


    @Override
    public void displayMessage() {

        fileView.initView(message);
        fileView.setOnClickListener(fileView);
    }

}
