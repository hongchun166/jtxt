package com.linkb.jstx.activity.contact;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.activity.trend.FriendMomentActivity;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.HttpServiceManagerV2;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BasePersonInfoResult;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.v2.QueryUserInfoResult;
import com.linkb.jstx.util.FileURLBuilder;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class UserDetailActivityV2 extends BaseActivity {
    @BindView(R.id.viewContentGroup)
    LinearLayout viewContentGroup;

    @BindView(R.id.viewIVHead)
    WebImageView viewIVHead;
    @BindView(R.id.viewTVName)
    TextView viewTVName;
    @BindView(R.id.viewIVSex)
    ImageView viewIVSex;

    @BindView(R.id.viewTVCompany)
    TextView viewTVCompany;
    @BindView(R.id.viewTVProfession)
    TextView viewTVProfession;
    @BindView(R.id.viewTVAddress)
    TextView viewTVAddress;
    @BindView(R.id.viewTVSign)
    TextView viewTVSign;
    @BindView(R.id.viewTVLableTitle)
    TextView viewTVLableTitle;

    @BindView(R.id.viewBtnLookDT)
    Button viewBtnLookDT;

    @BindView(R.id.viewTagFlowLayout)
    TagFlowLayout viewTagFlowLayout;

    Unbinder unbinder;
    Friend friend;

    public static void navToAct(Context context,Friend friend){
        if(context==null){
            return;
        }
        Intent intent=new Intent(context,UserDetailActivityV2.class);
        intent.putExtra("friend",friend);
        context.startActivity(intent);
    }
    @Override
    protected int getContentLayout() {
        return R.layout.activity_user_detailed_v2;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(unbinder!=null){
            unbinder.unbind();
            unbinder=null;
        }
    }

    @Override
    protected void initComponents() {
        unbinder=ButterKnife.bind(this);

        Bundle bundle=getIntent().getExtras();
        friend= (Friend) bundle.getSerializable("friend");
        String friendAccount=friend.account;
        User user=Global.getCurrentUser();

        viewIVHead.load(FileURLBuilder.getUserIconUrl(friendAccount), R.mipmap.lianxiren, 999);

        HttpServiceManagerV2.queryUserInfo(user.account, friendAccount, new HttpRequestListener<QueryUserInfoResult>(){
            @Override
            public void onHttpRequestSucceed(QueryUserInfoResult result, OriginalCall call) {
                if(result.isSuccess() && result.getData()!=null && result.getData().size()>0){
                    updateUi(result.getData().get(0));
                }

            }
            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {

            }
        });
    }


    @OnClick(R.id.viewBack)
    public void onBack(){
        finish();
    }
    @OnClick(R.id.viewBtnLookDT)
    public void onLookMomentRule(){
        Intent uintent = new Intent(this, FriendMomentActivity.class);
        uintent.putExtra(Friend.class.getName(), friend);
        startActivity(uintent);
    }
    private Context getContext(){
        return this;
    }
    private void updateUi(QueryUserInfoResult.DataBean dataBean){
        viewTVName.setText(dataBean.getName());
        viewTVSign.setText(TextUtils.isEmpty(dataBean.getMotto())?"":dataBean.getMotto());
        int sexResId=dataBean.getGender().equals("1")?R.mipmap.ic_sex_man:R.mipmap.ic_sex_woman;
        viewIVSex.setImageResource(sexResId);
//        viewSexTx.setText(dataBean.getGender().equals("1")?"男":"女");
//        viewIVSex.setVisibility(View.GONE);

        viewTVCompany.setText(TextUtils.isEmpty(dataBean.getMarrriage())?"":dataBean.getMarrriage());//
        viewTVCompany.setVisibility(View.GONE);
        viewTVProfession.setText(TextUtils.isEmpty(dataBean.getIndustry())?"":dataBean.getIndustry());//互联网IT Web前端
        viewTVAddress.setText(TextUtils.isEmpty(dataBean.getArea())?"":dataBean.getArea());//中国-湖南省-长沙
        if(!TextUtils.isEmpty(dataBean.getTag())){
            List<String> signList=new ArrayList<>();
            signList.add(dataBean.getTag());
//        signList.add("金融");
//        signList.add("区块链");
//        signList.add("直销");
//        signList.add("PE");
//        signList.add("比特币");
//        signList.add("互联网");
            viewTagFlowLayout.setAdapter(new TagAdapter<String>(signList) {
                @Override
                public View getView(FlowLayout parent, int position, String str) {
                    TextView tv = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_user_flow,parent, false);
                    tv.setText(str);
                    return tv;
                }
            });
        }
    }

}
