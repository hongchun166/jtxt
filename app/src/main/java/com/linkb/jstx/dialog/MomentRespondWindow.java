
package com.linkb.jstx.dialog;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.R;

public class MomentRespondWindow extends PopupWindow implements OnClickListener {
    private TextView praise;
    private OnItemClickedListener onItemClickedListener;

    public MomentRespondWindow(Context context) {
        super(context, null);
        View contentView = LayoutInflater.from(context).inflate(R.layout.window_moment_respond, null);
        setContentView(contentView);

        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        contentView.measure(w, h);
        setWidth(contentView.getMeasuredWidth());
        setHeight(contentView.getMeasuredHeight());

        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(0x00000000));
        praise = getContentView().findViewById(R.id.praise_text);
        getContentView().findViewById(R.id.bar_praise).setOnClickListener(this);
        getContentView().findViewById(R.id.bar_comment).setOnClickListener(this);
        setAnimationStyle(R.style.popubWindowAlphaAnimation);
    }

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    @Override
    public void onClick(View v) {
        dismiss();
        onItemClickedListener.onItemClicked(v.getTag(), v);
    }


    public void showAtLocation(View parent, boolean hasPraise) {
        praise.setText(hasPraise ? R.string.common_cancel : R.string.common_praise);
        int[] loc = new int[2];
        parent.getLocationOnScreen(loc);
        int x = loc[0];
        int y = (int) (loc[1] - Resources.getSystem().getDisplayMetrics().density * 8);
        super.showAtLocation(parent, Gravity.NO_GRAVITY, x, y);
    }

    public void enablePariseMenu() {
        getContentView().findViewById(R.id.bar_praise).setEnabled(true);
    }

    public void disenablePariseMenu() {
        getContentView().findViewById(R.id.bar_praise).setEnabled(false);
    }
}
