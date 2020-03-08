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


    TextView viewRedState;
    TextView viewRedMoney;
    TextView viewRedType;
    Button viewConfirm;
    ImageButton viewCancel;

    Dialog dialog;


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

        return this;
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
