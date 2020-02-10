package com.linkb.jstx.dialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.linkb.R;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.dialog.base.BaseDialog;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManagerV2;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.v2.UpdateMessageDestroyTimeResult;

import java.util.HashMap;

public class ReadDelteSetTimeDialog extends BaseDialog implements View.OnClickListener {



    RadioGroup viewRadioGroup;
    RadioButton viewRBTime10;
    RadioButton viewRBTime20;
    RadioButton viewRBTime60;
    Button viewBtnConfirm;

    HashMap<String,String> map=new HashMap<>();

    String friendAccount;

    int curTime=10;

    public ReadDelteSetTimeDialog(Context context) {
        super(context);
        initView(context);
    }
    private void initView(Context context){
       View contentView= LayoutInflater.from(context).inflate(R.layout.dialog_read_delete_settime,null);
       setContentView(contentView);
//        setCanceledOnTouchOutside(false);
        viewRadioGroup=contentView.findViewById(R.id.viewRadioGroup);
        viewRBTime10=contentView.findViewById(R.id.viewRBTime10);
        viewRBTime20=contentView.findViewById(R.id.viewRBTime20);
        viewRBTime60=contentView.findViewById(R.id.viewRBTime60);
        viewBtnConfirm=contentView.findViewById(R.id.viewBtnConfirm);
        map.put(String.valueOf(R.id.viewRBTime10),String.valueOf(10));
        map.put(String.valueOf(R.id.viewRBTime20),String.valueOf(20));
        map.put(String.valueOf(R.id.viewRBTime60),String.valueOf(60));
        map.put(String.valueOf(10),String.valueOf(R.id.viewRBTime10));
        map.put(String.valueOf(20),String.valueOf(R.id.viewRBTime20));
        map.put(String.valueOf(60),String.valueOf(R.id.viewRBTime60));
        curTime=Global.getUserToFriendMsgValidTime(friendAccount);
        viewRadioGroup.check(Integer.valueOf(map.get(String.valueOf(curTime))));
        viewBtnConfirm.setOnClickListener(this);
    }

    public void setFriendAccount(String friendAccount) {
        this.friendAccount = friendAccount;
    }
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.viewBtnConfirm){
            int checkId=viewRadioGroup.getCheckedRadioButtonId();
            int time=Integer.valueOf(map.get(String.valueOf(checkId)));
            httpSetTime(time);
        }
    }


    private void httpSetTime(int time){
        httpUpdateMessageDestroyTime(time);
    }
    public void httpUpdateMessageDestroyTime(final int time){
        String account=Global.getCurrentUser().account;
        final String frienAccount=friendAccount;
        HttpServiceManagerV2.updateMessageDestroyTime(account,frienAccount,time, new HttpRequestListener<UpdateMessageDestroyTimeResult>() {
            @Override
            public void onHttpRequestSucceed(UpdateMessageDestroyTimeResult result, OriginalCall call) {
                if(result.isSuccess()){
                    Global.saveUserToFriendMsgValidTime(friendAccount,time);
                }
                dismiss();
            }
            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {
                dismiss();
            }
        });
    }
}
