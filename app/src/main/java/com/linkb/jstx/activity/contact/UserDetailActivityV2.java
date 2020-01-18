package com.linkb.jstx.activity.contact;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.component.WebImageView;
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

    public static void navToAct(Context context,String friendAccount){
        Intent intent=new Intent(context,UserDetailActivityV2.class);
        intent.putExtra("friendAccount",friendAccount);
        context.startActivity(intent);
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
    protected int getContentLayout() {
        return R.layout.activity_user_detailed_v2;
    }
    @Override
    protected void initComponents() {
        unbinder=ButterKnife.bind(this);

        Bundle bundle=getIntent().getExtras();
        String friendAccount=bundle.getString("friendAccount");
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

    private Context getContext(){
        return this;
    }
    private void updateUi(QueryUserInfoResult.DataBean dataBean){
        viewTVName.setText(dataBean.getName());
        viewTVSign.setText(dataBean.getMotto());
        int sexResId=dataBean.getGender().equals("1")?R.mipmap.ic_sex_woman:R.mipmap.ic_sex_woman;
        viewIVSex.setImageResource(sexResId);
        viewTVCompany.setText("长江实业有限公司");
        viewTVProfession.setText("互联网IT Web前端");
        viewTVAddress.setText("中国-湖南省-长沙");
        List<String> signList=new ArrayList<>();
        signList.add("金融");
        signList.add("区块链");
        signList.add("直销");
        signList.add("PE");
        signList.add("比特币");
        signList.add("互联网");
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
