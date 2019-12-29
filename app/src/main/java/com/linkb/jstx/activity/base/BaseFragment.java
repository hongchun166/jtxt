
package com.linkb.jstx.activity.base;


import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.dialog.CustomProgressDialog;
import com.linkb.jstx.util.AppTools;

public class BaseFragment extends Fragment {

    /** 导航栏
     * */
    protected TextView toolBarTitle;
    protected View imageBtn;
    private CustomProgressDialog progressDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void showProgressDialog(String content, boolean cancancelable) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.setMessage(content);
            progressDialog.setCancelable(cancancelable);
            return;
        }
        progressDialog = new CustomProgressDialog(getContext(), R.style.CustomDialogStyle);
        progressDialog.setMessage(content);
        progressDialog.setCancelable(cancancelable);
        progressDialog.show();
    }

    public void showProgressDialog(String content) {
        showProgressDialog(content, true);
    }

    public void hideProgressDialog() {

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }

    public void showToastView(String text) {
        AppTools.showToastView(getContext(),text);
    }

    public void showToastView(@StringRes int id) {
        showToastView(getString(id));
    }

}
