package com.linkb.jstx.activity.contact;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.database.GroupRepository;
import com.linkb.jstx.model.Group;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManagerV2;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.v2.CheckInGroupResult;
import com.linkb.jstx.network.result.v2.FindGroupsResult;
import com.linkb.jstx.util.FileURLBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class GroupDetailDescActivity extends BaseActivity {

    @BindView(R.id.viewGroupHead)
    WebImageView viewGroupHead;
    @BindView(R.id.viewGroupName)
    TextView viewGroupName;
    @BindView(R.id.viewGroupMemberNum)
    TextView viewGroupMemberNum;
    @BindView(R.id.viewCreateTime)
    TextView viewCreateTime;
    @BindView(R.id.viewGroupCode)
    TextView viewGroupCode;
    @BindView(R.id.viewGroupDesc)
    TextView viewGroupDesc;
    @BindView(R.id.nextButton)
    Button nextButton;

    Unbinder unbinder;
    Group group;

    public static void navToAct(Context context, Group group){
        Intent intent=new Intent(context,GroupDetailDescActivity.class);
        intent.putExtra("Group",group);
        context.startActivity(intent);
    }
    @Override
    protected int getContentLayout() {
        return R.layout.activity_group_detail_desc;
    }
    @Override
    protected void initComponents() {
         unbinder=ButterKnife.bind(this);
        group= (Group) getIntent().getSerializableExtra("Group");
        httpCheckInGroup(group);
        updateViewByGroup(group);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(unbinder!=null){
            unbinder.unbind();
            unbinder=null;
        }
    }

    @OnClick(R.id.back)
    public void back() {
        finish();
    }

    public void updateViewByGroup(Group group){
        viewGroupHead.load(FileURLBuilder.getUserIconUrl(String.valueOf(group.id)), R.mipmap.lianxiren, 999);
        viewGroupName.setText(String.valueOf(group.name));
        viewGroupCode.setText(String.valueOf(group.id));
        viewGroupDesc.setText(String.valueOf(group.summary));
        viewGroupMemberNum.setText(String.format(getString(R.string.group_member_size),group.memberSize));

        updateViewByInGroup(GroupRepository.queryById(group.getId())!=null);
    }
    public void updateViewByInGroup(boolean isInGroup){
        if(isInGroup){
            nextButton.setText("发消息");
        }else {
            nextButton.setText("申请入群");
        }
    }
    private void httpCheckInGroup(Group group) {
        showProgressDialog("");
        User user = Global.getCurrentUser();
        final Context context = this;
        HttpServiceManagerV2.checkInGroup(user.account, group.getId(), new HttpRequestListener<CheckInGroupResult>() {
            @Override
            public void onHttpRequestSucceed(CheckInGroupResult result, OriginalCall call) {
                hideProgressDialog();
                if (result.isSuccess()) {
                    if (result.isData()) {
                        updateViewByInGroup(true);
                    } else {
                        updateViewByInGroup(false);
                    }
                }
            }

            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {
                hideProgressDialog();
            }
        });
    }
}
