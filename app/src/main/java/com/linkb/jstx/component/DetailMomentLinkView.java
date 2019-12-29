
package com.linkb.jstx.component;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.linkb.jstx.activity.chat.MMWebViewActivity;
import com.linkb.jstx.model.Moment;
import com.linkb.jstx.network.model.MomentLink;
import com.linkb.R;
import com.google.gson.Gson;

public class DetailMomentLinkView extends DetailMomentView {
    private TextView linkTitle;
    private String linkUrl;

    public DetailMomentLinkView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        linkTitle = findViewById(R.id.linkTitle);
        ((View) linkTitle.getParent()).setOnClickListener(this);
    }

    @Override
    public void displayMoment(Moment moment) {
        super.displayMoment(moment);
        final MomentLink link = new Gson().fromJson(moment.content, MomentLink.class);
        linkUrl = link.link;
        linkTitle.setText(link.title);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view == linkTitle.getParent()) {
            Intent intent = new Intent(getContext(), MMWebViewActivity.class);
            intent.setData(Uri.parse(linkUrl));
            getContext().startActivity(intent);
        }
    }

}
