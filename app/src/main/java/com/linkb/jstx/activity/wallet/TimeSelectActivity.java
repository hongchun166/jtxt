package com.linkb.jstx.activity.wallet;

import android.content.Intent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** 选择时间页面
* */
public class TimeSelectActivity extends BaseActivity {

    @BindView(R.id.textView63)
    TextView startDayTv;

    @BindView(R.id.textView64)
    TextView endDayTv;

    @BindView(R.id.day_date_picker)
    DatePicker dayDatePicker;

    @BindView(R.id.root_view)
    View rootView;

    private boolean enableSelectByDay = false;

    private boolean enableStartDaySelect = true;


    @Override
    protected void initComponents() {
        ButterKnife.bind(this);

        SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyy-MM-dd");
        Date curDate =  new Date(System.currentTimeMillis());
        String   str   =   formatter.format(curDate);
        startDayTv.setText(str);
        endDayTv.setText(str);

        String[] timeArray = str.split("-");
        dayDatePicker.init(Integer.valueOf(timeArray[0]), Integer.valueOf(timeArray[1]) - 1, Integer.valueOf(timeArray[2]),new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                if (enableStartDaySelect){
                    startDayTv.setText(i + "-" + (i1 + 1) + "-" + i2);
                }else {
                    endDayTv.setText(i + "-" + (i1 + 1) + "-" + i2);
                }
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_time_select;
    }

    @OnClick(R.id.textView62)
    public void changeTimeSelectType(){

    }

    @OnClick(R.id.finish_fly)
    public void onFinishSelected(){
        Intent intent = getIntent();
        intent.putExtra("beginTime", startDayTv.getText().toString());
        intent.putExtra("endTime", endDayTv.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    @OnClick(R.id.back_btn)
    public void backBtn(){
        finish();
    }


    @OnClick(R.id.textView63)
    public void changeTimeSelectStart(){
        enableStartDaySelect = true;
        String   str   = startDayTv.getText().toString();
        String[] timeArray = str.split("-");
        dayDatePicker.updateDate(Integer.valueOf(timeArray[0]), Integer.valueOf(timeArray[1]) - 1, Integer.valueOf(timeArray[2]));
    }

    @OnClick(R.id.textView64)
    public void changeTimeSelectEnd(){
        enableStartDaySelect = false;
        String   str   = endDayTv.getText().toString();
        String[] timeArray = str.split("-");
        dayDatePicker.updateDate(Integer.valueOf(timeArray[0]), Integer.valueOf(timeArray[1]) - 1, Integer.valueOf(timeArray[2]));
    }
}
