
package com.linkb.jstx.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.linkb.jstx.component.WebImageView;
import com.linkb.R;


public class SystemMessageViewHolder extends RecyclerView.ViewHolder {
    public TextView categoryText;
    public TextView time;
    public WebImageView icon;
    public TextView content;
    public Button handleButton;
    public TextView tipText;

    public SystemMessageViewHolder(View itemView) {
        super(itemView);
        categoryText = itemView.findViewById(R.id.categoryText);
        time = itemView.findViewById(R.id.time);
        icon = itemView.findViewById(R.id.icon);
        content = itemView.findViewById(R.id.content);
        tipText = itemView.findViewById(R.id.tipText);
        handleButton = itemView.findViewById(R.id.handleButton);
    }
}
