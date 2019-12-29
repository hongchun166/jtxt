
package com.linkb.jstx.network;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
import com.linkb.jstx.database.GlideImageRepository;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.listener.CloudImageApplyListener;
import com.linkb.jstx.listener.CloudImageLoadListener;
import com.linkb.jstx.util.FileURLBuilder;

import java.io.File;
import java.security.MessageDigest;

public class GlideImageLoader implements CloudImageLoader {

    @Override
    public void loadAndApply(ImageView target, String url, @DrawableRes int defRes, float round, final CloudImageApplyListener listener) {
        loadAndApply(target, url, defRes == 0 ? null : ContextCompat.getDrawable(target.getContext(), defRes), round, listener);
    }

    @Override
    public void loadAndApply(ImageView target, String url, Bitmap defResBitmap, CloudImageApplyListener listener) {
        loadAndApply(target, url, new BitmapDrawable(target.getResources(), defResBitmap), 0, listener);
    }

    private void loadAndApply(ImageView target, String url, Drawable defDrawable, float round, final CloudImageApplyListener listener) {
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .format(DecodeFormat.PREFER_RGB_565)
                .dontAnimate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            options.disallowHardwareConfig();
        }
        if (defDrawable != null) {
            options.error(defDrawable);
            options.placeholder(defDrawable);
        }
        if (round > 0) {
            options.transform(new GlideCircleTransform(round));
        }
        if (url != null && FileURLBuilder.isLogo(url)) {
            String version = GlideImageRepository.getVersion(url);
            if (!TextUtils.isEmpty(version)) {
                options.signature(new ObjectKey(version));
            }
        }

        RequestBuilder request = Glide.with(LvxinApplication.getInstance())
                .asBitmap()
                .load(url)
                .apply(options);
        if (listener != null) {
            request.listener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object o, Target target, boolean b) {
                    ImageView targetView = ((BitmapImageViewTarget) target).getView();
                    listener.onImageApplyFailure(o, targetView);
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap bitmap, Object o, Target<Bitmap> target, DataSource dataSource, boolean b) {
                    ImageView targetView = ((BitmapImageViewTarget) target).getView();
                    listener.onImageApplySucceed(o, targetView, new BitmapDrawable(LvxinApplication.getInstance().getResources(), bitmap));
                    return false;
                }
            });
        }

        request.into(target);

    }


    @Override
    public void loadAndApply(ImageView target, File file, @DrawableRes int defResId, float round, CloudImageApplyListener listener) {
        loadAndApply(target, Uri.fromFile(file).toString(), defResId, round, listener);
    }

    @Override
    public void loadAndApply(ImageView target, File file, @DrawableRes int defResId, float round) {
        loadAndApply(target, Uri.fromFile(file).toString(), defResId, round, null);
    }

    @Override
    public void loadAndApply(ImageView target, String url, @DrawableRes int defResId, float round) {
        loadAndApply(target, url, defResId, round, null);

    }

    @Override
    public void loadAndApply(ImageView target, File file, @DrawableRes int defResId) {
        loadAndApply(target, Uri.fromFile(file).toString(), defResId, 0, null);
    }

    @Override
    public void loadAndApply(ImageView target, String url, @DrawableRes int defResId) {
        loadAndApply(target, url, defResId, 0, null);

    }

    @Override
    public void loadAndApply(ImageView target, File file, CloudImageApplyListener listener) {
        loadAndApply(target, Uri.fromFile(file).toString(), 0, 0, listener);

    }

    @Override
    public void loadAndApply(ImageView target, String url, CloudImageApplyListener listener) {
        loadAndApply(target, url, 0, 0, listener);

    }


    @Override
    public void loadAndApply(ImageView target, File file, @DrawableRes int defResId, CloudImageApplyListener listener) {
        loadAndApply(target, Uri.fromFile(file).toString(), defResId, 0, listener);

    }

    @Override
    public void loadAndApply(ImageView target, String url, @DrawableRes int defResId, CloudImageApplyListener listener) {
        loadAndApply(target, url, defResId, 0, listener);
    }

    @Override
    public void loadGifAndApply(ImageView target, String url) {
        RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);
        options.format(DecodeFormat.PREFER_RGB_565);
        RequestBuilder request = Glide.with(LvxinApplication.getInstance()).asGif().load(url).apply(options);
        request.into(target);
    }

    @Override
    public void downloadOnly(String url) {
        downloadOnly(url, null);
    }

    @Override
    public void downloadOnly(String url, CloudImageLoadListener listener) {
        downloadOnly(Uri.parse(url), SimpleTarget.SIZE_ORIGINAL, listener);
    }

    @Override
    public void downloadOnly(File file, CloudImageLoadListener listener) {
        downloadOnly(Uri.fromFile(file).toString(), listener);
    }
    @Override
    public void downloadOnly(Uri file, CloudImageLoadListener listener) {
        downloadOnly(file, SimpleTarget.SIZE_ORIGINAL, listener);
    }

    @Override
    public void downloadOnly(final Uri url, int size, final CloudImageLoadListener listener) {
        RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            options.disallowHardwareConfig();
        }
        if (url != null && FileURLBuilder.isLogo(url.toString())) {
            String version = GlideImageRepository.getVersion(url.toString());
            if (!TextUtils.isEmpty(version)) {
                options.signature(new ObjectKey(version));
            }
        }
        RequestBuilder request = Glide.with(LvxinApplication.getInstance()).asBitmap().load(url).apply(options);

        request.into(new SimpleTarget<Bitmap>(size, size) {

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                if (listener != null) {
                    listener.onImageLoadFailure(url);
                }
            }

            @Override
            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                if (listener != null) {
                    listener.onImageLoadSucceed(url, bitmap);
                }
            }
        });
    }


    @Override
    public void clearMemory() {
        Glide.get(LvxinApplication.getInstance()).clearMemory();
    }

    @Override
    public void clearDiskCache() {
        Glide.get(LvxinApplication.getInstance()).clearDiskCache();
    }

    static class GlideCircleTransform extends BitmapTransformation {
        float rounded;

        GlideCircleTransform(float rounded) {
            this.rounded = (Resources.getSystem().getDisplayMetrics().density * rounded + 0.5f);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap source, int outWidth, int outHeight) {
            if (source == null) {
                return null;
            }
            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }
            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            canvas.drawRoundRect(rectF, rounded, rounded, paint);
            return result;
        }

        @Override
        public void updateDiskCacheKey(MessageDigest messageDigest) {
        }
    }
}
