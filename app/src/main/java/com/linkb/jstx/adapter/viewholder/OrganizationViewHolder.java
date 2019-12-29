
package com.linkb.jstx.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.linkb.R;

public class OrganizationViewHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public View icon;

    public OrganizationViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        icon = itemView.findViewById(R.id.icon);
    }
}
