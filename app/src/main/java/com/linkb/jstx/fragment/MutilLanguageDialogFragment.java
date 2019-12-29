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

/** 多语言选择底部弹出框
* */
public class MutilLanguageDialogFragment extends BaseDialogFragment {

    public OnSelectLanguageListener getListener() {
        return listener;
    }

    public void setListener(OnSelectLanguageListener listener) {
        this.listener = listener;
    }

    private OnSelectLanguageListener listener;

    public MutilLanguageDialogFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
         //使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.fragment_switch_language);
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

    @OnClick(R.id.lly_cn)
    public void selectCn(){
        if (listener != null){
            listener.selectCn();
        }
    }

    @OnClick(R.id.lly_en)
    public void selectEn(){
        if (listener != null){
            listener.selectEn();
        }
    }


    public interface OnSelectLanguageListener {
        void selectCn();
        void selectEn();
    }
}
