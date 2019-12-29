
package com.linkb.jstx.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.linkb.R;


public class MapAddressViewHolder extends RecyclerView.ViewHolder {
    public TextView title;
    public TextView content;

    public MapAddressViewHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        content = itemView.findViewById(R.id.content);
    }
}
