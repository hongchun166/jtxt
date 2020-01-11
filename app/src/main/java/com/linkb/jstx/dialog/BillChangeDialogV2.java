package com.linkb.jstx.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.dialog.base.BaseDialog;

public class BillChangeDialogV2 extends BaseDialog implements View.OnClickListener {
    private Context mContext;
    private TextView tvAll;
    private TextView tvRed;
    private TextView tvWithdrawal;
    private TextView tvTopUp;
    private TextView tvCancel;
    private OnBillChangeListener listener;
    private int type;

    public BillChangeDialogV2(Context context) {
        super(context);
        initUI();
    }

    private void initUI() {
        setContentView(R.layout.dialog_bill_chage_v2);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.BOTTOM;
        tvAll = findViewById(R.id.tv_all);
        tvRed = findViewById(R.id.tv_red);
        tvWithdrawal = findViewById(R.id.tv_withdrawal);
        tvTopUp = findViewById(R.id.tv_top_up);
        tvCancel = findViewById(R.id.tv_cancel);
        tvAll.setOnClickListener(this);
        tvRed.setOnClickListener(this);
        tvWithdrawal.setOnClickListener(this);
        tvTopUp.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        updateType(0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_all:
                if (listener != null) listener.onAll();
                dismiss();
                break;
            case R.id.tv_red:
                if (listener != null) listener.onRed();
                dismiss();
                break;
            case R.id.tv_withdrawal:
                if (listener != null) listener.onWithdrawal();
                dismiss();
                break;
            case R.id.tv_top_up:
                if (listener != null) listener.onTopUp();
                dismiss();
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
        }
    }

    public void setOnBillChangeListener(OnBillChangeListener changeListener) {
        this.listener = changeListener;
    }

    /**
     * 设置类型
     *
     * @param type 0为all，1为红包  2为提现  3为充值
     */
    public void setType(int type) {
        this.type = type;
        updateType(this.type);
    }

    private void updateType(int type) {
        hidden();
        switch (type) {
            case 0:
                tvAll.setSelected(true);
                break;
            case 1:
                tvRed.setSelected(true);
                break;
            case 2:
                tvWithdrawal.setSelected(true);
                break;
            case 3:
                tvTopUp.setSelected(true);
                break;
            default:
                tvAll.setSelected(true);
                break;
        }

    }

    private void hidden() {
        tvAll.setSelected(false);
        tvRed.setSelected(false);
        tvWithdrawal.setSelected(false);
        tvTopUp.setSelected(false);
    }

    /**
     * 账单类型
     */
    public interface OnBillChangeListener {
        void onAll();

        void onRed();

        void onWithdrawal();

        void onTopUp();
    }
}
