
package com.linkb.jstx.dialog;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.linkb.jstx.model.MicroServerMenu;
import com.linkb.R;
import com.linkb.jstx.util.AppTools;

import java.util.ArrayList;
import java.util.List;

public class MicroServerMenuWindow extends PopupWindow implements OnItemClickListener {
    private List<MicroServerMenu> list = new ArrayList<>();
    private OnMenuClickListener clickListener;
    private Context mContext;
    private ListViewAdapter listViewAdapter;
    private int windowWidth;
    private int menuWidth;
    private int switchWidth;

    public MicroServerMenuWindow(Context context, OnMenuClickListener listener) {
        super(context, null);
        mContext = context;
        clickListener = listener;
        setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.abc_menu_hardkey_panel_mtrl_mult));
        ListView listView = (ListView) LayoutInflater.from(context).inflate(R.layout.layout_microserver_menuwindow, null);
        listViewAdapter = new ListViewAdapter();
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(this);
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        switchWidth = screenWidth / 6;
        windowWidth = (int) ((screenWidth) / 2.5);
        menuWidth = (screenWidth - switchWidth) / 3;
        setContentView(listView);
        setWidth(windowWidth);
        setHeight(LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true);
        setFocusable(true);
    }

    public void showAtLocation(View parent, List<MicroServerMenu> mlist) {

        list.clear();
        list.addAll(mlist);
        listViewAdapter.notifyDataSetChanged();
        int index = (Integer) parent.getTag(R.drawable.icon);
        int left = switchWidth + index * menuWidth + (menuWidth - windowWidth) / 2;
        int bottom = AppTools.dip2px(50f);
        super.showAtLocation(parent, Gravity.BOTTOM | Gravity.START, left, bottom);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        clickListener.onMenuClicked(listViewAdapter.getItem(position));
        dismiss();
    }

    public interface OnMenuClickListener {
        void onMenuClicked(MicroServerMenu menu);
    }

    class ListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size() + 1;
        }

        @Override
        public MicroServerMenu getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (position == list.size()) {
                return new View(parent.getContext());
            }
            MicroServerMenu target = getItem(position);
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_menu_textview, null);
            TextView name = convertView.findViewById(R.id.name);
            name.setText(target.name);
            return convertView;
        }

    }

}
