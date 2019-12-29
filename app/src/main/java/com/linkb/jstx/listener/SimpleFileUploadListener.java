
package com.linkb.jstx.listener;

import com.linkb.jstx.bean.FileResource;

public class SimpleFileUploadListener implements OSSFileUploadListener {

    @Override
    public void onUploadCompleted(FileResource resource) {

    }

    @Override
    public void onUploadProgress(String key, float progress) {

    }

    @Override
    public void onUploadFailured(FileResource resource, Exception e) {

    }
}
