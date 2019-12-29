
package com.linkb.jstx.network;

/*import android.content.Intent;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;*/


/**
 * 全局的文件上传工具
 *//*

public class OSSCloudFileUploader {

    private final static String endpoint = "http://oss-cn-beijing.aliyuncs.com";
    // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建
    private final static String accessKeyId = "LTAIVMcNTJnyrXiI";
    private final static String accessKeySecret = "p20MGqtloBZ6tUliFd3nHamNwTJpeZ";
    final static OSSCredentialProvider OSS_CREDENTIAL_PROVIDER = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);

    public static void asyncUpload(final String objectKey, final File file,
                                   final OSSFileUploadListener callback) {
        asyncUpload(FileURLBuilder.BUCKET_CHAT, objectKey, file, callback);

    }

    public static void asyncUpload(final String bucket, final String objectKey, final File file, final OSSFileUploadListener callback) {


        OSS oss = new OSSClient(LvxinApplication.getInstance(), endpoint, OSS_CREDENTIAL_PROVIDER);
        PutObjectRequest put = new PutObjectRequest(bucket, objectKey, file.getAbsolutePath());
        if (callback != null)
            put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
                @Override
                public void onProgress(PutObjectRequest request, final long currentSize, final long totalSize) {
                    BackgroundThreadHandler.postUIThread(new Runnable() {
                        public void run() {
                            callback.onUploadProgress(objectKey, (float) (currentSize * 100l) / totalSize);
                        }
                    });
                }
            });
        oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                if (callback != null) {
                    BackgroundThreadHandler.postUIThread(new Runnable() {
                        public void run() {
                            callback.onUploadCompleted(objectKey, file);
                        }
                    });
                }
                Intent intent = new Intent(Constant.Action.ACTION_UPLOAD_PROGRESS);
                intent.putExtra("objectKey", objectKey);
                intent.putExtra("progress", 100f);
                LvxinApplication.getInstance().LvxinApplication.sendLocalBroadcast(intent);
            }

            @Override
            public void onFailure(PutObjectRequest putObjectRequest, final ClientException e, final ServiceException e1) {
                if (callback != null) {
                    BackgroundThreadHandler.postUIThread(new Runnable() {
                        public void run() {
                            callback.onUploadFailured(e, objectKey);
                        }
                    });
                }
                Intent intent = new Intent(Constant.Action.ACTION_UPLOAD_PROGRESS);
                intent.putExtra("objectKey", objectKey);
                intent.putExtra("progress", -1f);
                LvxinApplication.getInstance().LvxinApplication.sendLocalBroadcast(intent);
            }
        });
    }
}
*/
