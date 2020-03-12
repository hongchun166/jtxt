package com.linkb.jstx.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.linkb.R;

public class EditorRedBagDig  {

    public static class RedBagBParam{
        public static final int STATE_SUC=0;
        public static final int STATE_FAIL=1;
        public int state=STATE_SUC;  // get redbag suc or faill
        public  double number=0D;// 2.5
        public String type="KKC";// KKC
    }

    TextView viewRedState;
    TextView viewRedMoney;
    TextView viewRedType;
    Button viewConfirm;
    ImageButton viewCancel;

    Dialog dialog;

    RedBagBParam redBagBParam;

    public static EditorRedBagDig build(){
        return  new EditorRedBagDig();
    }

    public EditorRedBagDig buildDialog(Context context){
        dialog=new Dialog(context,R.style.DialogTheme);
        View contentView= LayoutInflater.from(context).inflate(R.layout.dialog_editor_redbag,null);
        dialog.setContentView(contentView);
        dialog.setCanceledOnTouchOutside(false);


        viewRedState=contentView.findViewById(R.id.viewRedState);
        viewRedMoney=contentView.findViewById(R.id.viewRedMoney);
        viewRedType=contentView.findViewById(R.id.viewRedType);
        viewConfirm=contentView.findViewById(R.id.viewConfirm);
        viewCancel=contentView.findViewById(R.id.viewCancel);

        viewConfirm.setOnClickListener(v -> hideDialog());
        viewCancel.setOnClickListener(v -> hideDialog());
        updateView();
        return this;
    }

    public EditorRedBagDig setRedBagBParam(RedBagBParam redBagBParam){
            this.redBagBParam=redBagBParam;
            updateView();
            return this;
    }

    private void updateView(){
        if(viewRedMoney==null || redBagBParam==null) return;
        viewRedMoney.setText(String.valueOf(redBagBParam.number));
        viewRedState.setText((redBagBParam.state==RedBagBParam.STATE_SUC?R.string.hint_get_suc:R.string.received_red_packet_error));
        viewRedType.setText(redBagBParam.type);
    }

    public void showDialog(){
        if(dialog!=null && !dialog.isShowing()){
            dialog.show();
        }
    }
    public void hideDialog(){
        if(dialog!=null && dialog.isShowing()){
            dialog.dismiss();
            dialog=null;
        }
    }

}
