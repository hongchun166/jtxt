package com.linkb.jstx.dialog;

import android.content.Context;
import android.content.res.AssetManager;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import com.linkb.R;
import com.linkb.jstx.dialog.base.BaseDialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RegionChangeDialogV2 extends BaseDialog implements View.OnClickListener {

    public OnRegionChangeListener mListener;
    private Context mContext;
    private int mType;

    public RegionChangeDialogV2(Context context) {
        super(context);
        this.mContext = context;
        initUI();

    }

    private void initUI() {
        setContentView(R.layout.dialog_region_change_v2);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.BOTTOM;
        System.out.println("当前数据----" + getJson("citycode.json", mContext));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_no:
                updateStatus(0);
                break;
            case R.id.ll_yes:
                updateStatus(1);
                break;
            case R.id.tv_ok:
                if (mListener != null) mListener.marriageStatus(mType);
                break;
        }
    }

    /**
     * 修改所有选中状态
     *
     * @param type 状态 0为未婚1为已婚
     */
    public void updateStatus(int type) {
        this.mType = type;

    }

    /**
     * 读取assets本地json
     *
     * @param fileName
     * @param context
     * @return
     */
    public static String getJson(String fileName, Context context) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * 设置婚姻状态选择的回调监听
     *
     * @param checkListener 对象
     */
    public void setOnMarriageCheckListener(OnRegionChangeListener checkListener) {
        this.mListener = checkListener;
    }

    public interface OnRegionChangeListener {
        /**
         * 性别选择的回调监听
         *
         * @param type 0为未婚，1为已婚
         */
        void marriageStatus(int type);
    }

}
