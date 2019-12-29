
package com.linkb.jstx.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import com.linkb.jstx.component.WebImageView;
import com.linkb.R;

public class AlbumItemViewHolder extends RecyclerView.ViewHolder {
    public WebImageView imageView;
    public CheckBox checkbox;

    public AlbumItemViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.image);
        checkbox = itemView.findViewById(R.id.checkbox);

    }
}
