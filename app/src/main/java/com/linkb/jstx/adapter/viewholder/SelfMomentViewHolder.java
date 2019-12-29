
package com.linkb.jstx.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import com.linkb.jstx.component.MomentImageView;
import com.linkb.jstx.component.WebImageView;
import com.linkb.R;


public class SelfMomentViewHolder extends RecyclerView.ViewHolder {
    public int viewType;
    public TextView text;
    public MomentImageView singleImageView;
    public GridLayout gridImageLayout;
    public View linkPanel;
    public TextView linkTitle;
    public TextView day;
    public TextView month;
    public WebImageView thumbnailView;

    public SelfMomentViewHolder(View itemView, int viewType) {
        super(itemView);
        this.viewType = viewType;
        thumbnailView = itemView.findViewById(R.id.thumbnailView);
        singleImageView = itemView.findViewById(R.id.singleImageView);
        gridImageLayout = itemView.findViewById(R.id.imageGridLayout);
        day = itemView.findViewById(R.id.day);
        month = itemView.findViewById(R.id.month);
        text = itemView.findViewById(R.id.text);
        linkTitle = itemView.findViewById(R.id.linkTitle);
        linkPanel = itemView.findViewById(R.id.linkPanel);
    }
}
