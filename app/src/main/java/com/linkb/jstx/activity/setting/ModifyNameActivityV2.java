package com.linkb.jstx.activity.setting;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.ModifyPersonInfoResult;

public class ModifyNameActivityV2 extends BaseActivity implements HttpRequestListener<ModifyPersonInfoResult> {
    @Override
    protected void initComponents() {

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_modify_common_v2;
    }

    @Override
    public void onHttpRequestSucceed(ModifyPersonInfoResult result, OriginalCall call) {

    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {

    }
}
