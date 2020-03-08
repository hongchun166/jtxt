
package com.linkb.jstx.component;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.linkb.jstx.activity.chat.MMWebViewActivity;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.listener.OnCommentSelectedListener;
import com.linkb.jstx.model.Moment;
import com.linkb.jstx.network.model.MomentLink;
import com.linkb.R;
import com.google.gson.Gson;

public class TimelineMomentLinkView extends TimelineMomentView {
    private TextView linkTitle;
    private String linkUrl;

    public TimelineMomentLinkView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        linkTitle = findViewById(R.id.linkTitle);
        ((View) linkTitle.getParent()).setOnClickListener(this);
    }

    @Override
    public void displayMoment(Moment moment, User self, OnCommentSelectedListener commentSelectedListener) {
        super.displayMoment(moment, self, commentSelectedListener);
        final MomentLink publishObject = new Gson().fromJson(moment.content, MomentLink.class);
        linkUrl = publishObject.link;
        linkTitle.setText(publishObject.title);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view == linkTitle.getParent()) {
            MMWebViewActivity.createNavToParam(Uri.parse(linkUrl)).start(getContext());
        }
    }

}
