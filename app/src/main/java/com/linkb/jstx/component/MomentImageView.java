
package com.linkb.jstx.component;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;

import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.model.Moment;
import com.linkb.jstx.network.model.SNSMomentImage;
import com.linkb.R;
import com.linkb.jstx.util.FileURLBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;


public class MomentImageView extends WebImageView implements OnClickListener {

    private SNSMomentImage image;

    public MomentImageView(Context context) {
        super(context);
        this.setOnClickListener(this);
    }

    public MomentImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnClickListener(this);
    }


    /**
     * @filename 文件名
     * @return 如果本地存在这个文件返回 本地文件file://{filepath},否则返回http url
     */
    private String getOptimalImageUrl(String filename){
        File target = new File(LvxinApplication.CACHE_DIR_IMAGE,filename);
        if (target.exists()){
            return Uri.fromFile(target).toString();
        }else {
            return FileURLBuilder.getMomentFileUrl(filename);
        }
    }
    public void display(Moment target, SNSMomentImage image) {
        this.setTag(R.id.target, target);
        this.image = image;

        /**
          tag统一使用http url，为了转场动画结束，通过tag在列表里面找到对应的imageview
         */
        setTag(FileURLBuilder.getMomentFileUrl(image.thumb));

        /**
         * 获取本地文件地址或者远程地址，减少网络加载的次数
         */
        load(getOptimalImageUrl(image.thumb), R.color.def_imageview_color);
    }

    public void displayFitSize(Moment target, SNSMomentImage image) {
        this.setTag(R.id.target, target);
        this.image = image;

        int w = image.ow;
        int h = image.oh;
        int circleSinglePhotoSide = getContext().getResources().getDimensionPixelOffset(R.dimen.circle_single_photo_side);

        if (w < circleSinglePhotoSide && h < circleSinglePhotoSide) {
            this.getLayoutParams().width = w;
            this.getLayoutParams().height = h;
        } else {
            if (w >= h) {
                this.getLayoutParams().width = circleSinglePhotoSide;
                this.getLayoutParams().height = circleSinglePhotoSide * h / w;
            } else {
                this.getLayoutParams().height = circleSinglePhotoSide;
                this.getLayoutParams().width = circleSinglePhotoSide * w / h;
            }
        }
        load(getOptimalImageUrl(image.image), R.color.def_imageview_color);
    }

    @Override
    public void onClick(View view) {
        Moment moment = (Moment) view.getTag(R.id.target);
        if (moment.type.equals(Moment.FORMAT_MULTI_IMAGE)) {
            ArrayList<SNSMomentImage> snsImageList = new Gson().fromJson(moment.content, new TypeToken<ArrayList<SNSMomentImage>>() {}.getType());
            snsImageList.remove(image);
            snsImageList.add(0, image);
            LvxinApplication.getInstance().startGalleryPhotoActivity(getContext(), snsImageList, this);

        }
        if (moment.type.equals(Moment.FORMAT_IMAGE)) {
            LvxinApplication.getInstance().startPhotoActivity(getContext(), image, this);
        }
    }
}
