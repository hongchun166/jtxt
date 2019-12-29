package com.linkb.jstx.component;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.linkb.R;

public class CopyPhotoTipsPopupWindow extends PopupWindow {


    private BitmapClickListener mListener;
    private Uri mUri;

    public CopyPhotoTipsPopupWindow(Context context, Uri uri, BitmapClickListener listener){
        super(context);
        mListener = listener;
        mUri = uri;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popup_window_copy_bitmap_tips, null);
        WebImageView copyBitmap = view.findViewById(R.id.copy_bitmap);
        copyBitmap.load(uri);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) mListener.sendBitmap(mUri);
            }
        });
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xf0f1f2);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        this.setOutsideTouchable(true);

    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public interface BitmapClickListener {
        void sendBitmap(Uri uri);
    }
}
