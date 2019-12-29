package com.linkb.jstx.activity.contact;

import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.database.GroupRepository;
import com.linkb.jstx.model.Group;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.util.AppTools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** 群公告编辑功能
* */
public class GroupSummaryEditActivity extends BaseActivity {

    @BindView(R.id.editText)
    EditText editText;

    @BindView(R.id.confirm_btn)
    TextView modifyBtn;

    /** 是否是编辑状态
    * */
    private boolean enableModifyStatus = false;
    private Group mGroup ;

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);

        mGroup = (Group) this.getIntent().getSerializableExtra("group");
        if (mGroup != null) editText.setText(mGroup.summary);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_modify_group_summary;
    }

    @OnClick(R.id.confirm_btn)
    public void modifyGroupSummary(){
        if (enableModifyStatus){
            enableModifyStatus = false;
            modifyBtn.setText(getResources().getString(R.string.common_edit));
            editText.setEnabled(false);

            mGroup.summary = editText.getText().toString().trim();
            GroupRepository.update(mGroup);
            HttpServiceManager.setGroupSummary(mGroup.id, mGroup.summary);
            AppTools.showToastView(this,R.string.tip_save_complete);

        }else {
            enableModifyStatus = true;
            modifyBtn.setText(getResources().getString(R.string.finish));
            editText.setEnabled(true);
        }

    }

    @OnClick(R.id.back_btn)
    public void onBackbtn(){
        finish();
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra(Group.class.getName(),mGroup);
        setResult(RESULT_OK,intent);
        super.finish();
    }
}
