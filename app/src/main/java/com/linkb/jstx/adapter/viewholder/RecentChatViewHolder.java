
package com.linkb.jstx.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkb.jstx.component.EmoticonTextView;
import com.linkb.jstx.component.WebImageView;
import com.linkb.R;


public class RecentChatViewHolder extends RecyclerView.ViewHolder {
    public TextView time;
    public TextView name;
    public EmoticonTextView preview;
    public WebImageView icon;
    public TextView badge;
    public View reddot;
    public ImageView iconNotifyOff;

    public RecentChatViewHolder(View itemMessageView) {
        super(itemMessageView);
        icon = itemMessageView.findViewById(R.id.logo);
        name = itemMessageView.findViewById(R.id.name);
        badge = itemMessageView.findViewById(R.id.badge);
        preview = itemMessageView.findViewById(R.id.preview);
        time = itemMessageView.findViewById(R.id.time);

        iconNotifyOff = itemMessageView.findViewById(R.id.icon_notify_off);
        reddot = itemMessageView.findViewById(R.id.reddot);
    }
}
