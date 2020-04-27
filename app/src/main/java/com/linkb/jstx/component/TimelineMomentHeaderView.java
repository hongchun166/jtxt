
package com.linkb.jstx.component;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.linkb.jstx.model.Comment;
import com.linkb.jstx.activity.trend.MomentMessageActivity;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.model.Message;
import com.linkb.R;
import com.linkb.jstx.util.FileURLBuilder;
import com.google.gson.Gson;

import java.util.List;

public class TimelineMomentHeaderView extends LinearLayout implements View.OnClickListener {
    private OnItemClickedListener onIconClickedListener;
    private WebImageView wallpaper;

    public TimelineMomentHeaderView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        findViewById(R.id.sns_message_remind).setOnClickListener(this);
        findViewById(R.id.iconCardView).setOnClickListener(this);
        wallpaper = findViewById(R.id.wallpaper);
        int height = (int) (Resources.getSystem().getDisplayMetrics().widthPixels / 1.8);
        wallpaper.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,height));
    }

    public void showMessageRemind(List<Message> msgList) {
        if (!msgList.isEmpty()) {
            findViewById(R.id.sns_message_remind).setVisibility(View.VISIBLE);
            for (int i = 0; i < msgList.size(); i++) {
                Comment comment = new Gson().fromJson(msgList.get(i).content, Comment.class);
                if (i == 0) {
                    ((WebImageView) findViewById(R.id.sns_notify_firstimg)).load(FileURLBuilder.getUserIconUrl(comment.account), R.mipmap.lianxiren);
                }
                if (i == 1) {
                    ((WebImageView) findViewById(R.id.sns_notify_secondimg)).load(FileURLBuilder.getUserIconUrl(comment.account), R.mipmap.lianxiren);
                }
                if (i == 2) {
                    ((WebImageView) findViewById(R.id.sns_notify_lastimg)).load(FileURLBuilder.getUserIconUrl(comment.account), R.mipmap.lianxiren);
                }
            }
        } else {
            findViewById(R.id.sns_message_remind).setVisibility(View.GONE);
        }
    }

    public void displayBg(String url){
        ((WebImageView) findViewById(R.id.wallpaper)).load(url, R.drawable.circle_banner_normal);
    }
    public void displayIcon(String url) {
        ((WebImageView) findViewById(R.id.icon)).load(url, R.mipmap.lianxiren);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sns_message_remind:
                Intent intent = new Intent(getContext(), MomentMessageActivity.class);
                getContext().startActivity(intent);
                findViewById(R.id.sns_message_remind).setVisibility(View.GONE);
                break;
            case R.id.iconCardView:
                if (onIconClickedListener != null) {
                    onIconClickedListener.onItemClicked(view.getId(), view);
                }
                break;
        }
    }

    public void setOnIconClickedListener(OnItemClickedListener onIconClickedListener) {
        this.onIconClickedListener = onIconClickedListener;
    }


}
