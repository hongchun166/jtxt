
package com.linkb.jstx.component;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linkb.jstx.activity.chat.MMWebViewActivity;
import com.linkb.jstx.network.model.MicroServerLinkMessage;
import com.linkb.R;
import com.google.gson.Gson;

public class MicroServerFromLinkPanelView extends BaseMicroServerFromView implements OnClickListener {
    private LinearLayout linkPanelView;

    public MicroServerFromLinkPanelView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }


    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        linkPanelView = findViewById(R.id.linksPanelView);
    }

    @Override
    public void displayMessage() {

        linkPanelView.setOnTouchListener(this);
        linkPanelView.setOnLongClickListener(this);
        MicroServerLinkMessage linkMsg = new Gson().fromJson(super.message.content, MicroServerLinkMessage.class);
        linkPanelView.removeAllViews();
        View topLine = LayoutInflater.from(getContext()).inflate(R.layout.layout_microserver_cell_from_banner, null);
        ((TextView) topLine.findViewById(R.id.title)).setText(linkMsg.title);
        WebImageView image = topLine.findViewById(R.id.banner);
        image.setImageResource(R.color.theme_window_color);
        image.load(linkMsg.image);
        topLine.setTag(linkMsg.link);
        topLine.setOnClickListener(this);
        linkPanelView.addView(topLine);
        if (linkMsg.hasMore()) {
            topLine.setBackgroundResource(R.drawable.item_background_top);
            for (int i = 0; i < linkMsg.items.size(); i++) {
                View item = LayoutInflater.from(getContext()).inflate(R.layout.layout_microserver_cell_from_link, null);
                ((TextView) item.findViewById(R.id.title)).setText(linkMsg.getSubLink(i).title);
                WebImageView iconImage = item.findViewById(R.id.image);
                iconImage.setImageResource(R.color.theme_window_color);
                iconImage.load(linkMsg.getSubLink(i).image);
                linkPanelView.addView(item);
                item.setTag(linkMsg.getSubLink(i).link);
                item.setOnClickListener(this);
                if (i == linkMsg.items.size()) {
                    item.setBackgroundResource(R.drawable.item_background_bottom);
                } else {
                    item.setBackgroundResource(R.drawable.item_background_middle);
                }
            }

        } else {
            topLine.setBackgroundResource(R.drawable.item_background_single);
        }
    }


    @Override
    public Object getTag() {
        return linkPanelView.getTag(R.id.logo);
    }


    @Override
    public void setTag(Object obj) {
        linkPanelView.setTag(R.id.logo, obj);
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), MMWebViewActivity.class);
        intent.setData(Uri.parse(v.getTag().toString()));
        getContext().startActivity(intent);
    }

}
