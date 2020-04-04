package com.linkb.jstx.activity.wallet;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.linkb.BuildConfig;
import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.URLConstant;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.database.GroupRepository;
import com.linkb.jstx.fragment.PasswordInputFragment;
import com.linkb.jstx.model.Group;
import com.linkb.jstx.network.http.HttpRequestBody;
import com.linkb.jstx.network.http.HttpRequestLauncher;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManagerV2;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.http.SimpleHttpRequestListener;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.ContactsResult;
import com.linkb.jstx.network.result.CurrencyListResult;
import com.linkb.jstx.network.result.v2.ListMyCurrencyResult;
import com.linkb.jstx.network.result.v2.SendRedPacketResultV2;
import com.linkb.jstx.util.ConvertUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**  红包
* */
public class RedPacketActivityV2 extends BaseActivity implements PasswordInputFragment.VerifyApplyPasswordListener, HttpRequestListener<BaseResult> {

    @BindView(R.id.imageView10)
    WebImageView coinImage;
    @BindView(R.id.textView48) TextView coinNameTv;
    @BindView(R.id.red_packet_amount_edit) EditText redPacketAmountEditText;
    @BindView(R.id.red_packet_description_edit) EditText redPacketDescriptionEditText;
    @BindView(R.id.textView49) TextView redPacketDescriptionTextNumberTv;
    @BindView(R.id.send_red_packet_confirm_button)  Button gotoSendRedPacketBtn;
    @BindView(R.id.red_packet_number_edit) EditText redPacketNumberEdt;
    @BindView(R.id.textView51) TextView redPacketNumberTv;
    @BindView(R.id.coin_number_cly)
    View redPacketNumberView;
    @BindView(R.id.textView86)
    TextView collectTv;
    @BindView(R.id.textView50)
    TextView redPacketAmount;
    @BindView(R.id.textView87)
    TextView redPacketTypeTv;
    @BindView(R.id.textView88)
    TextView changeRedPacketTypeTv;
    @BindView(R.id.textView90)
    TextView amountTvTips;
    @BindView(R.id.textView91)
    TextView amountTv;
    @BindView(R.id.textView92)
    TextView groupMemberSizeTv;

    private PasswordInputFragment mPasswordInputFragment;

    private int redPacketDescriptionNumber = 9;

    /**选择的币种ID
    * */
    private int mCurrencyID = 0 ;

    /**  发红包种类
     *  1 表示普通红包, 2表示普通群发红包   , 3表示拼手气群发红包
    * */
    private int redPacketType ;


    /** 群成员数量
    * */
    private int groupMemberSize = 0;


    String receiveId;//远程用户ID，或者群ID

    User self;


    @Override
    protected void initComponents() {
        ButterKnife.bind(this);
        self=Global.getCurrentUser();
        redPacketType = getIntent().getIntExtra(Constant.RedPacketType.RED_PACKET_TYPE, 1);
        receiveId = getIntent().getStringExtra(Constant.CHAT_OTHRES_ID);

        changeRedTypeUI(redPacketType);

        redPacketDescriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("RedPacket", charSequence.toString());
                Log.d("RedPacket", i + "");
                Log.d("RedPacket", i1 + "");
                Log.d("RedPacket", i2 + "");
                redPacketDescriptionNumber -= i1;
                redPacketDescriptionNumber += i2;
                redPacketDescriptionTextNumberTv.setText(redPacketDescriptionNumber + "/25");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        redPacketAmountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                gotoSendRedPacketBtn.setEnabled(!TextUtils.isEmpty(charSequence));

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        HttpServiceManagerV2.listMyCurrency(self.getAccount(),"",this);

        queryGroupInfo(receiveId);
    }
    private boolean hasGroupRedPacket(){
        return redPacketType != Constant.RedPacketType.COMMON_RED_PACKET;
    }
    private void changeRedTypeUI( int redPacketType) {
        if (redPacketType == Constant.RedPacketType.COMMON_RED_PACKET){
            //单个红包
            redPacketAmount.setText( R.string.amount);
            collectTv.setVisibility(View.GONE);
            redPacketNumberView.setVisibility(View.GONE);
            redPacketTypeTv.setVisibility(View.GONE);
            changeRedPacketTypeTv.setVisibility(View.GONE);
            groupMemberSizeTv.setVisibility(View.GONE);
        }else if (redPacketType == Constant.RedPacketType.COMMON_GROUP_LURKEY_RED_PACKET){
            //群拼手气红包
            redPacketAmount.setText( R.string.total_amount);
            collectTv.setVisibility(View.VISIBLE);
            redPacketNumberView.setVisibility(View.VISIBLE);
            redPacketTypeTv.setVisibility(View.VISIBLE);
            changeRedPacketTypeTv.setVisibility(View.VISIBLE);
            redPacketTypeTv.setText(R.string.collect_red_packet_tips);
            changeRedPacketTypeTv.setText(R.string.change_common_red_packet_tips);
            groupMemberSizeTv.setVisibility(View.VISIBLE);
            groupMemberSizeTv.setText(getResources().getString(R.string.group_member_size, groupMemberSize));
        }else {
            //群普通红包
            redPacketAmount.setText( R.string.single_amount);
            collectTv.setVisibility(View.GONE);
            redPacketNumberView.setVisibility(View.VISIBLE);
            redPacketTypeTv.setVisibility(View.VISIBLE);
            changeRedPacketTypeTv.setVisibility(View.VISIBLE);
            groupMemberSizeTv.setVisibility(View.VISIBLE);
            redPacketTypeTv.setText(R.string.common_red_packet_tips);
            changeRedPacketTypeTv.setText(R.string.change_collect_red_packet_tips);
            groupMemberSizeTv.setText(getResources().getString(R.string.group_member_size, groupMemberSize));
        }

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_red_packet;
    }

    @OnClick(R.id.currency_cly)
    public void queryCurrencyList() {
        Intent intent = new Intent(RedPacketActivityV2.this, MoreCurrencyActivityV2.class);
        startActivityForResult(intent, 0x11);
    }

    @OnClick(R.id.send_red_packet_confirm_button)
    public void gotoSendRedPackedt(){
        mPasswordInputFragment = PasswordInputFragment.getInstance(redPacketAmountEditText.getText().toString(), coinNameTv.getText().toString());
        mPasswordInputFragment.setListener(this);

//        if (mPasswordInputFragment == null){
//            mPasswordInputFragment = PasswordInputFragment.getInstance(redPacketAmountEditText.getText().toString(), coinNameTv.getText().toString());
//            mPasswordInputFragment.setListener(this);
//        }else {
//            mPasswordInputFragment.setRedPacketMoney(redPacketAmountEditText.getText().toString());
//            mPasswordInputFragment.setCurrencyMark(coinNameTv.getText().toString());
//        }
        mPasswordInputFragment.show(RedPacketActivityV2.this.getSupportFragmentManager(), "PasswordInputFragment");
    }

    @OnClick(R.id.red_packet_record_fly)
    public void gotoRedPacketSendRecord(){
        Intent intent = new Intent(RedPacketActivityV2.this, RedPacketRecordActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.textView88)
    public void changeGroupRedPacketType(){
        if (redPacketType == Constant.RedPacketType.COMMON_GROUP_RED_PACKET){
            redPacketType = Constant.RedPacketType.COMMON_GROUP_LURKEY_RED_PACKET;

        }else {
            redPacketType = Constant.RedPacketType.COMMON_GROUP_RED_PACKET;
        }
        changeRedTypeUI(redPacketType);
    }

    @OnClick(R.id.back_btn)
    public void onBackBtn(){
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 0x11){
            if (data != null) {
                CurrencyListResult.DataListBean dataListBean = (CurrencyListResult.DataListBean) data.getSerializableExtra(CurrencyListResult.DataListBean .class.getName());
                coinImage.load(BuildConfig.API_HOST  + dataListBean.getCurrencyIcon(), R.mipmap.btc);
                coinNameTv.setText(dataListBean.getCurrencyMark());
                mCurrencyID = dataListBean.getId();
                amountTv.setText(ConvertUtils.doubleToString(dataListBean.getAmount()));

            }
        }
    }


    @Override
    public void onVerifySuccess(String password) {
        sendRedPacket(password);
    }

    @Override
    public void onVerifyFailed() {

    }

    /** 发红包
    * */
    private void sendRedPacket(String password){
        String sendMoney = redPacketNumberEdt.getText().toString();
        int redPacketNumber = 1;
        if (!TextUtils.isEmpty(sendMoney)){
            redPacketNumber = Integer.valueOf(redPacketNumberEdt.getText().toString());
        }
        showProgressDialog(getResources().getString(R.string.sending_red_packet));

        HttpServiceManagerV2.redpackgeSend(mCurrencyID, redPacketAmountEditText.getText().toString(),
                redPacketDescriptionEditText.getText().toString(),redPacketNumber, redPacketType, password,receiveId, this);
    }

    @Override
    public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
        if (result instanceof SendRedPacketResultV2){
            hideProgressDialog();
            if (result.isSuccess()){
                SendRedPacketResultV2 redPacketResult = (SendRedPacketResultV2) result;
                SendRedPacketResultV2.DataBean dataBean1= redPacketResult.getData();
                Intent intent = getIntent();
                intent.putExtra(SendRedPacketResultV2.DataBean.class.getName(), dataBean1);
                setResult(RESULT_OK, intent);
                finish();
            }else {
                showToastView(result.message);
            }
        }else if (result instanceof ListMyCurrencyResult){
            if (result.isSuccess()){

                ListMyCurrencyResult result1 = (ListMyCurrencyResult) result;

               if(result1.getData() != null & result1.getData().size() > 0){
                   ListMyCurrencyResult.DataBean dataListBean = result1.getData().get(0);
                   coinImage.load(BuildConfig.API_HOST  + dataListBean.getCurrencyIcon(), R.mipmap.btc);
                   coinNameTv.setText(dataListBean.getCurrencyName());
                   mCurrencyID = dataListBean.getCurrencyId();
                   amountTv.setText(ConvertUtils.doubleToString(dataListBean.getBalance()));
               }
            }
        }

    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        hideProgressDialog();
    }

    private void queryGroupInfo(final String receiveId){
        if(hasGroupRedPacket() || TextUtils.isEmpty(receiveId)){
            //个人
            return;
        }
        final long  groupId=Long.valueOf(receiveId);
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.GET_BASE_DATA_URL, ContactsResult.class);
        requestBody.get();
        requestBody.setRunWithOtherThread();
        HttpRequestLauncher.execute(requestBody, new SimpleHttpRequestListener<ContactsResult>() {
            @Override
            public void onHttpRequestSucceed(ContactsResult result, OriginalCall call) {

                if (result.isNotEmpty(Group.class)) {
                    for (Group group :result.groupList) {
                        if (group.id == groupId){
                            groupMemberSize = group.memberList.size();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    groupMemberSizeTv.setText(getResources().getString(R.string.group_member_size, groupMemberSize));
                                }
                            });
                        }
                    }
                    GroupRepository.saveAll(result.groupList);
                }

            }
        });
    }
}
