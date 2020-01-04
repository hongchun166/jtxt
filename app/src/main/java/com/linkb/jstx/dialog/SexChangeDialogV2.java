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


public class SexChangeDialogV2 extends BaseDialog implements View.OnClickListener {
    private LinearLayout llMan;
    private LinearLayout llFemale;
    private ImageView imMan;
    private ImageView imFemale;
    private TextView tvOk;

    public OnSexCheckListener mListener;
    private Context mContext;
    private int mType;

    public SexChangeDialogV2(Context context) {
        super(context);
        this.mContext = context;
        initUI();

    }

    private void initUI() {
        setContentView(R.layout.dialog_sex_change);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.BOTTOM;
        llMan = findViewById(R.id.ll_man);
        llFemale = findViewById(R.id.ll_female);
        imMan = findViewById(R.id.image_check_man);
        imFemale = findViewById(R.id.image_check_female);
        tvOk = findViewById(R.id.tv_ok);
        llMan.setOnClickListener(this);
        llFemale.setOnClickListener(this);
        tvOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_man:
                updateStatus(0);
                break;
            case R.id.ll_female:
                updateStatus(1);
                break;
            case R.id.tv_ok:
                if (mListener != null) mListener.checkSex(mType);
                break;
        }
    }

    /**
     * 修改所有选中状态
     *
     * @param type 状态 0为男1为女
     */
    public void updateStatus(int type) {
        this.mType = type;
        imMan.setVisibility(type == 0 ? View.VISIBLE : View.GONE);
        imFemale.setVisibility(type == 1 ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置性别选择的回调监听
     *
     * @param checkListener 对象
     */
    public void setOnSexCheckListener(OnSexCheckListener checkListener) {
        this.mListener = checkListener;
    }

    public interface OnSexCheckListener {
        /**
         * 性别选择的回调监听
         *
         * @param type 0为男，1为女
         */
        void checkSex(int type);
    }

}

