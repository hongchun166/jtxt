
package com.linkb.jstx.component;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Vibrator;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.contact.PersonInfoActivity;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.bean.ChatItem;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.database.CommentRepository;
import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.database.GroupRepository;
import com.linkb.jstx.dialog.CustomDialog;
import com.linkb.jstx.listener.OnDialogButtonClickListener;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.model.Comment;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.model.Moment;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BasePersonInfoResult;
import com.linkb.jstx.util.AnimationTools;
import com.linkb.jstx.util.FileURLBuilder;

import java.util.List;

public class CommentListView extends LinearLayout implements OnDialogButtonClickListener, OnLongClickListener, OnClickListener {
    private Moment moment;
    private OnItemClickedListener onCommentClickListener;
    private CustomDialog customDialog;
    private User self;
    private Comment removeComment;
    private int mLastTouchY;
    private GridLayout gridLayout;
    private LinearLayout commentPanel;
    private RelativeLayout praisePanel;
    private View divider;
    private int spacing;
    private int iconWidth;

    public CommentListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setGravity(Gravity.LEFT);
        customDialog = new CustomDialog(getContext());
        customDialog.setOnDialogButtonClickListener(this);
        customDialog.setIcon((R.drawable.icon_dialog_delete));
        customDialog.setMessage(getContext().getString(R.string.tip_delete_comment));
        customDialog.setButtonsText(getContext().getString(R.string.common_cancel), getContext().getString(R.string.common_delete));
        self = Global.getCurrentUser();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        commentPanel = findViewById(R.id.commentPanel);
        praisePanel = findViewById(R.id.praisePanel);
        gridLayout = praisePanel.findViewById(R.id.gridLayout);
        spacing = getResources().getDimensionPixelOffset(R.dimen.sns_photos_spacing);
        divider = praisePanel.findViewById(R.id.divider);

        float density = Resources.getSystem().getDisplayMetrics().density;
        int column = gridLayout.getColumnCount();
        iconWidth = (int) ((Resources.getSystem().getDisplayMetrics().widthPixels - (113 * density + (column - 1) * spacing)) / column);
    }

    public void setOnItemClickListener(OnItemClickedListener onCommentClickListener) {
        this.onCommentClickListener = onCommentClickListener;
    }


    public int getLastTouchY() {
        return mLastTouchY;
    }


    public void showCommentsAndPraises(Moment moment) {
        this.moment = moment;
        showPraiseView();
        showCommentView();
    }

    private void showPraiseView() {
        List<Comment> praiseList = moment.getPraiseList();
        praisePanel.setVisibility(praiseList.isEmpty() ? GONE : VISIBLE);
        gridLayout.removeAllViews();
        tooglePanelDivider();

        for (int i = 0; i < praiseList.size(); i++) {
            WebImageView itemView = new WebImageView(this.getContext());
            itemView.setId(R.id.icon);
            itemView.setTag(praiseList.get(i));
            itemView.setOnClickListener(this);
            itemView.load(FileURLBuilder.getUserIconUrl(praiseList.get(i).account), R.mipmap.lianxiren);
            gridLayout.addView(itemView, iconWidth, iconWidth);

            boolean isRowFirst = i % gridLayout.getColumnCount() != 0;
            ((GridLayout.LayoutParams) itemView.getLayoutParams()).leftMargin = isRowFirst ? spacing : 0;

            boolean isFirstRow = i < gridLayout.getColumnCount();
            ((GridLayout.LayoutParams) itemView.getLayoutParams()).topMargin = isFirstRow ? 0 : spacing;
        }

    }


    private void showCommentView() {
        List<Comment> commentList = moment.getTextList();
        commentPanel.removeAllViews();
        commentPanel.setVisibility(commentList.isEmpty() ? GONE : VISIBLE);

        for (int i = 0; i < commentList.size(); i++) {
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_circle_comment, null);
            commentPanel.addView(itemView);
            itemView.setOnClickListener(this);
            display(itemView, commentList.get(i));
        }
    }

    private void display(final View itemView, final Comment comment) {
        final EmoticonTextView commentText = (EmoticonTextView) itemView;
        HttpServiceManager.queryPersonInfo(comment.account, new HttpRequestListener<BasePersonInfoResult>() {
            @Override
            public void onHttpRequestSucceed(BasePersonInfoResult result, OriginalCall call) {
                if (result.isSuccess()){
                    Friend friend = User.UserToFriend(result.getData());
                    FriendRepository.save(friend);

                    String name = friend.name;

                    if (Comment.TYPE_1.equals(comment.type)) {
                        asynTextViewName(commentText, comment.reply, name, comment);

//                        String replyName = FriendRepository.queryFriendName(comment.reply);
//                        String string = getResources().getString(R.string.label_moment_replay_user, name, replyName, TextUtils.htmlEncode(comment.content));
//                        commentText.setText(Html.fromHtml(string));
                    } else {
                        String string = getResources().getString(R.string.label_moment_replay, name, TextUtils.htmlEncode(comment.content));
                        commentText.setText(Html.fromHtml(string));
                    }
                    itemView.setTag(comment);
                    itemView.setOnLongClickListener(CommentListView.this);
                }
            }

            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {

            }
        });

//        String name = FriendRepository.queryFriendName(comment.account);
//        if (Comment.TYPE_1.equals(comment.type)) {
//            String replyName = FriendRepository.queryFriendName(comment.reply);
//            String string = getResources().getString(R.string.label_moment_replay_user, name, replyName, TextUtils.htmlEncode(comment.content));
//            commentText.setText(Html.fromHtml(string));
//        } else {
//            String string = getResources().getString(R.string.label_moment_replay, name, TextUtils.htmlEncode(comment.content));
//            commentText.setText(Html.fromHtml(string));
//        }
//        itemView.setTag(comment);
//        itemView.setOnLongClickListener(this);
    }

    private void asynTextViewName(final TextView textView, String account, final  String name , final  Comment comment){
        HttpServiceManager.queryPersonInfo(account, new HttpRequestListener<BasePersonInfoResult>() {
            @Override
            public void onHttpRequestSucceed(BasePersonInfoResult result, OriginalCall call) {
                if (result.isSuccess()){
                    Friend friend = User.UserToFriend(result.getData());
                    FriendRepository.save(friend);
                    String string = getResources().getString(R.string.label_moment_replay_user, name, friend.name, TextUtils.htmlEncode(comment.content));
                    textView.setText(Html.fromHtml(string));
                }
            }

            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {

            }
        });
    }

    private void tooglePanelDivider() {
        if (moment.getTextCount() > 0 && moment.getPraiseCount() > 0) {
            divider.setVisibility(VISIBLE);
        }
        if (moment.getTextCount() == 0) {
            divider.setVisibility(GONE);
        }
    }

    @Override
    public void onLeftButtonClicked() {
        customDialog.dismiss();
    }


    @Override
    public void onRightButtonClicked() {

        customDialog.dismiss();

        HttpServiceManager.deleteComment(removeComment.id,moment.account,null);

        removeComment(removeComment);
    }




    @Override
    public boolean onLongClick(View view) {
        Comment comment = (Comment) view.getTag();
        if (comment.account.equals(self.account) && comment.id != 0) {
            ((Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE)).vibrate(20);
            removeComment = comment;
            customDialog.show();
        }
        return false;
    }


    @Override
    public void onClick(View view) {
        Comment comment = (Comment) view.getTag();
        if (view.getId() == R.id.icon) {
            Friend friend = FriendRepository.queryFriend(comment.account, mListener);
            if (friend != null){
                postQueryFriend(friend);
            }
            return;
        }

        int[] loc = new int[2];
        view.getLocationOnScreen(loc);
        mLastTouchY = loc[1] + view.getMeasuredHeight();
        if (!self.account.equals(comment.account)) {
            onCommentClickListener.onItemClicked(comment, this);
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

    public int getEndYOnScrenn() {
        int[] loc = new int[2];
        if (getChildCount() == 0 || getVisibility() == GONE) {
            View parent = (View) getParent();
            parent.getLocationOnScreen(loc);
            return loc[1] + parent.getMeasuredHeight();
        }

        getLocationOnScreen(loc);
        return loc[1] + getMeasuredHeight();
    }

    public void addPraise(Comment data) {
        WebImageView icon = new WebImageView(getContext());
        icon.load(FileURLBuilder.getUserIconUrl(data.account), R.mipmap.lianxiren);
        icon.setOnClickListener(this);
        icon.setTag(data);
        icon.setId(R.id.icon);
        gridLayout.addView(icon, iconWidth, iconWidth);

        boolean isRowFirst = (gridLayout.getChildCount() - 1) % gridLayout.getColumnCount() != 0;
        ((GridLayout.LayoutParams) icon.getLayoutParams()).leftMargin = isRowFirst ? spacing : 0;

        boolean isFirstRow = (gridLayout.getChildCount() - 1) < gridLayout.getColumnCount();
        ((GridLayout.LayoutParams) icon.getLayoutParams()).topMargin = isFirstRow ? 0 : spacing;

        praisePanel.setVisibility(VISIBLE);
        setVisibility(VISIBLE);
        tooglePanelDivider();
    }


    public void addComment(final Comment comment) {
        moment.add(comment);
        setVisibility(View.VISIBLE);
        commentPanel.setVisibility(View.VISIBLE);
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_circle_comment, null);
        commentPanel.addView(itemView);
        AnimationTools.start(itemView, R.anim.appear);
        display(itemView, comment);
        itemView.setOnClickListener(this);
        tooglePanelDivider();
    }

    private void removeComment(Comment comment) {
        moment.remove(comment);
        for (int i = 0; i < commentPanel.getChildCount(); i++) {
            View itemView = commentPanel.getChildAt(i);
            if (comment.equals(itemView.getTag())) {
                AnimationTools.start(itemView, R.anim.disappear);
                commentPanel.removeViewAt(i);
            }
        }

        CommentRepository.deleteById(comment.id);

        if (moment.getAllCount() == 0) {
            setVisibility(GONE);
        }

        tooglePanelDivider();
    }

    public void removePraise(Comment data) {

        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View itemView = gridLayout.getChildAt(i);
            if (data.equals(itemView.getTag())) {
                AnimationTools.start(itemView, R.anim.disappear);
                gridLayout.removeViewAt(i);
            }
        }
        praisePanel.setVisibility(moment.getPraiseCount() == 0 ? GONE : VISIBLE);

        if (moment.getAllCount() == 0) {
            setVisibility(GONE);
        }

    }
}
