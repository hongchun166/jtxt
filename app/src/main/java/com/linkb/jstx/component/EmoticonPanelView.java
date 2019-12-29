
package com.linkb.jstx.component;


import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.linkb.jstx.adapter.ViewPaperAdapter;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EmoticonPanelView extends FrameLayout implements OnPageChangeListener, View.OnClickListener {

    private static final int PAGE_SIZE = 30;
    private ViewPager viewPager;
    private OnItemClickedListener emotionSelectedListener;
    private List<View> pagerListView = new ArrayList<>();
    private int pageIndex = 0;
    private LinearLayout tagPanelView;
    private int panelHeight = 0;
    public EmoticonPanelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (viewPager.getMeasuredHeight() > 0 && panelHeight != viewPager.getMeasuredHeight()) {
            panelHeight = viewPager.getMeasuredHeight();
            buildEmotionPagerView();
        }
    }

    private void buildEmotionPagerView() {
        pagerListView.clear();
        int cellWidth = Resources.getSystem().getDisplayMetrics().widthPixels / 6;
        int cellHeight = (viewPager.getMeasuredHeight() - tagPanelView.getMeasuredHeight()) / 5;
        Iterator<Map.Entry<String, Integer>> entries = LvxinApplication.EMOTION_MAP.entrySet().iterator();
        int index = 0;
        while (entries.hasNext()) {
            Map.Entry<String, Integer> entry = entries.next();
            if (index % PAGE_SIZE == 0) {
                GridLayout gridLayout = new GridLayout(getContext());
                gridLayout.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                gridLayout.setColumnCount(6);
                gridLayout.setRowCount(5);
                pagerListView.add(gridLayout);
            }
            int line = index / PAGE_SIZE;
            GridLayout gridLayout = (GridLayout) pagerListView.get(line);
            ImageView cellView = (ImageView) LayoutInflater.from(getContext()).inflate(R.layout.layout_emoticon_itemview, gridLayout, false);
            cellView.setImageResource(entry.getValue());
            cellView.setOnClickListener(this);
            cellView.setTag(entry.getKey());
            gridLayout.addView(cellView, new GridLayout.LayoutParams(new ViewGroup.LayoutParams(cellWidth, cellHeight)));

            if (index % PAGE_SIZE == PAGE_SIZE - 2) {
                ImageView deleteButton = (ImageView) LayoutInflater.from(getContext()).inflate(R.layout.layout_emoticon_itemview, gridLayout, false);
                int pading = (int) (Resources.getSystem().getDisplayMetrics().density * 12 + 0.5f);
                deleteButton.setPadding(pading, pading, pading, pading);
                deleteButton.setImageResource(R.drawable.icon_emotion_delete);
                deleteButton.setOnClickListener(this);
                deleteButton.setTag("DELETE");
                gridLayout.addView(deleteButton, new GridLayout.LayoutParams(new ViewGroup.LayoutParams(cellWidth, cellHeight)));
                index++;
            }
            index++;
        }

        viewPager.setAdapter(new ViewPaperAdapter(pagerListView));
        viewPager.addOnPageChangeListener(this);

        tagPanelView.getChildAt(0).setSelected(true);
    }


    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        tagPanelView = findViewById(R.id.tagPanelView);
        viewPager = findViewById(R.id.emoticonViewPager);
    }

    @Override
    public void onClick(View v) {
        emotionSelectedListener.onItemClicked(v.getTag(), v);
    }


    public void setOnEmotionSelectedListener(OnItemClickedListener emoticoSelectedListener) {
        this.emotionSelectedListener = emoticoSelectedListener;
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {


    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {


    }

    @Override
    public void onPageSelected(int index) {
        if (pageIndex != index) {
            tagPanelView.getChildAt(index).setSelected(true);
            tagPanelView.getChildAt(pageIndex).setSelected(false);
            pageIndex = index;
        }

    }
}
