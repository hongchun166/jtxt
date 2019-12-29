
package com.linkb.jstx.listener;

import com.linkb.jstx.bean.FileResource;

public interface OSSFileUploadListener {
    void onUploadCompleted(FileResource resource);

    void onUploadProgress(String key, float progress);

    void onUploadFailured(FileResource resource, Exception e);
}
