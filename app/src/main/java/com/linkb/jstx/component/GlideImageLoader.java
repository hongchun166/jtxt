package com.linkb.jstx.component;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.linkb.R;
import com.youth.banner.loader.ImageLoader;

/** 发现页面Banner 图片加载器
* */
public class GlideImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //Glide 加载图片简单用法
        Glide.with(context)
                .setDefaultRequestOptions(
                        new RequestOptions()
                                .frame(1000000)
                                .centerCrop()
                                .placeholder(R.mipmap.banner_load_failed))
                .load(path)
                .into(imageView);
    }


}
