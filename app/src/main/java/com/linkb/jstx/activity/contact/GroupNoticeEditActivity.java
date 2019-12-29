package com.linkb.jstx.activity.contact;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseDataResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/** 群公告编辑
* */
public class GroupNoticeEditActivity extends BaseActivity {

    @BindView(R.id.editText)
    EditText profileEditText;
    @BindView(R.id.group_notice_status_btn)
    TextView profileEditStatusTv;
    @BindView(R.id.group_notice_edit_fly)
    View groupNoticeEditFly;

    private boolean enableModify;

    private Boolean enableGroupCreator;

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);
        profileEditText.setEnabled(enableModify);
        enableGroupCreator  = getIntent().getBooleanExtra("enableGroupCreator", false);
        if (enableGroupCreator){
            groupNoticeEditFly.setVisibility(View.VISIBLE);
        }else {
            groupNoticeEditFly.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_group_notice_edit;
    }

    @OnClick(R.id.group_notice_edit_fly)
    public void changeEditStatus() {
        if (!enableModify){
            enableModify = true;
            profileEditText.setEnabled(true);
            profileEditStatusTv.setText(getResources().getString(R.string.common_edit));
        }else {
            enableModify = false;
            profileEditText.setEnabled(false);
            profileEditStatusTv.setText(getResources().getString(R.string.common_save));
        }
    }

    @OnClick(R.id.back_btn)
    public void onBack(){
        finish();
    }

    private HttpRequestListener<BaseDataResult> listener = new HttpRequestListener<BaseDataResult>() {
        @Override
        public void onHttpRequestSucceed(BaseDataResult result, OriginalCall call) {

        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {

        }
    };
}
