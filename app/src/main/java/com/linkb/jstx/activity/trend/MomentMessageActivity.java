
package com.linkb.jstx.activity.trend;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.model.Comment;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.adapter.MomentMessageAdapter;
import com.linkb.jstx.database.MomentRepository;
import com.linkb.jstx.model.Moment;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.MomentResult;
import com.linkb.R;

public class MomentMessageActivity extends BaseActivity implements OnItemClickedListener, HttpRequestListener<MomentResult> {


    @Override
    public void initComponents() {

        MomentMessageAdapter adapter = new MomentMessageAdapter();
        RecyclerView messageListView = findViewById(R.id.recyclerView);
        messageListView.setLayoutManager(new LinearLayoutManager(this));
        messageListView.setAdapter(adapter);
        adapter.addAll(MessageRepository.queryMoments());

        adapter.setOnItemClickedListener(this);
        MessageRepository.batchReadMessage(new String[]{Constant.MessageAction.ACTION_501, Constant.MessageAction.ACTION_502});

        findViewById(R.id.emptyView).setVisibility(adapter.isEmpty() ? View.VISIBLE : View.GONE);

    }


    @Override
    public void onHttpRequestSucceed(MomentResult result, OriginalCall call) {
        hideProgressDialog();
        if (result.isSuccess() && (result.data == null)) {
            showToastView(R.string.tip_article_deleted);
            return;
        }

        //MomentRepository.add(result.data);

        Intent intent = new Intent(this, MomentDetailedActivity.class);
        intent.putExtra(Moment.class.getName(), result.data);
        startActivity(intent);
    }



    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        hideProgressDialog();
    }

    @Override
    public int getContentLayout() {

        return R.layout.activity_common_recyclerview;
    }

    @Override
    public int getToolbarTitle() {

        return R.string.label_moment_message;
    }


    private void performGetMomentRequest(long gid) {
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.common_handle)));
        HttpServiceManager.queryMoment(gid,this);
    }

    @Override
    public void onItemClicked(Object obj, View view) {
        Comment comment = (Comment) obj;
        Moment article = MomentRepository.queryById(comment.targetId);
        if (article != null) {
            Intent intent = new Intent(this, MomentDetailedActivity.class);
            intent.putExtra(Moment.class.getName(), MomentRepository.queryById(comment.targetId));
            startActivity(intent);
        } else {
            performGetMomentRequest(comment.targetId);
        }
    }
}
