
package com.linkb.jstx.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class TextViewHolder extends RecyclerView.ViewHolder {
    public TextView textView;

    public TextViewHolder(View itemView) {
        super(itemView);
        textView = (TextView) itemView;
    }
}
