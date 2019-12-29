
package com.linkb.jstx.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.linkb.R;

public class AppSettingViewHolder extends RecyclerView.ViewHolder {
    public TextView label;
    public TextView hint;
    public Switch appSwitch;

    public AppSettingViewHolder(View itemView) {
        super(itemView);
        label = itemView.findViewById(R.id.label);
        hint = itemView.findViewById(R.id.hint);
        appSwitch = itemView.findViewById(R.id.appSwitch);
    }
}
