
package com.linkb.jstx.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linkb.jstx.component.ProgressbarPhotoView;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.network.model.SNSChatImage;
import com.linkb.jstx.network.model.SNSImage;
import com.linkb.jstx.listener.CloudImageApplyListener;
import com.linkb.R;

import java.util.List;

public class GalleryPhotoViewAdapter extends PagerAdapter {

    private List<SNSImage> list;
    private OnItemClickedListener onItemClickedListener;
    private CloudImageApplyListener cloudImageApplyListener;
    public GalleryPhotoViewAdapter(List<SNSImage> list) {
        this.list = list;
    }

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    public void setCloudImageApplyListener(CloudImageApplyListener cloudImageApplyListener) {
        this.cloudImageApplyListener = cloudImageApplyListener;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {

        ProgressbarPhotoView photoView = (ProgressbarPhotoView) LayoutInflater.from(container.getContext()).inflate(R.layout.layout_progressbar_photoview, null);

        photoView.setTag(position);
        photoView.setOnPhotoViewClickListener(onItemClickedListener);
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        photoView.display(list.get(position),cloudImageApplyListener);
        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void addAll(List<SNSChatImage> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public SNSImage getItem(int position) {
       return  list.get(position);
    }
}
