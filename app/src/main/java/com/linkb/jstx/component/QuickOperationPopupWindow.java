package com.linkb.jstx.component;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.linkb.R;

public class QuickOperationPopupWindow extends PopupWindow {

     TextView createGroupChatBtn;
     TextView addFriendsBtn;
    TextView mineWalletBtn;
    View create_group_cly, add_friends_cly, mine_wallet_cly, qr_scan_cly;
    ImageView canImageView;

    private QuickOperationClickListener mListener;

    public QuickOperationPopupWindow(Context context, QuickOperationClickListener listener){
        super(context);
        mListener = listener;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.popup_window_quick_operation, null);
        create_group_cly = view.findViewById(R.id.create_group_cly);
        add_friends_cly = view.findViewById(R.id.add_friends_cly);
        mine_wallet_cly = view.findViewById(R.id.mine_wallet_cly);
        qr_scan_cly = view.findViewById(R.id.qr_scan_cly);

        canImageView = view.findViewById(R.id.qr_scan_imageView);
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
        create_group_cly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.createGroup();
                    dismiss();
                }
            }
        });
        add_friends_cly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.addFriends();
                    dismiss();
                }
            }
        });
        mine_wallet_cly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.mineWallet();
                    dismiss();
                }
            }
        });
        qr_scan_cly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.scanQrCode();
                    dismiss();
                }
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public interface QuickOperationClickListener {
        void createGroup();
        void addFriends();
        void mineWallet();
        void scanQrCode();
    }
}
