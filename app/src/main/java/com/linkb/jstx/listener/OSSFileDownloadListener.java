
package com.linkb.jstx.listener;

import java.io.File;

public interface OSSFileDownloadListener {
    void onDownloadCompleted(File file, String currentKey);

    void onDownloadFailured(File file, String currentKey);

    void onDownloadProgress(String key, float progress);
}
