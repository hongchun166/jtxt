
package com.linkb.jstx.activity.contact;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.dialog.CustomDialog;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.activity.util.PhotoAlbumActivity;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.app.URLConstant;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.database.GroupMemberRepository;
import com.linkb.jstx.database.GroupRepository;
import com.linkb.jstx.dialog.CustomProgressDialog;
import com.linkb.jstx.listener.OnDialogButtonClickListener;
import com.linkb.jstx.model.Group;
import com.linkb.jstx.model.GroupMember;
import com.linkb.jstx.network.CloudFileUploader;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.GroupResult;
import com.linkb.R;
import com.linkb.jstx.util.AppTools;
import com.linkb.jstx.util.FileURLBuilder;
import com.linkb.jstx.util.StringUtils;

import java.io.File;

public class CreateGroupActivity extends AppCompatActivity implements OnDialogButtonClickListener, HttpRequestListener<GroupResult>,View.OnClickListener {
    protected CustomProgressDialog progressDialog;
    protected EditText name;
    protected EditText summary;
    private CustomDialog customDialog;
    protected WebImageView icon;
    protected File logoFile;
    private Group newGroup = new Group();
    protected User self;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        name = this.findViewById(R.id.name);
        summary = this.findViewById(R.id.summary);
        icon = this.findViewById(R.id.logo);
        this.findViewById(R.id.logo).setOnClickListener(this);
        this.findViewById(R.id.leftButton).setOnClickListener(this);
        this.findViewById(R.id.rightButton).setOnClickListener(this);

        customDialog = new CustomDialog(this);
        customDialog.setOnDialogButtonClickListener(this);
        customDialog.setIcon(R.drawable.icon_dialog_success);
        customDialog.setMessage((R.string.tip_group_create_succesed));
        customDialog.setRightButtonsText(R.string.common_invite);

        progressDialog = new CustomProgressDialog(this, R.style.CustomDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.tip_loading, getString(R.string.common_create)));
        self = Global.getCurrentUser();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rightButton ){
            doCreate();
        }
        if (v.getId() == R.id.leftButton ){
            finish();
        }
        if (v.getId() == R.id.logo ){
            Intent intentFromGallery = new Intent();
            intentFromGallery.setClass(this, PhotoAlbumActivity.class);
            startActivityForResult(intentFromGallery, 9);
        }
    }

    private void doCreate() {
        newGroup.summary = summary.getText().toString();
        if (StringUtils.isEmpty(name.getText())) {
            AppTools.showToastView(this,R.string.tips_input_group_name);
            return;
        }
        newGroup.name = name.getText().toString();
        newGroup.founder = self.account;

        performCreateRequest();
    }

    private void performCreateRequest() {
        progressDialog.show();
        HttpServiceManager.create(newGroup,this);
    }

    protected void performUploadLogo() {
        if (logoFile != null) {
            CloudFileUploader.asyncUpload(FileURLBuilder.BUCKET_GROUP_ICON, newGroup.id, logoFile);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 9) {

            AppTools.startIconPhotoCrop(this, data.getData());
        }
        if (resultCode == Activity.RESULT_OK && requestCode == Constant.RESULT_ZOOM && data != null) {
            logoFile = new File(Global.getCropPhotoFilePath());
            icon.load(logoFile,0,999);
        }
    }


    @Override
    public void onHttpRequestSucceed(GroupResult result, OriginalCall call) {
        progressDialog.dismiss();
        newGroup = result.data;

        GroupMember member = new GroupMember();
        member.account = self.account;
        member.groupId = newGroup.id;
        member.id = System.currentTimeMillis();
        member.host = GroupMember.RULE_FOUNDER;
        GroupRepository.add(newGroup);
        GroupMemberRepository.saveMember(member);

        Intent addIntent = new Intent(Constant.Action.ACTION_GROUP_ADD);
        addIntent.putExtra(Group.NAME, newGroup);
        LvxinApplication.sendLocalBroadcast(addIntent);

        performUploadLogo();

        customDialog.show();
    }



    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        if (call.equalsPost(URLConstant.GROUP_OPERATION_URL)){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onLeftButtonClicked() {
        customDialog.dismiss();
        finish();
    }

    @Override
    public void onRightButtonClicked() {
        customDialog.dismiss();
        Intent intent = new Intent();
        intent.putExtra("group", newGroup);
        intent.setClass(CreateGroupActivity.this, InviteGroupMemberActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && AppTools.isOutOfBounds(this, event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }
}
