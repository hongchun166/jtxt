
package com.linkb.jstx.network;


public class CloudImageLoaderFactory {
    private static final int MODE_GLIDE = 0;
    public static final int MODE_FRESC0 = 1;
    private static GlideImageLoader glideImageLoader = new GlideImageLoader();

    public static CloudImageLoader get() {
        return glideImageLoader;
    }

    public static CloudImageLoader get(int mode) {
        if (mode == MODE_GLIDE) {
            return glideImageLoader;
        }
        return glideImageLoader;
    }
}
