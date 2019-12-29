package com.linkb.jstx.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.linkb.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/** 账单择底部弹出框
* */
public class BillChangeDialogFragment extends BaseDialogFragment {

    public OnBottomDialogSelectListener getListener() {
        return listener;
    }

    public void setListener(OnBottomDialogSelectListener listener) {
        this.listener = listener;
    }

    private OnBottomDialogSelectListener listener;

    public BillChangeDialogFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
         //使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.fragment_switch_bill_type);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消

        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);

        ButterKnife.bind(this, dialog); // Dialog即View


        return dialog;
    }

    @OnClick(R.id.lly_withdraw_coin_record)
    public void selectWithdrawCoinRecord(){
        if (listener != null){
            listener.selectFirstItem();
        }
    }

    @OnClick(R.id.lly_red_packet_record)
    public void selectRePacketRecord(){
        if (listener != null){
            listener.selectSecondItem();
        }
    }


    public interface OnBottomDialogSelectListener {
        void selectFirstItem();
        void selectSecondItem();
    }
}
