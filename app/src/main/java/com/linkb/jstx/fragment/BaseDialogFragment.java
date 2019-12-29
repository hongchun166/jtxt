package com.linkb.jstx.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;

import com.linkb.R;
import com.linkb.jstx.dialog.CustomProgressDialog;
import com.linkb.jstx.util.AppTools;

public class BaseDialogFragment extends DialogFragment {

    private CustomProgressDialog progressDialog;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
