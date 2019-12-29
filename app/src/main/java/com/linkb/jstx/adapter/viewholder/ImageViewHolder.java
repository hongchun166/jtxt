
package com.linkb.jstx.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.linkb.jstx.component.WebImageView;

public class ImageViewHolder extends RecyclerView.ViewHolder {
    public WebImageView imageView;

    public ImageViewHolder(View itemView) {
        super(itemView);
        imageView = (WebImageView) itemView;
    }
}
