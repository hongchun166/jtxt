
package com.linkb.jstx.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.linkb.jstx.component.WebImageView;
import com.linkb.R;

public class MomentMessageViewHolder extends RecyclerView.ViewHolder {
    public TextView time;
    public TextView content;
    public TextView name;
    public WebImageView icon;

    public MomentMessageViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        icon = itemView.findViewById(R.id.icon);
        content = itemView.findViewById(R.id.content);
        time = itemView.findViewById(R.id.time);
    }
}
