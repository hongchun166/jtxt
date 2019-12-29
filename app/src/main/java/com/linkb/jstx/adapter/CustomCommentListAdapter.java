
package com.linkb.jstx.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.linkb.jstx.component.EmoticonTextView;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.model.Comment;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.util.AppTools;
import com.linkb.jstx.model.Moment;
import com.linkb.R;
import com.linkb.jstx.util.FileURLBuilder;

public class CustomCommentListAdapter extends BaseAdapter {
    private Moment moment;

    public CustomCommentListAdapter(Moment moment) {
        super();
        this.moment = moment;
    }

    @Override
    public int getCount() {
        return moment.getTextCount();
    }

    @Override
    public Comment getItem(int position) {
        return moment.getTextList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int index, View itemView, ViewGroup parent) {

        Comment comment = getItem(index);
        ViewHolder viewHolder = null;
        if (itemView == null) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_circle_detailed_comment, null);
            viewHolder = new ViewHolder();
            viewHolder.name = itemView.findViewById(R.id.name);
            viewHolder.reply = itemView.findViewById(R.id.reply);
            viewHolder.replyName = itemView.findViewById(R.id.replyName);
            viewHolder.icon = itemView.findViewById(R.id.icon);
            viewHolder.content = itemView.findViewById(R.id.content);
            viewHolder.time = itemView.findViewById(R.id.time);
            itemView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) itemView.getTag();
        }

        itemView.setTag(R.id.target, comment);

        viewHolder.icon.load(FileURLBuilder.getUserIconUrl(comment.account), R.mipmap.lianxiren);
//        viewHolder.name.setText(FriendRepository.queryFriendName(comment.account));
        Friend.asynTextViewName(viewHolder.name, comment.account);

        viewHolder.content.setText(TextUtils.htmlEncode(comment.content));
        viewHolder.time.setText(AppTools.howTimeAgo(comment.timestamp));
        if (Comment.TYPE_1.equals(comment.type)) {
            viewHolder.reply.setVisibility(View.VISIBLE);
            viewHolder.replyName.setVisibility(View.VISIBLE);
//            viewHolder.replyName.setText(FriendRepository.queryFriendName(comment.reply));
            Friend.asynTextViewName(viewHolder.replyName, comment.reply);
        } else {
            viewHolder.reply.setVisibility(View.GONE);
            viewHolder.replyName.setVisibility(View.GONE);
        }
        return itemView;
    }


    private static class ViewHolder {
        TextView time;
        EmoticonTextView content;
        TextView name;
        TextView reply;
        TextView replyName;
        WebImageView icon;
    }


}
