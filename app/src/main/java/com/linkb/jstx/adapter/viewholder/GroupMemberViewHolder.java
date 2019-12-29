
package com.linkb.jstx.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.linkb.jstx.component.WebImageView;
import com.linkb.R;


public class GroupMemberViewHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public WebImageView icon;
    public View mark;
    public RadioButton radioButton;

    public GroupMemberViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        icon = itemView.findViewById(R.id.icon);
        mark = itemView.findViewById(R.id.mark);
        radioButton = itemView.findViewById(R.id.radioButton);
    }
}
