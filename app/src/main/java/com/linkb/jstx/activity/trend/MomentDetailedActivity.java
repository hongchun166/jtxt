
package com.linkb.jstx.activity.trend;


import android.content.Intent;
import android.support.v4.app.SharedElementCallback;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.linkb.jstx.component.DetailMomentView;
import com.linkb.jstx.model.Comment;
import com.linkb.jstx.activity.base.CIMMonitorActivity;
import com.linkb.jstx.adapter.CustomCommentListAdapter;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.component.SimpleInputPanelView;
import com.linkb.jstx.database.CommentRepository;
import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.database.MomentRepository;
import com.linkb.jstx.dialog.CustomDialog;
import com.linkb.jstx.listener.OnDialogButtonClickListener;
import com.linkb.jstx.listener.OnInputPanelEventListener;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.model.Moment;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BasePersonInfoResult;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.CommentResult;
import com.linkb.R;

import java.util.List;
import java.util.Map;

public class MomentDetailedActivity extends CIMMonitorActivity implements OnItemClickListener, OnInputPanelEventListener, OnDialogButtonClickListener, OnItemClickedListener,HttpRequestListener {

    private CustomCommentListAdapter adapter;
    private ListView listView;
    private Moment moment;
    private SimpleInputPanelView inputPanelView;
    private User self;
    private CustomDialog customDialog;

    private Comment target;
    private String type = Comment.TYPE_0;
    private String transitionName;

    @Override
    public void initComponents() {

        self = Global.getCurrentUser();
        moment = (Moment) this.getIntent().getSerializableExtra(Moment.class.getName());
        if (moment.getAllCount() == 0) {
            moment.setCommentList(CommentRepository.queryCommentList(moment.id));
        }


        adapter = new CustomCommentListAdapter(moment);
        listView = findViewById(R.id.listView);
        inputPanelView = findViewById(R.id.inputPanelView);
        inputPanelView.setOnInputPanelEventListener(this);

        DetailMomentView headerView = (DetailMomentView) getHeaderView();
        headerView.displayMoment(moment);
        headerView.setOnItemClickedListener(this);
        listView.addHeaderView(headerView);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        setExitSharedElementCallback( sharedElementCallback);

    }

    private View getHeaderView() {
        if (moment.type.equals(Moment.FORMAT_LINK)) {
            return LayoutInflater.from(this).inflate(R.layout.moment_details_link, null, false);
        }
        if (moment.type.equals(Moment.FORMAT_VIDEO)) {
            return LayoutInflater.from(this).inflate(R.layout.moment_details_video, null, false);
        }
        if (moment.type.equals(Moment.FORMAT_TEXT)) {
            return LayoutInflater.from(this).inflate(R.layout.moment_details_text, null, false);
        }

        if (moment.type.equals(Moment.FORMAT_IMAGE)) {
            return LayoutInflater.from(this).inflate(R.layout.moment_details_one_photo, null, false);
        }
        return LayoutInflater.from(this).inflate(R.layout.moment_details_multi_photo, null, false);
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {

        Comment comment = (Comment) arg1.getTag(R.id.target);
        if (comment == null || comment.account.equals(self.account)) {
            resetApiParams();
            return;
        }

        target = comment;
        type = Comment.TYPE_1;

        asynViewName(inputPanelView, comment.account);
//        String name = FriendRepository.queryFriendName(comment.account);
//        inputPanelView.setHint(getString(R.string.hint_comment, name));
//        inputPanelView.show();
    }

    private void asynViewName(final SimpleInputPanelView simpleInputPanelView, String account){
        HttpServiceManager.queryPersonInfo(account, new HttpRequestListener<BasePersonInfoResult>() {
            @Override
            public void onHttpRequestSucceed(BasePersonInfoResult result, OriginalCall call) {
                if (result.isSuccess()){
                    Friend friend = User.UserToFriend(result.getData());
                    FriendRepository.save(friend);
                    simpleInputPanelView.setHint(getString(R.string.hint_comment, friend.name));
                    simpleInputPanelView.show();
                }
            }

            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {

            }
        });
    }

    @Override
    public void onSendButtonClicked(String content) {

        performAddCommentRequest(content);
        inputPanelView.reset();
    }

    @Override
    public void onMessageInsertAt() {

    }

    @Override
    public void onMessageTextDelete(String txt) {

    }

    @Override
    public void onKeyboardStateChanged(boolean visable) {

    }

    @Override
    public void onPanelStateChanged(boolean switchToPanel) {

    }


    private void onHttpRequestSucceed(CommentResult result) {
        CommentRepository.add(result.data);
        moment.add(result.data);
        adapter.notifyDataSetChanged();
        listView.setSelection(listView.getBottom());
        resetApiParams();
    }


    @Override
    public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
        if (result instanceof CommentResult){
            onHttpRequestSucceed((CommentResult) result);
        }else {
            MomentRepository.deleteById(moment.id);
            Intent intent = new Intent(Constant.Action.ACTION_DELETE_MOMENT);
            intent.putExtra(Moment.class.getName(), moment);
            LvxinApplication.sendLocalBroadcast(intent);
            finish();
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
    }

    private void resetApiParams() {
        type = Comment.TYPE_0;
        target = null;
        inputPanelView.setContent(null);
        inputPanelView.setHint(null);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_moment_detailed;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.common_detail;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (self.account.equals(moment.account)) {
            getMenuInflater().inflate(R.menu.single_icon, menu);
            menu.findItem(R.id.menu_icon).setIcon(R.drawable.icon_menu_delete);

            customDialog = new CustomDialog(this);
            customDialog.setOnDialogButtonClickListener(this);
            customDialog.setIcon((R.drawable.icon_dialog_delete));
            customDialog.setMessage((R.string.tip_delete_article));
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_icon) {
            customDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRightButtonClicked() {

        customDialog.dismiss();
        HttpServiceManager.deleteMoment(moment.id,this);

    }




    private void performAddCommentRequest(String content) {

        Comment comment = new Comment();
        if (Comment.TYPE_1.equals(type)) {
            comment.sourceId = target.id ;
            comment.reply = target.account;
        }
        comment.content = content;
        comment.targetId = moment.id;
        comment.type = type;

        HttpServiceManager.publish(comment, moment.account,this);

    }


    @Override
    public void onLeftButtonClicked() {
        customDialog.dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Intent intent = new Intent(Constant.Action.ACTION_REFRESH_MOMENT);
        intent.putExtra(Moment.class.getName(), moment);
        LvxinApplication.sendLocalBroadcast(intent);
    }

    @Override
    public void onItemClicked(Object obj, View view) {
        if (view.getId() == R.id.bar_comment) {
            inputPanelView.show();
        }
    }



    SharedElementCallback sharedElementCallback = new SharedElementCallback() {
        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            if (transitionName != null) {
                sharedElements.put(transitionName,listView.findViewWithTag(transitionName));
            }
        }
    };

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        transitionName = data.getStringExtra("url");
    }
}
