
package com.linkb.jstx.database;


import android.support.v4.util.ArrayMap;

import com.linkb.jstx.model.GlideImage;

public class GlideImageRepository extends BaseRepository<GlideImage, String> {
    private final static ArrayMap<String, String> GLIDE_IMAGE_MAP = new ArrayMap<>(20);
    private static GlideImageRepository manager = new GlideImageRepository();


    /**
     * 图片版本信息为公共数据，不需要清除
     */
    @Override
    public  void clearTable() {
    }

    @Override
    public String getDatabaseName() {
        return DATABASE_NAME;
    }

    public static String getVersion(String url) {
        if (url == null) {
            return null;
        }
        String version = GLIDE_IMAGE_MAP.get(url);
        if (version == null) {
            GlideImage config = manager.innerQueryById(url);
            if (config != null) {
                version = config.version;
                GLIDE_IMAGE_MAP.put(url, version);
            }
        }
        if (version == null) {
            GLIDE_IMAGE_MAP.put(url, "");
        }
        return version;
    }

    public static void save(String url, String version) {
        GlideImage config = new GlideImage();
        config.url = url;
        config.version = version;
        manager.createOrUpdate(config);
        GLIDE_IMAGE_MAP.put(url, version);
    }
}
