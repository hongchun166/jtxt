
package com.linkb.jstx.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.linkb.jstx.component.WebImageView;
import com.linkb.R;

public class AlbumBucketViewHolder extends RecyclerView.ViewHolder {
    public WebImageView imageView;
    public RadioButton mark;
    public TextView name;

    public AlbumBucketViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.image);
        mark = itemView.findViewById(R.id.mark);
        name = itemView.findViewById(R.id.name);
    }
}
