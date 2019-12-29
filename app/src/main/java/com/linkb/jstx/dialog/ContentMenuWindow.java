
package com.linkb.jstx.dialog;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.linkb.jstx.listener.OnMenuClickedListener;
import com.linkb.jstx.util.AppTools;
import com.linkb.R;

public class ContentMenuWindow extends PopupWindow implements View.OnClickListener {
    private ViewGroup rootView;
    private OnMenuClickedListener operationListener;

    public ContentMenuWindow(Context context) {

        super(context);
        rootView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.layout_context_menu_dialog, null);
        setContentView(rootView);

        setWidth(AppTools.dip2px(150));
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(0x00000000));
        setAnimationStyle(R.style.popubWindowScaleAnimation);
        setAllMenuClickListener();
    }

    public void  show(View anchor){
        int[] location = new int[2];
        anchor.getLocationInWindow(location);
        float longClickX = (float) anchor.getTag(R.id.x);
        float longClickY = (float) anchor.getTag(R.id.y);
       /* try {
            Field xField = View.class.getDeclaredField("mLongClickX");
            xField.setAccessible(true);
            longClickX = xField.getFloat(anchor);
            Field yField = View.class.getDeclaredField("mLongClickY");
            yField.setAccessible(true);
            longClickY = yField.getFloat(anchor);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        int x = (int) (location[0] + longClickX);
        int y = (int) (location[1] + longClickY);

        AppTools.measureView(rootView);

        setHeight(rootView.getMeasuredHeight());

        super.showAtLocation(anchor, Gravity.NO_GRAVITY,x,y);
    }

    public void setOnMenuClickedListener(OnMenuClickedListener operationListener) {
        this.operationListener = operationListener;
    }

    public Object getTag() {

        return rootView.getTag();
    }

    public void setTag(Object tag) {

        rootView.setTag(tag);
    }

    private View findViewById(int id){
        return rootView.findViewById(id);
    }

    public void buildChatMenuGroup(boolean hasTop, boolean noRead) {
        hideAll();
        if (!hasTop) {
            findViewById(R.id.menu_chat_top).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.menu_cancel_top).setVisibility(View.VISIBLE);
        }
        if (!noRead) {
            findViewById(R.id.menu_mark_noread).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.menu_mark_read).setVisibility(View.VISIBLE);
        }
        findViewById(R.id.menu_delete_chat).setVisibility(View.VISIBLE);
    }

    public void buildChatRecordMenuGroup(boolean canCopy,boolean canForward, boolean canRevoke, boolean canReadQrCode) {
        hideAll();
        findViewById(R.id.menu_delete).setVisibility(View.VISIBLE);
        if (canForward) {
            findViewById(R.id.menu_forward).setVisibility(View.VISIBLE);
        }
        if (canReadQrCode){
            findViewById(R.id.menu_qr_code_copy).setVisibility(View.VISIBLE);
        }
        if (canCopy) {
            findViewById(R.id.menu_copy).setVisibility(View.VISIBLE);
        }
        if (canRevoke) {
            findViewById(R.id.menu_revoke).setVisibility(View.VISIBLE);
        }
    }

    private void hideAll() {
        for (int i = 0; i < rootView.getChildCount(); i++) {
            rootView.getChildAt(i).setVisibility(View.GONE);
        }
    }

    private void setAllMenuClickListener() {
        for (int i = 0; i < rootView.getChildCount(); i++) {
            rootView.getChildAt(i).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        this.dismiss();
        operationListener.onMenuItemClicked(v.getId());
    }
}
