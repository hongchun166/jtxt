package com.linkb.jstx.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.linkb.R;

public class EmptyViewV2  extends RelativeLayout {


    public EmptyViewV2(Context context) {
        this(context, null);
    }

    public EmptyViewV2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public EmptyViewV2(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }
    public EmptyViewV2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        LayoutInflater.from(context).inflate(R.layout.view_contact_user_null_bg,this);
    }
}
