
package com.linkb.jstx.dialog;


import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.component.EmoticonTextView;
import com.linkb.jstx.component.HorizontalPaddingDecoration;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.listener.OnDialogButtonClickListener;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.model.MessageSource;
import com.linkb.jstx.network.model.ChatMap;
import com.linkb.jstx.network.model.ChatVoice;
import com.linkb.jstx.network.model.SNSChatImage;
import com.linkb.jstx.network.model.SNSVideo;
import com.linkb.jstx.network.model.ChatFile;
import com.linkb.R;
import com.linkb.jstx.util.FileURLBuilder;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SharedMessageDialog extends AppCompatDialog implements View.OnClickListener {
    private List<MessageSource> receiverList = new ArrayList<>();
    private EmoticonTextView text;
    private TextView multipleText;
    private TextView name;
    private WebImageView icon;
    private WebImageView image;
    private RecyclerView recyclerView;
    private OnDialogButtonClickListener onDialogButtonClickListener;
    private CardView videoView;
    private WebImageView videoThumbnail;

    public SharedMessageDialog(Context context) {

        super(context,R.style.CommonDialogStyle);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_shared_message);
        text = findViewById(R.id.text);
        text.setFaceSize(20);
        name = findViewById(R.id.name);
        videoView = findViewById(R.id.videoView);

        multipleText = findViewById(R.id.multipleText);
        icon = findViewById(R.id.icon);
        image = findViewById(R.id.image);
        videoThumbnail = findViewById(R.id.thumbnail);

        findViewById(R.id.leftButton).setOnClickListener(this);
        findViewById(R.id.rightButton).setOnClickListener(this);
        recyclerView = findViewById(R.id.multi);
        int padding = (int) Resources.getSystem().getDisplayMetrics().density * 8;

        recyclerView.addItemDecoration(new HorizontalPaddingDecoration(padding));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new ReceiverAdapter());
    }

    public void setOnDialogButtonClickListener(OnDialogButtonClickListener onDialogButtonClickListener) {
        this.onDialogButtonClickListener = onDialogButtonClickListener;
    }


    public void setMessage(Message message) {

        if (message.format.equals(Constant.MessageFormat.FORMAT_TEXT)) {
            text.setVisibility(View.VISIBLE);
            text.setText(message.content);
        }
        if (message.format.equals(Constant.MessageFormat.FORMAT_VOICE)) {
            multipleText.setVisibility(View.VISIBLE);
            ChatVoice chatVoice = new Gson().fromJson(message.content, ChatVoice.class);
            multipleText.setText(getContext().getString(R.string.title_shared_voice_message, chatVoice.length + "\""));
        }
        if (message.format.equals(Constant.MessageFormat.FORMAT_FILE)) {
            multipleText.setVisibility(View.VISIBLE);
            ChatFile chatFile = new Gson().fromJson(message.content, ChatFile.class);
            multipleText.setText(getContext().getString(R.string.title_shared_file_message,chatFile.name));
        }
        if (message.format.equals(Constant.MessageFormat.FORMAT_IMAGE)) {
            image.setVisibility(View.VISIBLE);
            SNSChatImage chatImage = new Gson().fromJson(message.content, SNSChatImage.class);
            Uri uri = Uri.parse(chatImage.image);
            image.load(uri.isRelative() ? FileURLBuilder.getChatFileUrl(chatImage.image) : uri.toString(), 0);
        }
        if (message.format.equals(Constant.MessageFormat.FORMAT_VIDEO)) {
            videoView.setVisibility(View.VISIBLE);
            SNSVideo video = new Gson().fromJson(message.content, SNSVideo.class);
            initVidwoView(video);
        }
        if (message.format.equals(Constant.MessageFormat.FORMAT_MAP)) {
            image.setVisibility(View.VISIBLE);
            ChatMap chatMap = new Gson().fromJson(message.content, ChatMap.class);
            image.load(FileURLBuilder.getChatFileUrl(chatMap.image));
        }
    }

    private void initVidwoView(SNSVideo video) {

        if (video.mode == SNSVideo.HORIZONTAL) {
            videoThumbnail.getLayoutParams().width = getContext().getResources().getDimensionPixelOffset(R.dimen.preview_video_height);
            videoThumbnail.getLayoutParams().height = getContext().getResources().getDimensionPixelOffset(R.dimen.preview_video_width);
        } else {
            videoThumbnail.getLayoutParams().width = getContext().getResources().getDimensionPixelOffset(R.dimen.preview_video_width);
            videoThumbnail.getLayoutParams().height = getContext().getResources().getDimensionPixelOffset(R.dimen.preview_video_height);
        }
        videoThumbnail.requestLayout();

        videoThumbnail.load(FileURLBuilder.getChatFileUrl(video.image), R.drawable.def_chat_video_background);
    }


    public void show(MessageSource source) {
        recyclerView.setVisibility(View.GONE);
        findViewById(R.id.single).setVisibility(View.VISIBLE);
        name.setText(source.getName());
        icon.load(source.getWebIcon(), R.mipmap.lianxiren);
        super.show();
    }

    public void show(List<MessageSource> selectedList) {
        if (selectedList.size() == 1) {
            show(selectedList.get(0));
            return;
        }
        recyclerView.setVisibility(View.VISIBLE);
        findViewById(R.id.single).setVisibility(View.GONE);
        receiverList.clear();
        receiverList.addAll(selectedList);
        recyclerView.getAdapter().notifyDataSetChanged();
        super.show();
    }

    @Override
    public void onClick(View view) {
        if (R.id.leftButton == view.getId()) {
            dismiss();
        }
        if (R.id.rightButton == view.getId()) {
            onDialogButtonClickListener.onRightButtonClicked();
        }
    }


    private class ReceiverAdapter extends RecyclerView.Adapter<IconHolder> {

        @Override
        public IconHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new IconHolder(View.inflate(getContext(), R.layout.item_icon_name, null));
        }

        @Override
        public void onBindViewHolder(IconHolder holder, int position) {
            MessageSource source = receiverList.get(position);
            holder.icon.load(source.getWebIcon(), source.getDefaultIconRID());
            holder.name.setText(source.getName());
        }

        @Override
        public int getItemCount() {
            return receiverList.size();
        }
    }


    private class IconHolder extends RecyclerView.ViewHolder {
        WebImageView icon;
        TextView name;

        IconHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.name);
        }
    }
}
