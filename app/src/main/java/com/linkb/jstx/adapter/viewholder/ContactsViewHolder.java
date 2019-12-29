
package com.linkb.jstx.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.linkb.jstx.component.WebImageView;
import com.linkb.R;


public class ContactsViewHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public WebImageView icon;
    public View dividerView;
    public CheckBox checkBox;

    public ContactsViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        icon = itemView.findViewById(R.id.icon);
        dividerView = itemView.findViewById(R.id.divider_view);
        checkBox = itemView.findViewById(R.id.checkbox);
    }
}
