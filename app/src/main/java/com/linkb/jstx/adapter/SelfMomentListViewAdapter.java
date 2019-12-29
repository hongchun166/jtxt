
package com.linkb.jstx.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.linkb.jstx.activity.chat.MMWebViewActivity;
import com.linkb.jstx.activity.trend.MomentDetailedActivity;
import com.linkb.jstx.adapter.viewholder.SelfMomentViewHolder;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.component.MomentImageView;
import com.linkb.jstx.model.Moment;
import com.linkb.jstx.network.model.SNSMomentImage;
import com.linkb.jstx.network.model.SNSVideo;
import com.linkb.jstx.util.AppTools;
import com.linkb.jstx.util.StringUtils;
import com.linkb.jstx.network.model.MomentLink;
import com.linkb.R;
import com.linkb.jstx.util.FileURLBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.List;

public class SelfMomentListViewAdapter extends MomentListViewAdapter {
    private final float density;
    private int cellWidth;
    private int spacing;

    public SelfMomentListViewAdapter(Context context) {
        super(context);
        spacing = context.getResources().getDimensionPixelOffset(R.dimen.sns_photos_spacing);
        density = Resources.getSystem().getDisplayMetrics().density;
        cellWidth = (int) ((Resources.getSystem().getDisplayMetrics().widthPixels - 110 * density) / 3);
    }

    @Override
    public RecyclerView.ViewHolder getMomentItemViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if (viewType == ACTION_ITEM_PHOTOS) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_moment_multi_photo, parent, false);
        }
        if (viewType == ACTION_ITEM_LINK) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_moment_link, parent, false);
        }
        if (viewType == ACTION_ITEM_ONEPHOTO) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_moment_one_photo, parent, false);
        }
        if (viewType == ACTION_ITEM_TEXT) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_moment_text, parent, false);
        }
        if (viewType == ACTION_ITEM_VIDEO) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_moment_video, parent, false);
        }

        return new SelfMomentViewHolder(itemView, viewType);
    }


    @Override
    public void onBindMomentViewHolder(RecyclerView.ViewHolder holder, final Moment target) {
        final SelfMomentViewHolder viewHolder = (SelfMomentViewHolder) holder;
        viewHolder.text.setVisibility(StringUtils.isNotEmpty(target.text) ? View.VISIBLE : View.GONE);
        viewHolder.text.setText(target.text);

        viewHolder.month.setText(LvxinApplication.getInstance().getString(R.string.label_user_moment_month, AppTools.getMonth(target.timestamp)));
        viewHolder.day.setText(AppTools.getDay(target.timestamp));


        if (viewHolder.viewType == ACTION_ITEM_PHOTOS) {
            List<SNSMomentImage> snsImageList = new Gson().fromJson(target.content, new TypeToken<List<SNSMomentImage>>() {}.getType());

            viewHolder.gridImageLayout.removeAllViews();

            for (int i = 0; i < snsImageList.size(); i++) {
                MomentImageView itemView = new MomentImageView(holder.itemView.getContext());
                itemView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                viewHolder.gridImageLayout.addView(itemView, cellWidth, cellWidth);
                itemView.display(target, snsImageList.get(i));

                boolean isRowFirst = i % viewHolder.gridImageLayout.getColumnCount() != 0;
                ((GridLayout.LayoutParams) viewHolder.gridImageLayout.getChildAt(i).getLayoutParams()).leftMargin = isRowFirst ? spacing : 0;

                boolean isFirstRow = i < viewHolder.gridImageLayout.getColumnCount();
                ((GridLayout.LayoutParams) viewHolder.gridImageLayout.getChildAt(i).getLayoutParams()).topMargin = isFirstRow ? 0 : spacing;
            }
        }

        if (viewHolder.viewType == ACTION_ITEM_LINK) {
            final MomentLink link = new Gson().fromJson(target.content, MomentLink.class);
            viewHolder.linkPanel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), MMWebViewActivity.class);
                    intent.setData(Uri.parse(link.link));
                    view.getContext().startActivity(intent);
                }
            });
            viewHolder.linkTitle.setText(link.title);
        }

        if (viewHolder.viewType == ACTION_ITEM_ONEPHOTO) {
            SNSMomentImage image = new Gson().fromJson(target.content, SNSMomentImage.class);
            viewHolder.singleImageView.displayFitSize(target, image);
        }
        if (viewHolder.viewType == ACTION_ITEM_VIDEO) {

            if (viewHolder.thumbnailView.getHeight() == 0) {
                int width = (int) (Resources.getSystem().getDisplayMetrics().widthPixels - 100 * density);
                viewHolder.thumbnailView.getLayoutParams().height = width * 2 / 3;
                viewHolder.thumbnailView.getLayoutParams().width = width;
                viewHolder.thumbnailView.requestLayout();
            }

            SNSVideo video = new Gson().fromJson(target.content, SNSVideo.class);
            File thumbnailFile = new File(LvxinApplication.CACHE_DIR_VIDEO, video.image);
            if (thumbnailFile.exists()) {
                viewHolder.thumbnailView.load(thumbnailFile, R.color.video_background);
            } else {
                String url = FileURLBuilder.getMomentFileUrl(video.image);
                viewHolder.thumbnailView.load(url, R.color.video_background);
            }

        }
        viewHolder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MomentDetailedActivity.class);
                intent.putExtra(Moment.class.getName(), target);
                view.getContext().startActivity(intent);
            }
        });
    }
}
