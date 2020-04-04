
package com.linkb.jstx.component;


import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linkb.jstx.activity.trend.MineMomentActivity;
import com.linkb.jstx.activity.trend.MomentDetailedActivity;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.database.MomentRepository;
import com.linkb.jstx.dialog.CustomDialog;
import com.linkb.jstx.listener.OnCommentSelectedListener;
import com.linkb.jstx.listener.OnDialogButtonClickListener;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.model.Comment;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.HttpServiceManagerV2;
import com.linkb.jstx.network.model.MomentExtra;
import com.linkb.jstx.network.result.BasePersonInfoResult;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.CommentResult;
import com.linkb.jstx.util.AppTools;
import com.linkb.jstx.util.StringUtils;
import com.linkb.jstx.activity.chat.MapViewActivity;
import com.linkb.jstx.activity.trend.FriendMomentActivity;
import com.linkb.jstx.app.URLConstant;
import com.linkb.jstx.database.CommentRepository;
import com.linkb.jstx.dialog.MomentRespondWindow;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.model.Moment;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.model.MapAddress;
import com.linkb.R;
import com.linkb.jstx.util.BackgroundThreadHandler;
import com.linkb.jstx.util.FileURLBuilder;
import com.google.gson.Gson;

import java.io.Serializable;

public class TimelineMomentView extends RelativeLayout implements OnClickListener, OnItemClickedListener, OnDialogButtonClickListener, HttpRequestListener {
    private Moment moment;
    private TextView text;
    private WebImageView icon;
    private TextView name;
    private TextView time;
    private CommentListView commentListView;
    private User self;
    private OnCommentSelectedListener commentSelectedListener;
    private MomentRespondWindow respondWindow;
    private View commentButton;
    private TextView location;
    private TextView delete;
    private CustomDialog customDialog;

    public TimelineMomentView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        delete = findViewById(R.id.delete);
        name = findViewById(R.id.name);
        text = findViewById(R.id.text);
        icon = findViewById(R.id.icon);
        time = findViewById(R.id.time);
        location = findViewById(R.id.location);
        commentListView = findViewById(R.id.commentListView);
        icon.setOnClickListener(this);
        commentButton = findViewById(R.id.commentButton);
        commentButton.setOnClickListener(this);
        respondWindow = new MomentRespondWindow(getContext());
        respondWindow.setOnItemClickedListener(this);
    }

    public void displayMoment(Moment moment, User self, OnCommentSelectedListener commentSelectedListener) {
        this.moment = moment;
        this.self = self;
        this.commentSelectedListener = commentSelectedListener;

        this.setTag(moment.id);

        icon.load(FileURLBuilder.getUserIconUrl(moment.account), R.mipmap.lianxiren);
        name.getPaint().setFakeBoldText(true);
        if (moment.account.equals(self.account)) {
            name.setText(self.name);
        } else {
            Friend.asynTextViewName(name, moment.account);
        }
        time.setText(AppTools.getRecentTimeString(moment.timestamp));


        MomentExtra object = new Gson().fromJson(moment.extra, MomentExtra.class);
        text.setText(moment.text);
        text.setVisibility(StringUtils.isNotEmpty(moment.text) ? View.VISIBLE : GONE);
        if (object.location != null) {
            location.setVisibility(VISIBLE);
            location.setText(object.location.name);
            location.setTag(object.location);
            location.setOnClickListener(this);
        } else {
            location.setVisibility(GONE);
        }

        delete.setVisibility(self.account.equals(moment.account) ? VISIBLE : GONE);
        delete.setOnClickListener(this);
        commentListView.setVisibility(moment.getAllCount() > 0 ? VISIBLE : GONE);
        commentListView.showCommentsAndPraises(moment);
        commentListView.setTag(R.id.target, moment);
        commentListView.setOnItemClickListener(this);
    }



    public void addPraise(Comment data) {
        commentListView.addPraise(data);
    }


    public void addComment(Comment data) {
        commentListView.addComment(data);
    }


    @Override
    public void onClick(View view) {
        if (view == icon) {
            Intent intent = new Intent();
            if (self.account.equals(moment.account)) {
                intent.setClass(getContext(), MineMomentActivity.class);
                getContext().startActivity(intent);
            } else {
                intent.setClass(getContext(), FriendMomentActivity.class);
                Friend friend = FriendRepository.queryFriend(moment.account, mListener);
                if (friend != null){
                    intent.putExtra(Friend.class.getName(), friend);
                    getContext().startActivity(intent);
                }
            }
            return;
        }
        if (view == commentButton) {
            respondWindow.showAtLocation(commentButton, getHasPraise() != null);
            return;
        }

        if (view == this) {
            Intent intent = new Intent(getContext(), MomentDetailedActivity.class);
            intent.putExtra(Moment.class.getName(), moment);
            getContext().startActivity(intent);
        }
        if (view == location) {
            Intent intent = new Intent(getContext(), MapViewActivity.class);
            intent.putExtra(MapAddress.class.getName(), (Serializable) location.getTag());
            getContext().startActivity(intent);
        }

        if (view == delete) {
            customDialog = new CustomDialog(getContext());
            customDialog.setOnDialogButtonClickListener(this);
            customDialog.setIcon((R.drawable.icon_dialog_delete));
            customDialog.setMessage((R.string.tip_delete_article));
            customDialog.show();
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
        intent.setClass(getContext(), FriendMomentActivity.class);
        if (friend != null){
            intent.putExtra(Friend.class.getName(), friend);
            getContext().startActivity(intent);
        }
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
    public void onItemClicked(Object obj, View view) {
        if (obj instanceof Comment) {
            Comment comment = (Comment) obj;
            CommentListView commentListView = (CommentListView) view;

            Comment newComment = new Comment();
            newComment.reply = comment.account;
            newComment.type = Comment.TYPE_1;
            newComment.targetId = moment.id;
            newComment.sourceId = comment.id;

            commentSelectedListener.onCommentSelected(commentListView, moment, newComment);
        }
        if (view.getId() == R.id.bar_comment) {

            final Comment newComment = new Comment();
            newComment.type = Comment.TYPE_0;
            newComment.targetId = moment.id;
            BackgroundThreadHandler.postUIThreadDelayed(new Runnable() {
                @Override
                public void run() {
                    commentSelectedListener.onCommentSelected(commentListView, moment,newComment);
                }
            }, 50);
        }
        if (view.getId() == R.id.bar_praise) {
            Comment praise = getHasPraise();
            if (praise != null) {
                performCancelPraiseRequest(praise.id);
            } else {
                performPraiseRequest();
            }
        }
    }

    private void performPraiseRequest() {

        respondWindow.disenablePariseMenu();
        HttpServiceManager.addPraise(moment.id, moment.account,this);
    }


    private void performCancelPraiseRequest(long gid) {

        respondWindow.disenablePariseMenu();
        HttpServiceManager.deleteComment(gid, moment.account,this);

    }

    @Override
    public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {

        if (call.equals(URLConstant.COMMENT_PRAISE_URL) && result.isSuccess()) {
            CommentResult data = (CommentResult) result;
            respondWindow.enablePariseMenu();
            moment.add(data.data);
            commentListView.addPraise(data.data);
            CommentRepository.add(data.data);
        }

        if (call.equalsDelete(URLConstant.COMMENT_DELETE_URL) && result.isSuccess() ) {

            respondWindow.enablePariseMenu();
            Comment praise = getHasPraise();
            moment.remove(praise);
            commentListView.removePraise(praise);
            CommentRepository.delete(praise);
        }

        if (call.equalsDelete(URLConstant.MOMENT_OPERATION_URL) && result.isSuccess()) {

            MomentRepository.deleteById(moment.id);
            Intent intent = new Intent(Constant.Action.ACTION_DELETE_MOMENT);
            intent.putExtra(Moment.class.getName(), moment);
            LvxinApplication.sendLocalBroadcast(intent);
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        respondWindow.enablePariseMenu();
    }

    @Override
    public void onLeftButtonClicked() {
        customDialog.dismiss();
    }

    @Override
    public void onRightButtonClicked() {
        customDialog.dismiss();
        HttpServiceManager.deleteMoment(moment.id,this);
    }




}
