
package com.linkb.jstx.activity.setting;


import android.content.res.XmlResourceParser;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.adapter.AppSettingListAdapter;
import com.linkb.jstx.bean.AppSetting;
import com.linkb.R;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息配置
 */
public class MessageSettingActivity extends BaseActivity {
    private RecyclerView recyclerView;

    @Override
    public void initComponents() {

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        parserSettingXml();
    }

    private void parserSettingXml(){
        List<AppSetting> settingList = new ArrayList<>();
        try {
            XmlResourceParser parser = getResources().getXml(R.xml.message_settings);
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_TAG && "item".equals(parser.getName())) {
                    String label = parser.getAttributeValue(null, "label");
                    String hint = parser.getAttributeValue(null, "hint");
                    String key = parser.getAttributeValue(null, "key");
                    String def = parser.getAttributeValue(null, "def");

                    AppSetting setting = new AppSetting();
                    setting.hint= hint;
                    setting.key =key;
                    setting.def =def;
                    setting.label = label;
                    settingList.add(setting);
                }
                event = parser.next();
            }
            parser.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        recyclerView.setAdapter(new AppSettingListAdapter(settingList));
    }

    @Override
    public int getContentLayout() {

        return R.layout.activity_message_setting;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.label_setting_notify;
    }
}
