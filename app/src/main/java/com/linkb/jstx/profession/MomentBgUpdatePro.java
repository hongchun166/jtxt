package com.linkb.jstx.profession;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.linkb.R;
import com.linkb.jstx.activity.util.PhotoAlbumActivity;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.FileResource;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.listener.OSSFileUploadListener;
import com.linkb.jstx.listener.SimpleFileUploadListener;
import com.linkb.jstx.network.CloudFileUploader;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManagerV2;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.util.AppTools;
import com.linkb.jstx.util.BackgroundThreadHandler;
import com.linkb.jstx.util.FileURLBuilder;

import java.io.File;

public class MomentBgUpdatePro {

    public interface OnMomentBgUpdateProCallback{
        void onShowProgressDialog(boolean hasShow,String msg);
        void onMomentBgUpdateCallback(boolean hasSuc,String key,Object object);
    }

    static final String TAG=MomentBgUpdatePro.class.getSimpleName();

    final int REQUEST_CODE_MomentsBg=0x19;
    final int REQUEST_CODE_MomentsBgZoom=0x20;

    OnMomentBgUpdateProCallback onMomentBgUpdateProCallback;

    AppCompatActivity context;

    public void release(){
        this.context=null;
        this.onMomentBgUpdateProCallback=null;
    }
    private Context getContext(){
        return context;
    }
    private AppCompatActivity getActivity(){
        return context;
    }
    private String getString(int resId,Object... formatArgs){
        if(context==null)return "";
        return context.getResources().getString(resId,formatArgs);
    }

    public void setOnMomentBgUpdateProCallback(OnMomentBgUpdateProCallback onMomentBgUpdateProCallback) {
        this.onMomentBgUpdateProCallback = onMomentBgUpdateProCallback;
    }

    public void navToSelectPhoto(AppCompatActivity context){
        this.context=context;
        context.startActivityForResult(new Intent(context, PhotoAlbumActivity.class), REQUEST_CODE_MomentsBg);
    }
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_MomentsBg) {
            if(getActivity()!=null){
                AppTools.startIconPhotoCrop(getActivity(), data.getData(),350,350,REQUEST_CODE_MomentsBgZoom);
            }
        }
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_MomentsBgZoom){
            File photo = new File(Global.getCropPhotoFilePath());
            upLoadMomentsBg(photo);
        }
    }
    private void upLoadMomentsBg( File photoBg){
        if(!photoBg.exists()){
            System.out.println(" uploadSingleImage MOMENT file not exists ");
        }
        String filePatch=photoBg.getAbsolutePath();
        final String fileName=filePatch.substring(filePatch.lastIndexOf("/")+1);

        if(onMomentBgUpdateProCallback!=null) {
            onMomentBgUpdateProCallback.onShowProgressDialog(true, getString(R.string.tip_file_uploading, 0));
        }
        uploadSingleImage(photoBg,fileName,new SimpleFileUploadListener(){
            @Override
            public void onUploadCompleted(FileResource resource) {

                if(onMomentBgUpdateProCallback!=null){
                    onMomentBgUpdateProCallback.onMomentBgUpdateCallback(true,resource.key,null);
                }

                final User userTemp=new User();
                userTemp.backgroudUrl=resource.key;
                HttpServiceManagerV2.updateUserInfo(userTemp,httpRequestListener);
                User user=Global.getCurrentUser();
                user.backgroudUrl=userTemp.backgroudUrl;
                Global.modifyAccount(user);
            }
            @Override
            public void onUploadProgress(String key, float progress) {
                super.onUploadProgress(key, progress);
                if(onMomentBgUpdateProCallback!=null) {
                    onMomentBgUpdateProCallback.onShowProgressDialog(true, getString(R.string.tip_file_uploading, (int) progress));
                }
            }

            @Override
            public void onUploadFailured(FileResource resource, Exception e) {
                e.printStackTrace();
                if(onMomentBgUpdateProCallback!=null) onMomentBgUpdateProCallback.onShowProgressDialog(false,"");
            }
        });
    }
    /** 上传单张图片封装
     * */
    private void uploadSingleImage(final File file,String fileKey, final OSSFileUploadListener listener) {
        CloudFileUploader.asyncUpload(FileURLBuilder.BUCKET_MOMENT,fileKey,file, new SimpleFileUploadListener(){
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
    HttpRequestListener httpRequestListener=new HttpRequestListener() {
        @Override
        public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
            if(onMomentBgUpdateProCallback!=null) onMomentBgUpdateProCallback.onShowProgressDialog(false,"");
        }
        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {
            if(onMomentBgUpdateProCallback!=null) onMomentBgUpdateProCallback.onShowProgressDialog(false,"");
        }
    };
}
