
package com.linkb.jstx.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.linkb.jstx.component.BaseFromMessageView;
import com.linkb.jstx.component.BaseToMessageView;
import com.linkb.R;

public class ChatRecordViewHolder extends RecyclerView.ViewHolder {
    public BaseToMessageView toMessageView;
    public BaseFromMessageView fromMessageView;
    public TextView dateTime;

    public ChatRecordViewHolder(View itemMessageView) {
        super(itemMessageView);
        dateTime  = itemMessageView.findViewById(R.id.datetime);
        toMessageView = itemMessageView.findViewById(R.id.to_message_view);
        fromMessageView = itemMessageView.findViewById(R.id.from_message_view);
    }
}
