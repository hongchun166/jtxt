
package com.linkb.jstx.app;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.target.ViewTarget;
import com.linkb.R;

@GlideModule
public class GlobalGlideModule extends AppGlideModule {
    /**
     * 500 MB of cache.
     */
    private final static int DEFAULT_DISK_CACHE_SIZE = 500 * 1024 * 1024;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setLogLevel(Log.ERROR);
        builder.setDiskCache(new ExternalPreferredCacheDiskCacheFactory(context, "image", DEFAULT_DISK_CACHE_SIZE));
        //设置glide加载图片的 tag，解决 WebImageVew不能直接 setTag(Object tag)的问题
        ViewTarget.setTagId(R.id.GLIDE_TAG);
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

}
