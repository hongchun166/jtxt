
package com.linkb.jstx.component;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkb.R;


public class GlobalEmptyView extends FrameLayout {
    private TextView tips;
    private ImageView icon;

    public GlobalEmptyView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.layout_empty_view, this);
        tips = findViewById(R.id.tips);
        icon = findViewById(R.id.icon);

    }


    public void setTips(int sid) {
        tips.setText(sid);
    }

    public void setIcon(int resId) {
        icon.setImageResource(resId);
    }

    public void toggle(RecyclerView.Adapter adapter) {
        setVisibility(adapter.getItemCount() > 0 ? GONE : VISIBLE);
    }
}
