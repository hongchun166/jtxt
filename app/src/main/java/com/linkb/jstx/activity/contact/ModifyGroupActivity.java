
package com.linkb.jstx.activity.contact;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.linkb.jstx.database.GlideImageRepository;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.util.AppTools;
import com.linkb.jstx.activity.util.PhotoAlbumActivity;
import com.linkb.jstx.bean.FileResource;
import com.linkb.jstx.database.GroupRepository;
import com.linkb.jstx.listener.OSSFileUploadListener;
import com.linkb.jstx.model.Group;
import com.linkb.jstx.network.CloudFileUploader;
import com.linkb.R;
import com.linkb.jstx.util.FileURLBuilder;
import com.linkb.jstx.util.StringUtils;

import java.util.Objects;

public class ModifyGroupActivity extends CreateGroupActivity implements OSSFileUploadListener {

    private Group group;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        group = (Group) getIntent().getSerializableExtra(Group.class.getName());

        name.setText(group.name);
        name.setVisibility(isNameMode()?View.VISIBLE:View.GONE);

        summary.setText(group.summary);
        summary.setVisibility(isSummaryMode()?View.VISIBLE:View.GONE);

        ((TextView)this.findViewById(R.id.rightButton)).setText(R.string.common_save);


        icon.load(FileURLBuilder.getGroupIconUrl(group.id), R.drawable.logo_group_normal,999);
        icon.setEnabled(isLogoMode());
        findViewById(R.id.edit_mark).setVisibility(icon.isEnabled()?View.VISIBLE:View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rightButton ){
            doUpdate();
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

    private boolean isNameMode(){
        return Objects.equals("name",getIntent().getAction());
    }

    private boolean isSummaryMode(){
        return Objects.equals("summary",getIntent().getAction());
    }

    private boolean isLogoMode(){
        return Objects.equals("logo",getIntent().getAction());
    }

    private void doUpdate() {

        if (StringUtils.isEmpty(name.getText().toString().trim())) {
            AppTools.showToastView(this,R.string.tips_input_group_name);
            return;
        }

        if (isNameMode()){
            performModifyNameRequest();
        }

        if (isSummaryMode()){
            performModifySummaryRequest();
        }

        if (isLogoMode()){
            performUploadLogo();
        }
    }

    @Override
    protected void performUploadLogo() {
        if (logoFile != null) {
            progressDialog.setMessage(getString(R.string.tip_file_uploading, 0));
            CloudFileUploader.asyncUpload(FileURLBuilder.BUCKET_GROUP_ICON, String.valueOf(group.id), logoFile,this);
        }else {
            finish();
        }
    }
    private void performModifyNameRequest() {
        group.name = name.getText().toString().trim();
        GroupRepository.update(group);
        HttpServiceManager.setGroupName(group.id,group.name);
        AppTools.showToastView(this,R.string.tip_save_complete);
        finishResultOk();
    }
    private void performModifySummaryRequest() {
        group.summary = summary.getText().toString().trim();
        GroupRepository.update(group);
        HttpServiceManager.setGroupSummary(group.id,group.summary);
        AppTools.showToastView(this,R.string.tip_save_complete);
        finishResultOk();
    }

    @Override
    public void onUploadCompleted(FileResource resource) {
        progressDialog.dismiss();
        HttpServiceManager.setGroupLogo(group.id);
        GlideImageRepository.save(FileURLBuilder.getGroupIconUrl(group.id), String.valueOf(System.currentTimeMillis()));
        AppTools.showToastView(this,R.string.tip_save_complete);
        finishResultOk();
    }

    private void finishResultOk(){
        Intent intent = new Intent();
        intent.putExtra(Group.class.getName(),group);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void onUploadProgress(String key, float progress) {
        progressDialog.setMessage(getString(R.string.tip_file_uploading, (int) progress));
        if (!progressDialog.isShowing()){
            progressDialog.show();
        }
    }

    @Override
    public void onUploadFailured(FileResource resource, Exception e) {
        progressDialog.dismiss();
        AppTools.showToastView(this,R.string.tip_logo_update_error);
    }


}
