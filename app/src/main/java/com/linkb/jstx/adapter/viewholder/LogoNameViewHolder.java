
package com.linkb.jstx.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.linkb.jstx.component.WebImageView;
import com.linkb.R;

public class LogoNameViewHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public WebImageView icon;
    public View badge;

    public LogoNameViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        icon = itemView.findViewById(R.id.icon);
        badge = itemView.findViewById(R.id.badge);
    }
}
