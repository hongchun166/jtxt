
package com.linkb.jstx.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.linkb.jstx.activity.trend.FriendMomentActivity;
import com.linkb.jstx.activity.trend.MineMomentActivity;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.component.TimelineMomentHeaderView;
import com.linkb.jstx.component.TimelineMomentView;
import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.listener.OnCommentSelectedListener;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.component.ListFooterView;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.model.Moment;
import com.linkb.R;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BasePersonInfoResult;

import java.util.ArrayList;
import java.util.List;

public class MomentListViewAdapter extends RecyclerView.Adapter implements OnClickListener {
    protected int ACTION_HEADER = 0;
    protected int ACTION_ITEM_PHOTOS = 1;
    protected int ACTION_FOOTER = 9999;
    protected int ACTION_ITEM_LINK = 2;
    protected int ACTION_ITEM_TEXT = 3;
    protected int ACTION_ITEM_ONEPHOTO = 4;
    protected int ACTION_ITEM_VIDEO = 5;

    private List<Moment> list = new ArrayList<>();
    private OnCommentSelectedListener commentSelectedListener;
    private User self;
    private TimelineMomentHeaderView mHeaderView;
    private ListFooterView mFooterView;
    private Context mContext;

    public MomentListViewAdapter(Context context) {
        super();
        mContext = context;
        self = Global.getCurrentUser();
        mHeaderView = (TimelineMomentHeaderView) LayoutInflater.from(context).inflate(R.layout.layout_circle_listheader, null);
        mFooterView = (ListFooterView) LayoutInflater.from(context).inflate(R.layout.layout_list_footer, null);
    }

    private Moment getItem(int position) {
        return list.get(position);
    }

    public void setOnCommentSelectedListener(OnCommentSelectedListener commentClickListener) {
        this.commentSelectedListener = commentClickListener;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return list.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ACTION_HEADER;
        }

        if (position == getItemCount() - 1) {
            return ACTION_FOOTER;
        }

        Moment moment = getItem(position - 1);
        if (moment.type.equals(Moment.FORMAT_LINK)) {
            return ACTION_ITEM_LINK;
        }
        if (moment.type.equals(Moment.FORMAT_VIDEO)) {
            return ACTION_ITEM_VIDEO;
        }
        if (moment.type.equals(Moment.FORMAT_TEXT)) {
            return ACTION_ITEM_TEXT;
        }

        if (moment.type.equals(Moment.FORMAT_IMAGE)) {
            return ACTION_ITEM_ONEPHOTO;
        }
        return ACTION_ITEM_PHOTOS;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == ACTION_HEADER) {
            return new HeaderViewHolder(mHeaderView);
        }

        if (viewType == ACTION_FOOTER) {
            return new FooterViewHolder(mFooterView);
        }

        return getMomentItemViewHolder(parent, viewType);
    }

    RecyclerView.ViewHolder getMomentItemViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if (viewType == ACTION_ITEM_PHOTOS) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline_moment_multi_photo, parent, false);
        }
        if (viewType == ACTION_ITEM_LINK) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline_moment_link, parent, false);
        }
        if (viewType == ACTION_ITEM_ONEPHOTO) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline_moment_one_photo, parent, false);
        }
        if (viewType == ACTION_ITEM_TEXT) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline_moment_text, parent, false);
        }
        if (viewType == ACTION_ITEM_VIDEO) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline_moment_video, parent, false);
        }
        return new MomentViewHolder(itemView);
    }


    void onBindMomentViewHolder(RecyclerView.ViewHolder holder, final Moment target) {
        final MomentViewHolder viewHolder = (MomentViewHolder) holder;
        viewHolder.itemMomentView.displayMoment(target, self, commentSelectedListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position > 0 && position < getItemCount() - 1) {
            final int index = position - 1;
            final Moment target = getItem(index);
            onBindMomentViewHolder(holder, target);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.icon) {
            Intent intent = new Intent();
            if (self.account.equals(v.getTag(R.id.account).toString())) {
                intent.setClass(v.getContext(), MineMomentActivity.class);
                v.getContext().startActivity(intent);
            } else {
                intent.setClass(v.getContext(), FriendMomentActivity.class);
                Friend friend = FriendRepository.queryFriend(v.getTag(R.id.account).toString(), mListener);
                if (friend != null){
                    intent.putExtra(Friend.class.getName(), friend);
                    v.getContext().startActivity(intent);
                }
            }
        }
    }

    private HttpRequestListener<BasePersonInfoResult> mListener = new HttpRequestListener<BasePersonInfoResult>() {
        @Override
        public void onHttpRequestSucceed(BasePersonInfoResult result, OriginalCall call) {
            if (result.isSuccess()){
                Friend friend = User.UserToFriend(result.getData());
                FriendRepository.save(friend);
                postQueryFriend(friend);
            }
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {

        }
    };

    private void postQueryFriend(Friend friend){
        Intent intent = new Intent();
        intent.setClass(mContext, FriendMomentActivity.class);
        intent.putExtra(Friend.class.getName(), friend);
        mContext.startActivity(intent);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public boolean listEquals(List<Moment> list) {
        if (this.list.size() < list.size()) {
            return false;
        }

        return this.list.subList(0, list.size()).equals(list);
    }

    public void replaceFirstPage(List<Moment> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void addAll(List<Moment> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        int lastIndex = this.list.size();
        if (this.list.addAll(list)) {
            notifyItemRangeInserted(lastIndex + 1, list.size());
        }
    }

    public void add(Moment article) {
        list.add(0, article);
        notifyItemInserted(1);
    }

    public void remove(Moment article) {
        int index = indexOf(article.id);
        if (index >= 0) {
            list.remove(index);
            notifyItemRemoved(index + 1);
        }
    }

    public void update(Moment article) {
        int index = indexOf(article.id);
        if (index >= 0) {
            list.set(index, article);
            notifyItemChanged(index + 1);
        }
    }

    private int indexOf(long gid) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).id == (gid)) {
                return i;
            }
        }
        return -1;
    }

    public TimelineMomentHeaderView getHeaderView() {

        return mHeaderView;
    }

    public ListFooterView getFooterView() {

        return mFooterView;
    }

    class MomentViewHolder extends RecyclerView.ViewHolder {
        TimelineMomentView itemMomentView;

        MomentViewHolder(View itemView) {
            super(itemView);
            itemMomentView = (TimelineMomentView) itemView;
        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {
        FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
