
package com.linkb.jstx.listener;

import android.hardware.Camera;

public interface OnCameraFrameListener {
    void onPreviewFrame(byte[] data, Camera camera);
}
