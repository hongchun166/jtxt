package com.linkb.jstx.component;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.linkb.R;

public class NearlyPeopleFilterPopupWindow extends PopupWindow {
    private NearlyPeopleFilterListener mListener;
    private View checkOnlyManCly, checkOnlyWomanCly, checkAllCly, checkOnlyCalledCly;

    public NearlyPeopleFilterPopupWindow(Context context, final NearlyPeopleFilterListener mListener) {
        super(context);
        this.mListener = mListener;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popup_window_nearly_people_filter ,null);
        checkOnlyManCly = view.findViewById(R.id.only_check_man_cly);
        checkOnlyWomanCly = view.findViewById(R.id.only_check_female_cly);
        checkAllCly = view.findViewById(R.id.check_all_cly);
        checkOnlyCalledCly = view.findViewById(R.id.check_called_people_cly);
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xFFFFFF);

        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        this.setOutsideTouchable(true);

        checkOnlyManCly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {mListener.selected(1);}
                NearlyPeopleFilterPopupWindow.this.dismiss();
            }
        });
        checkOnlyWomanCly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {mListener.selected(0);}
                NearlyPeopleFilterPopupWindow.this.dismiss();
            }
        });
        checkAllCly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {mListener.selected(2);}
                NearlyPeopleFilterPopupWindow.this.dismiss();
            }
        });
        checkOnlyCalledCly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {mListener.selected(3);}
                NearlyPeopleFilterPopupWindow.this.dismiss();
            }
        });
    }

   public interface NearlyPeopleFilterListener  {
        /** index 为男是1，女是0,2是全部，3是打过招呼的人
        * */
        void selected(int index);
    }
}
