package com.linkb.jstx.activity.contact;

import android.text.TextUtils;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.MineInviteInfoResult;
import com.linkb.video.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MineInvitationActivityV2 extends BaseActivity implements HttpRequestListener<MineInviteInfoResult> {
    private Unbinder unbinder;

    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_directly_number)
    TextView tvDirectlyNumber;
    @BindView(R.id.tv_indirect_number)
    TextView tvIndirectNumber;

    @Override
    protected void initComponents() {
        unbinder = ButterKnife.bind(this);
        HttpServiceManager.getInviteInfo(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_mine_invitation_v2;
    }

    @Override
    protected int getToolbarTitle() {
        return R.string.mine_invitation;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
    }

    /**
     * 网络获取我的邀请详情接口回调
     *
     * @param result 回调结果
     */
    @Override
    public void onHttpRequestSucceed(MineInviteInfoResult result, OriginalCall call) {
        if (result.isSuccess()) {
            String amount = TextUtils.isEmpty(result.data.prize) ? "0.00" : result.data.prize;
            String directlyNumber = TextUtils.isEmpty(result.data.inviteCount) ? "0" : result.data.inviteCount;
            String indirectNumber = TextUtils.isEmpty(result.data.inviteCount2) ? "0" : result.data.inviteCount2;
            tvAmount.setText(amount);
            tvDirectlyNumber.setText(directlyNumber);
            tvIndirectNumber.setText(indirectNumber);
        } else {
            ToastUtils.s(this, result.message);
        }
    }

    /**
     * 网络请求失败
     */
    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        ToastUtils.s(this, "获取邀请数据失败");
    }
}
