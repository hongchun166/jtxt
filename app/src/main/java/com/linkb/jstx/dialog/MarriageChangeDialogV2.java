package com.linkb.jstx.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.dialog.base.BaseDialog;

public class MarriageChangeDialogV2 extends BaseDialog implements View.OnClickListener {
    private LinearLayout llNo;
    private LinearLayout llYes;
    private ImageView imNo;
    private ImageView imYes;
    private TextView tvOk;

    public OnMarriageCheckListener mListener;
    private Context mContext;
    private int mType;

    public MarriageChangeDialogV2(Context context) {
        super(context);
        this.mContext = context;
        initUI();

    }

    private void initUI() {
        setContentView(R.layout.dialog_marriage_change);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.BOTTOM;
        llNo = findViewById(R.id.ll_no);
        llYes = findViewById(R.id.ll_yes);
        imNo = findViewById(R.id.image_check_no);
        imYes = findViewById(R.id.image_check_yes);
        tvOk = findViewById(R.id.tv_ok);
        llNo.setOnClickListener(this);
        llYes.setOnClickListener(this);
        tvOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_no:
                updateStatus(0);
                break;
            case R.id.ll_yes:
                updateStatus(1);
                break;
            case R.id.tv_ok:
                if (mListener != null) mListener.marriageStatus(mType);
                break;
        }
    }

    /**
     * 修改所有选中状态
     *
     * @param type 状态 0为未婚1为已婚
     */
    public void updateStatus(int type) {
        this.mType = type;
        imNo.setVisibility(type == 0 ? View.VISIBLE : View.GONE);
        imYes.setVisibility(type == 0 ? View.GONE : View.VISIBLE);
    }

    /**
     * 设置婚姻状态选择的回调监听
     *
     * @param checkListener 对象
     */
    public void setOnMarriageCheckListener(OnMarriageCheckListener checkListener) {
        this.mListener = checkListener;
    }

    public interface OnMarriageCheckListener {
        /**
         * 性别选择的回调监听
         *
         * @param type 0为未婚，1为已婚
         */
        void marriageStatus(int type);
    }

}


