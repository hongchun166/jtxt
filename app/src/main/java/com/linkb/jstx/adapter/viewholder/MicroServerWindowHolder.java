
package com.linkb.jstx.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.linkb.jstx.component.BaseMicroServerFromView;
import com.linkb.jstx.component.MicroServerToTextView;
import com.linkb.R;

public class MicroServerWindowHolder extends RecyclerView.ViewHolder {
    public MicroServerToTextView toTextMessageView;
    public BaseMicroServerFromView replyMessageView;
    public TextView dateTime;

    public MicroServerWindowHolder(View itemMessageView) {
        super(itemMessageView);
        if (itemMessageView instanceof MicroServerToTextView) {
            toTextMessageView = (MicroServerToTextView) itemMessageView;
        }
        if (itemMessageView instanceof BaseMicroServerFromView) {
            replyMessageView = (BaseMicroServerFromView) itemMessageView;
        }
        dateTime  = itemMessageView.findViewById(R.id.datetime);

    }
}
