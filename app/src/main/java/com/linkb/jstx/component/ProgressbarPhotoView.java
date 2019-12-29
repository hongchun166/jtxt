
package com.linkb.jstx.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.linkb.jstx.listener.CloudImageApplyListener;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.network.CloudImageLoaderFactory;
import com.linkb.jstx.network.model.SNSImage;
import com.linkb.R;
import com.linkb.jstx.util.FileURLBuilder;
import com.github.chrisbanes.photoview.OnPhotoTapListener;


public class ProgressbarPhotoView extends RelativeLayout implements OnPhotoTapListener, CloudImageApplyListener {
    private OnItemClickedListener onPhotoViewClickListener;
    private ProgressBar progressbar;
    private WebPhotoView photoView;
    private Bitmap mBitmap;

    public ProgressbarPhotoView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }


    @Override
    public void onImageApplyFailure(Object key, ImageView target) {
        progressbar.setVisibility(GONE);
        photoView.setImageResource(R.drawable.picture_def);
    }

    @Override
    public void onImageApplySucceed(Object key, ImageView target, BitmapDrawable resource) {
        progressbar.setVisibility(GONE);
        mBitmap = resource.getBitmap();
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setOnPhotoViewClickListener(
            OnItemClickedListener onPhotoViewClickListener) {
        this.onPhotoViewClickListener = onPhotoViewClickListener;
    }

    @Override
    public void onFinishInflate() {
        super.onDetachedFromWindow();
        super.onFinishInflate();
        photoView = findViewById(R.id.image);
        progressbar = findViewById(R.id.progress);
        photoView.setOnPhotoTapListener(this);
    }


    public void display(final SNSImage image, final CloudImageApplyListener listener) {
        progressbar.setVisibility(View.VISIBLE);
        final String thumbnailUrl = FileURLBuilder.getFileUrl(image.getBucket(),image.thumb);
        final String imageUrl = FileURLBuilder.getFileUrl(image.getBucket(),image.image);
        photoView.setTag(thumbnailUrl);
        ViewCompat.setTransitionName(photoView,thumbnailUrl);

        CloudImageLoaderFactory.get().loadAndApply(photoView,thumbnailUrl, new CloudImageApplyListener() {
            @Override
            public void onImageApplyFailure(Object key, ImageView target) {
                onImageApplySucceed(key, photoView,null);
            }

            @Override
            public void onImageApplySucceed(Object key, ImageView target,final BitmapDrawable resource) {
                if (!photoView.isDetachedFromWindow()) {
                    listener.onImageApplySucceed(key,photoView,resource);
                    photoView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (resource == null){
                                photoView.display(imageUrl,ProgressbarPhotoView.this);
                            }else {
                                photoView.display(imageUrl,resource.getBitmap(),ProgressbarPhotoView.this);
                            }
                        }
                    },500);
                }
            }


        });

    }


    public void display(String imageUrl) {
        photoView.display(imageUrl, this);
    }


    @Override
    public void onPhotoTap(ImageView imageView, float v, float v1) {
        onPhotoViewClickListener.onItemClicked(null, photoView);
    }
}
