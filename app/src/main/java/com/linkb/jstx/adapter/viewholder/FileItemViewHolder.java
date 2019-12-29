
package com.linkb.jstx.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkb.R;

public class FileItemViewHolder extends RecyclerView.ViewHolder {
    public ImageView icon;
    public CheckBox checkBox;
    public TextView name;
    public TextView description;

    public FileItemViewHolder(View itemView) {
        super(itemView);
        icon = itemView.findViewById(R.id.icon);
        checkBox = itemView.findViewById(R.id.checkbox);
        name = itemView.findViewById(R.id.name);
        description = itemView.findViewById(R.id.description);

    }
}
