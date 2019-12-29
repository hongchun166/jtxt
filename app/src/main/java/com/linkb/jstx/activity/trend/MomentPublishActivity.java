
package com.linkb.jstx.activity.trend;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.adapter.PhotoGridViewAdapter;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.app.URLConstant;
import com.linkb.jstx.bean.FileResource;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.dialog.CustomDialog;
import com.linkb.jstx.listener.OSSFileUploadListener;
import com.linkb.jstx.listener.OnDialogButtonClickListener;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.listener.SimpleFileUploadListener;
import com.linkb.jstx.network.CloudFileUploader;
import com.linkb.jstx.network.http.HttpRequestBody;
import com.linkb.jstx.network.http.HttpRequestLauncher;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.model.SNSChatImage;
import com.linkb.jstx.network.model.SNSVideo;
import com.linkb.jstx.util.AppTools;
import com.linkb.jstx.util.BackgroundThreadHandler;
import com.linkb.jstx.util.FileURLBuilder;
import com.linkb.jstx.util.StringUtils;
import com.linkb.jstx.activity.util.PhotoAlbumActivity;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.database.MomentRepository;
import com.linkb.jstx.model.Moment;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.model.MapAddress;
import com.linkb.jstx.network.model.MomentExtra;
import com.linkb.jstx.network.model.MomentLink;
import com.linkb.jstx.network.result.CommonResult;
import com.linkb.R;
import com.google.gson.Gson;
import com.linkb.video.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MomentPublishActivity extends BaseActivity implements OnDialogButtonClickListener, OnItemClickedListener, HttpRequestListener<CommonResult> {
    private static final String TAG = MomentPublishActivity.class.getSimpleName();

    public static final int REQUEST_CODE = 7452;
    private PhotoGridViewAdapter adapter;
    private EditText content;
    private User self;
    private CustomDialog customDialog;
    private Moment moment = new Moment();
    private MomentLink linkMessage;
    private SNSVideo snsVideo;
    private RecyclerView imageGridView;
    private MapAddress mapAddress;
    private ImageView locIcon;
    private TextView address;
    /**  多张图片上传失败
    * */
    private List<SNSChatImage> failureImageList = new ArrayList<>();

    @Override
    public void initComponents() {


        self = Global.getCurrentUser();
        imageGridView = findViewById(R.id.imageGridView);
        imageGridView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5, GridLayoutManager.VERTICAL, false);
        imageGridView.setLayoutManager(gridLayoutManager);
        imageGridView.setItemAnimator(new DefaultItemAnimator());
        imageGridView.setEnabled(false);
        content = findViewById(R.id.content);
        adapter = new PhotoGridViewAdapter();
        adapter.setOnItemClickedListener(this);
        findViewById(R.id.locationPanel).setOnClickListener(this);
        locIcon = findViewById(R.id.locIcon);
        address = findViewById(R.id.address);

        buildPhotoView();
        buildLinkView();
        buildVideoView();
    }

    private void buildVideoView() {
        snsVideo = (SNSVideo) getIntent().getSerializableExtra(SNSVideo.class.getName());
        if (snsVideo != null) {
            WebImageView thumbnailView = findViewById(R.id.thumbnailView);
            imageGridView.setVisibility(View.GONE);
            findViewById(R.id.videoPanel).setVisibility(View.VISIBLE);
            findViewById(R.id.removePicTips).setVisibility(View.GONE);
            findViewById(R.id.videoPanel).setOnClickListener(this);
            File thumbnailFile = new File(LvxinApplication.CACHE_DIR_VIDEO, snsVideo.image);
            thumbnailView.load(thumbnailFile);
        }
    }

    private void buildLinkView() {
        linkMessage = (MomentLink) getIntent().getSerializableExtra(MomentLink.NAME);
        if (linkMessage != null) {
            imageGridView.setVisibility(View.GONE);
            findViewById(R.id.linkPanel).setVisibility(View.VISIBLE);
            findViewById(R.id.removePicTips).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.linkTitle)).setText(linkMessage.title);
        }
    }

    private void buildPhotoView() {
        imageGridView.setVisibility(View.VISIBLE);
        imageGridView.setAdapter(adapter);
        registerForContextMenu(imageGridView);
        customDialog = new CustomDialog(this);
        customDialog.setOnDialogButtonClickListener(this);
        customDialog.setIcon(R.drawable.icon_dialog_image);
        customDialog.setMessage((R.string.title_choice_picture));
        customDialog.setButtonsText(getString(R.string.common_camera), getString(R.string.common_album));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.videoPanel) {
            LvxinApplication.getInstance().startVideoActivity(this, false, snsVideo, v);
        }
        if (v.getId() == R.id.locationPanel) {
            startActivityForResult(new Intent(this, MapAddressActivity.class), MapAddressActivity.REQUEST_CODE);
        }
    }

    @Override
    public void onHttpRequestSucceed(CommonResult result, OriginalCall call) {
        hideProgressDialog();
        this.moment.id = result.data ;
        this.moment.timestamp = System.currentTimeMillis();
        MomentRepository.add(moment);
        showToastView(R.string.tip_publish_complete);
        Intent intent = new Intent();
        intent.putExtra(Moment.class.getName(), moment);
        setResult(Activity.RESULT_OK, intent);
        finish();
        adapter.getImageList().clear();
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        hideProgressDialog();
    }

    private void buildMomentObject() {

        moment.type = Moment.FORMAT_TEXT;
        moment.text = content.getText().toString().trim();
        moment.account = self.account;

        if (adapter.getListSize() > 1) {
            moment.content = new Gson().toJson(adapter.getImageList());
            moment.type = Moment.FORMAT_MULTI_IMAGE;
        }

        if (adapter.getListSize() == 1) {
            moment.content = new Gson().toJson(adapter.getImageList().get(0));
            moment.type = Moment.FORMAT_IMAGE;
        }
        if (linkMessage != null) {
            moment.type = Moment.FORMAT_LINK;
            moment.content = new Gson().toJson(linkMessage);
        }
        if (snsVideo != null) {
            moment.type = Moment.FORMAT_VIDEO;
            moment.content = new Gson().toJson(snsVideo);
        }

        MomentExtra extra = new MomentExtra();
        extra.location = mapAddress;
        extra.device = Build.MODEL;

        moment.extra = new Gson().toJson(extra);
    }

    // 添加上下文菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        menu.add(1, Integer.MAX_VALUE, 1, getString(R.string.common_delete));
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    // 响应上下文菜单
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == Integer.MAX_VALUE) {
            adapter.removeSelected();
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == PhotoAlbumActivity.REQUEST_CODE_MULT) {
            adapter.addAll((List<File>) data.getSerializableExtra("files"));
        } else if (resultCode == RESULT_OK && requestCode == Constant.RESULT_CAMERA) {
            File photo = new File(Global.getPhotoGraphFilePath());
            adapter.add(photo);
        } else if (resultCode == RESULT_OK && requestCode == MapAddressActivity.REQUEST_CODE) {
            mapAddress = (MapAddress) data.getSerializableExtra(MapAddress.class.getName());
            locIcon.setSelected(true);
            address.setText(mapAddress.name);
        } else if (resultCode == RESULT_CANCELED && requestCode == MapAddressActivity.REQUEST_CODE) {
            mapAddress = null;
            locIcon.setSelected(false);
            address.setText(R.string.label_select_location);
        }
    }


    @Override
    public void onLeftButtonClicked() {
        customDialog.dismiss();
        AppTools.startCameraActivity(this);
    }

    @Override
    public void onRightButtonClicked() {
        customDialog.dismiss();
        Intent intent = new Intent(this, PhotoAlbumActivity.class);
        intent.setAction(Constant.Action.ACTION_MULTIPLE_PHOTO_SELECTOR);
        intent.putExtra(PhotoAlbumActivity.KEY_MAX_COUNT, Constant.MAX_MOMENT_PHOTO_SIZE - adapter.getListSize());
        startActivityForResult(intent, PhotoAlbumActivity.REQUEST_CODE_MULT);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_moment_publish;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.label_moment_publish;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_send) {
            preparePublishMoment();
        }
        return super.onOptionsItemSelected(item);
    }

    private void preparePublishMoment() {
        if (adapter.isEmpty() && StringUtils.isEmpty(content.getText().toString()) && linkMessage == null && snsVideo == null) {
            showToastView(R.string.tip_required_article);
            return;
        }
        buildMomentObject();

        showProgressDialog(getString(R.string.tip_loading, getString(R.string.label_moment_publish)));

        //先上传图片或者视频，上传成功再发送朋友圈
        if (moment.type.equals(Moment.FORMAT_MULTI_IMAGE)) {
            uploadMutiImage(moment);
        }else if (moment.type.equals(Moment.FORMAT_IMAGE)){
            uploadSingleImage(moment);
        }else if (moment.type.equals(Moment.FORMAT_VIDEO)){
            uploadVideo(moment);
        }else if (moment.type.equals(Moment.FORMAT_TEXT)){
            publishMoment(moment);
        }else if (moment.type.equals(Moment.FORMAT_LINK)){
            publishMoment(moment);
        }
    }

    /** 上传多张图片
    * */
    private void uploadMutiImage(final Moment moment){
        Log.d(TAG, "uploadMutiImage =======" + moment.content );
        final List<SNSChatImage> snsImageList = new Gson().fromJson(moment.content, new TypeToken<List<SNSChatImage>>() {}.getType());
        uploadImage(snsImageList, new SimpleFileUploadListener(){
            @Override
            public void onUploadCompleted(FileResource resource) {
                publishMoment(moment);
            }

            @Override
            public void onUploadFailured(FileResource resource, Exception e) {
                hideProgressDialog();
                BackgroundThreadHandler.postUIThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.s(MomentPublishActivity.this, getString(R.string.upload_image_failure));
                    }
                });
            }
        });
    }

    /** 上传单张图片图片
     * */
    private void uploadSingleImage(final Moment moment){
        Log.d(TAG, "uploadSingleImage =======" + moment.content );
        final SNSChatImage image = new Gson().fromJson(moment.content,SNSChatImage.class);
        uploadSingleImage(image, new SimpleFileUploadListener(){
            @Override
            public void onUploadCompleted(FileResource resource) {
                publishMoment(moment);
            }

            @Override
            public void onUploadFailured(FileResource resource, Exception e) {
                hideProgressDialog();
                BackgroundThreadHandler.postUIThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.s(MomentPublishActivity.this, getString(R.string.upload_video_failure));
                    }
                });
            }
        });
    }

    /** 上传视频
     * */
    private void uploadVideo(final Moment moment){
        Log.d(TAG, "uploadVideo =======" + moment.content );
        final SNSVideo video = new Gson().fromJson(moment.content, SNSVideo.class);
        uploadVideo(video , new SimpleFileUploadListener(){
            @Override
            public void onUploadCompleted(FileResource resource) {
                publishMoment(moment);
            }

            @Override
            public void onUploadFailured(FileResource resource, Exception e) {
                hideProgressDialog();
                BackgroundThreadHandler.postUIThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.s(MomentPublishActivity.this, getString(R.string.upload_video_failure));
                    }
                });
            }
        });
    }

    /** 上传成功再发送朋友圈
    * */
    private void publishMoment(Moment moment){
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.MOMENT_PUBLISH_URL, CommonResult.class);
        requestBody.addParameter("type", moment.type);
        requestBody.addParameter("content", moment.content);
        requestBody.addParameter("extra", moment.extra);
        requestBody.addParameter("text", moment.text);
        HttpRequestLauncher.execute(requestBody,this);
    }

    /** 上传单张图片封装
    * */
    private void uploadSingleImage(final SNSChatImage image, final OSSFileUploadListener listener) {
        CloudFileUploader.asyncUpload(FileURLBuilder.BUCKET_MOMENT,image.thumb, new File(LvxinApplication.CACHE_DIR_IMAGE, image.thumb), new SimpleFileUploadListener(){
            @Override
            public void onUploadCompleted(FileResource resource) {
                Log.d(TAG, "onUploadCompleted =======" + resource.key );
                CloudFileUploader.asyncUpload(FileURLBuilder.BUCKET_MOMENT,image.image, new File(LvxinApplication.CACHE_DIR_IMAGE, image.image), new SimpleFileUploadListener(){
                    @Override
                    public void onUploadCompleted(FileResource resource) {
                        Log.d(TAG, "onUploadCompleted =======" + resource.key );
                        listener.onUploadCompleted(resource);
                    }

                    @Override
                    public void onUploadFailured(FileResource resource, Exception e) {
                        Log.d(TAG, "onUploadFailured =======" + resource.key );
                        listener.onUploadFailured(resource,e);
                    }
                });
            }

            @Override
            public void onUploadFailured(FileResource resource, Exception e) {
                Log.d(TAG, "onUploadFailured =======" + resource.key );
                listener.onUploadFailured(resource,e);
            }
        });
    }

    /** 上传多张图片封装
    * */
    private void uploadImage(final List<SNSChatImage> snsImageList, final OSSFileUploadListener listener){
        for (int i = 0; i < snsImageList.size(); i++) {
            final SNSChatImage snsChatImage = snsImageList.get(i);
            CloudFileUploader.asyncUpload(FileURLBuilder.BUCKET_MOMENT,snsChatImage.thumb, new File(LvxinApplication.CACHE_DIR_IMAGE, snsChatImage.thumb), new SimpleFileUploadListener(){
                @Override
                public void onUploadCompleted(FileResource resource) {
                    Log.d(TAG, "onUploadCompleted =======" + resource.key );
                    CloudFileUploader.asyncUpload(FileURLBuilder.BUCKET_MOMENT,snsChatImage.image, new File(LvxinApplication.CACHE_DIR_IMAGE, snsChatImage.image), new SimpleFileUploadListener(){
                        @Override
                        public void onUploadCompleted(FileResource resource) {
                            Log.d(TAG, "onUploadCompleted =======" + resource.key );
                            if (snsChatImage.equals(snsImageList.get(snsImageList.size() - 1))){
                                if (failureImageList.size() == 0){
                                    listener.onUploadCompleted(resource);
                                }else {
                                    List<SNSChatImage> snsImageListTemp = new ArrayList<>();
                                    for (SNSChatImage image : failureImageList) {
                                        snsImageListTemp.add(image.clone());
                                    }
                                    uploadImage(snsImageListTemp, listener);
                                    failureImageList.clear();
                                }
                            }
                        }

                        @Override
                        public void onUploadFailured(FileResource resource, Exception e) {
                            Log.d(TAG, "onUploadFailured =======" + resource.key );
                            if (!failureImageList.contains(snsChatImage)){
                                failureImageList.add(snsChatImage);
                            }
                            listener.onUploadFailured(resource,e);
                        }
                    });
                }

                @Override
                public void onUploadFailured(FileResource resource, Exception e) {
                    Log.d(TAG, "onUploadFailured =======" + resource.key );
                    failureImageList.add(snsChatImage);
                    listener.onUploadFailured(resource,e);
                }
            });
        }
    }

    /** 上传视频封装
     * */
    private void uploadVideo(final SNSVideo video, final OSSFileUploadListener listener) {
        CloudFileUploader.asyncUpload(FileURLBuilder.BUCKET_MOMENT,video.image, new File(LvxinApplication.CACHE_DIR_VIDEO, video.image), new SimpleFileUploadListener(){
            @Override
            public void onUploadCompleted(FileResource resource) {
                Log.d(TAG, "onUploadCompleted =======" + resource.key );
                CloudFileUploader.asyncUpload(FileURLBuilder.BUCKET_MOMENT,video.video, new File(LvxinApplication.CACHE_DIR_VIDEO, video.video), new SimpleFileUploadListener(){
                    @Override
                    public void onUploadCompleted(FileResource resource) {
                        Log.d(TAG, "onUploadCompleted =======" + resource.key );
                        listener.onUploadCompleted(resource);
                    }

                    @Override
                    public void onUploadFailured(FileResource resource, Exception e) {
                        Log.d(TAG, "onUploadFailured =======" + resource.key );
                        listener.onUploadFailured(resource,e);
                    }
                });
            }

            @Override
            public void onUploadFailured(FileResource resource, Exception e) {
                Log.d(TAG, "onUploadFailured =======" + resource.key );
                listener.onUploadFailured(resource,e);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemClicked(Object obj, View view) {
        if (adapter.getListSize() == Constant.MAX_MOMENT_PHOTO_SIZE) {
            showToastView(R.string.tip_max_nine_picture);
        } else {
            customDialog.show();
        }
    }
}
