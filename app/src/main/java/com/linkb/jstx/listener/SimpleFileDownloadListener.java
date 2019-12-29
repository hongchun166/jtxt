
package com.linkb.jstx.listener;

import java.io.File;

public class SimpleFileDownloadListener implements OSSFileDownloadListener{

    @Override
    public void onDownloadCompleted(File file, String currentKey) {

    }

    @Override
    public void onDownloadFailured(File file, String currentKey) {

    }

    @Override
    public void onDownloadProgress(String key, float progress) {

    }
}
