
package com.linkb.jstx.component;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linkb.jstx.activity.chat.MMWebViewActivity;
import com.linkb.jstx.activity.contact.MessageForwardActivity;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.network.model.MomentLink;
import com.linkb.jstx.util.MessageUtil;
import com.linkb.R;
import com.google.gson.Gson;

public class MicroServerFromLinkView extends BaseMicroServerFromView implements OnClickListener {
    private MomentLink linkMsg;
    private TextView title;
    private TextView descrpiton;
    private WebImageView banner;
    private ViewGroup linkPanelView;

    public MicroServerFromLinkView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }


    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        title = findViewById(R.id.title);
        descrpiton = findViewById(R.id.descrpiton);
        banner = findViewById(R.id.banner);
        linkPanelView =  findViewById(R.id.linkPanelView);

    }

    @Override
    public void displayMessage() {
        linkPanelView.setOnLongClickListener(this);
        linkPanelView.setOnTouchListener(this);
        linkMsg = new Gson().fromJson(super.message.content, MomentLink.class);
        title.setText(linkMsg.title);
        descrpiton.setText(linkMsg.content);
        banner.setImageResource(R.color.theme_window_color);
        banner.load(linkMsg.image);
        findViewById(R.id.linkPanelView).setOnClickListener(this);
    }


    @Override
    public void onMenuItemClicked(int id) {

        super.onMenuItemClicked(id);

        if (id == R.id.menu_forward) {
            Message target = MessageUtil.clone(message);
            target.content = linkMsg.toString();
            target.format = Constant.MessageFormat.FORMAT_TEXT;
            Intent intent = new Intent(getContext(), MessageForwardActivity.class);
            intent.putExtra(Message.NAME, target);
            getContext().startActivity(intent);
        }
    }

    @Override
    public Object getTag() {
        return findViewById(R.id.linkPanelView).getTag(R.id.logo);
    }


    @Override
    public void setTag(Object obj) {
        findViewById(R.id.linkPanelView).setTag(R.id.logo, obj);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), MMWebViewActivity.class);
        intent.setData(Uri.parse(linkMsg.link));
        getContext().startActivity(intent);
    }

}
