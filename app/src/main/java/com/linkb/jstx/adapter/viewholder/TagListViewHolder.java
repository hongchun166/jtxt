
package com.linkb.jstx.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class TagListViewHolder extends RecyclerView.ViewHolder {
    public TextView name;

    public TagListViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView;
    }
}
