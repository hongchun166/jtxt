
package com.linkb.jstx.component;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.contact.PersonInfoActivity;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.app.URLConstant;
import com.linkb.jstx.bean.ChatItem;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.database.CommentRepository;
import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.dialog.MomentRespondWindow;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.model.Comment;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.model.Moment;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BasePersonInfoResult;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.CommentResult;
import com.linkb.jstx.util.AppTools;
import com.linkb.jstx.util.BackgroundThreadHandler;
import com.linkb.jstx.util.FileURLBuilder;
import com.linkb.jstx.util.StringUtils;

import java.util.List;

public class DetailMomentView extends RelativeLayout implements OnClickListener, OnItemClickedListener, HttpRequestListener {
    Moment moment;
    private TextView text;
    private WebImageView icon;
    private TextView name;
    private TextView time;
    private User self;
    private View praiseView;
    private GridLayout gridLayout;
    private int cellWidth;
    private MomentRespondWindow respondWindow;
    private View commentButton;
    private OnItemClickedListener onItemClickedListener;
    private int spacing;

    public DetailMomentView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }


    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        self = Global.getCurrentUser();
        name = findViewById(R.id.name);
        text = findViewById(R.id.text);
        icon = findViewById(R.id.icon);
        time = findViewById(R.id.time);
        praiseView = findViewById(R.id.praise_header);
        gridLayout = findViewById(R.id.praiserGridLayout);
        commentButton = findViewById(R.id.commentButton);
        commentButton.setOnClickListener(this);
        respondWindow = new MomentRespondWindow(getContext());
        respondWindow.setOnItemClickedListener(this);

        spacing = getResources().getDimensionPixelOffset(R.dimen.sns_photos_spacing);
        cellWidth = ((Resources.getSystem().getDisplayMetrics().widthPixels - (AppTools.dip2px(113) + (gridLayout.getColumnCount() - 1) * spacing)) / gridLayout.getColumnCount());

    }

    public void displayMoment(Moment moment) {
        this.moment = moment;
        icon.load(FileURLBuilder.getUserIconUrl(moment.account), R.mipmap.lianxiren);
        name.getPaint().setFakeBoldText(true);
        if (moment.account.equals(self.account)) {
            name.setText(self.name);
        } else {
//            name.setText(FriendRepository.queryFriendName(moment.account));
            Friend.asynTextViewName(name, moment.account);
        }
        time.setText(AppTools.getRecentTimeString(moment.timestamp));

        text.setVisibility(StringUtils.isNotEmpty(moment.text) ? View.VISIBLE :View.GONE);
        text.setText(moment.text);

        listPraiseView();
    }


    private void listPraiseView() {
        List<Comment> praiseList = moment.getPraiseList();

        if (praiseList.isEmpty()) {
            praiseView.setVisibility(GONE);
            return;
        }

        for (int i = 0; i < praiseList.size(); i++) {
            WebImageView itemView = new WebImageView(getContext());
            gridLayout.addView(itemView, cellWidth, cellWidth);
            itemView.setTag(praiseList.get(i).account);
            itemView.setId(R.id.icon);
            itemView.setOnClickListener(this);
            itemView.load(FileURLBuilder.getUserIconUrl(praiseList.get(i).account), R.mipmap.lianxiren);

            boolean isRowFirst = i % gridLayout.getColumnCount() != 0;
            ((GridLayout.LayoutParams) itemView.getLayoutParams()).leftMargin = isRowFirst ? spacing : 0;

            boolean isFirstRow = i < gridLayout.getColumnCount();
            ((GridLayout.LayoutParams) itemView.getLayoutParams()).topMargin = isFirstRow ? 0 : spacing;
        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.icon) {
            Friend friend = FriendRepository.queryFriend(view.getTag().toString(), mListener);
            if (friend != null){
                postQueryFriend(friend);
            }
            return;
        }

        if (view == commentButton) {
            respondWindow.showAtLocation(commentButton, getHasPraise() != null);
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
        Intent intent = new Intent(getContext(), PersonInfoActivity.class);
        intent.putExtra(Friend.class.getName(), friend);
        getContext().startActivity(intent);
    }

    private Comment getHasPraise() {
        for (Comment comment : moment.getPraiseList()) {
            if (comment.account.equals(self.account)) {
                return comment;
            }
        }
        return null;
    }


    @Override
    public void onItemClicked(Object obj, final View view) {
        if (view.getId() == R.id.bar_comment) {
            BackgroundThreadHandler.postUIThreadDelayed(new Runnable() {
                @Override
                public void run() {
                    onItemClickedListener.onItemClicked(view, view);
                }
            }, 50);
        }
        if (view.getId() == R.id.bar_praise) {
            Comment praise = getHasPraise();
            if (praise != null) {

                HttpServiceManager.deleteComment(praise.id, moment.account,this);

            } else {
                HttpServiceManager.addPraise(moment.id, moment.account,this);
            }
        }
    }

    @Override
    public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
        if (call.equalsDelete(URLConstant.COMMENT_DELETE_URL) && result.isSuccess()) {
            Comment praise = getHasPraise();
            removePraise(praise);
            CommentRepository.delete(praise);
        }
        if (call.equals(URLConstant.COMMENT_PRAISE_URL) && result.isSuccess()) {
            addPraise(((CommentResult)result).data);
            CommentRepository.add(((CommentResult)result).data);
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {

    }


    private void addPraise(Comment data) {
        WebImageView icon = new WebImageView(getContext());
        icon.load(FileURLBuilder.getUserIconUrl(data.account), R.mipmap.lianxiren);
        icon.setOnClickListener(this);
        icon.setTag(data.account);
        gridLayout.addView(icon, cellWidth, cellWidth);
        moment.add(data);
        boolean isRowFirst = (gridLayout.getChildCount() - 1) % gridLayout.getColumnCount() != 0;
        ((GridLayout.LayoutParams) icon.getLayoutParams()).leftMargin = isRowFirst ? spacing : 0;

        boolean isFirstRow = (gridLayout.getChildCount() - 1) < gridLayout.getColumnCount();
        ((GridLayout.LayoutParams) icon.getLayoutParams()).topMargin = isFirstRow ? 0 : spacing;

        praiseView.setVisibility(VISIBLE);
    }


    private void removePraise(Comment data) {
        if (data == null) {
            return;
        }
        int index = moment.getPraiseList().indexOf(data);

        if (index >= 0) {
            moment.remove(data);
            gridLayout.removeViewAt(index);
        }
        if (moment.getPraiseList().isEmpty()) {
            praiseView.setVisibility(GONE);
        }
    }

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

}
