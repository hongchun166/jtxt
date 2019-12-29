
package com.linkb.jstx.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.linkb.R;


public class ContactsHeaderHolder extends RecyclerView.ViewHolder {
    public View orgBar;
    public View groupBar;
    public View accountBar;
    public View tagBar;

    public ContactsHeaderHolder(View itemView) {
        super(itemView);
        orgBar = itemView.findViewById(R.id.orgBar);
        groupBar = itemView.findViewById(R.id.groupBar);
        accountBar = itemView.findViewById(R.id.accountBar);
        tagBar = itemView.findViewById(R.id.tagBar);
    }
}
