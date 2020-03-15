package com.linkb.jstx.component;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import com.linkb.R;

public class ActiveStarsView extends RelativeLayout {


    public ActiveStarsView(Context context) {
        super(context);
        initView(context, null, 0);
    }

    public ActiveStarsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public ActiveStarsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    ImageView viewActiveFlag;
    RecyclerView viewRecyclerView;
    MyAdapter myAdapter;
    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {

        View view=LayoutInflater.from(context).inflate(R.layout.view_active_stars_layout, this,true);
//        addView(view);
        viewActiveFlag = findViewById(R.id.viewActiveFlag);
        viewRecyclerView = findViewById(R.id.viewRecyclerView);
        myAdapter=new MyAdapter();
        myAdapter.setNumberStars(5);
        myAdapter.setRating(0);

        viewRecyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        viewRecyclerView.setAdapter(myAdapter);

    }
    public void setStartShowNumber(int value){
        myAdapter.setNumberStars(value);
    }

    public static void main(String[] args) {
        test(0);
        test(1);
        test(2);
        test(3);
        test(4);
        test(5);
        test(6);
        test(7);
        test(8);
        test(9);
        test(10);
        test(11);
        test(12);
        test(13);
        test(14);
    }

    private static void test(double active){
        int startNum=5;

        double startsV=active/2D;

        StringBuffer stringBuffer=new StringBuffer();

        {
            double fullLen=Math.floor(startsV);
            if(active>10){
                startNum= (int) (fullLen+1);
            }
            double hasHalf=startsV-fullLen;
            stringBuffer.append("startsV:").append(startsV).append(",fullLen:").append(fullLen)
                    .append(",hasHalf:").append(hasHalf).append("startNum:").append(startNum);
        }

        double rating=startsV;

        String starts="";
        for (int position=0;position<startNum;position++){
            int index=position+1;
            double halfT=index-rating;
            boolean hasHalf=Math.abs(halfT)>0 && Math.abs(halfT)<1;
            if(index<=rating){
                starts+="晶";
            }else if(hasHalf){
                starts+="日";
            }else {
                starts+="口";
            }
        }
        stringBuffer.append(",starts:").append(starts);
        System.out.println(stringBuffer.toString());
    }

    /**
     * 一点活跃度半颗星，所以值需要除以2
     * @param active
     */
    public void setStartValue(Double active){
        setStartValue(active==null?0.0D:active.doubleValue());
    }
    public void setStartValue(double active){

        int startNum=5;
        double startsV=active/2D;
        {
            double fullLen=Math.floor(startsV);
            if(active>10){
                startNum= (int) (fullLen+1);
            }
            double hasHalf=startsV-fullLen;
        }
        double rating=startsV;
        myAdapter.setNumberStars(startNum);
        myAdapter.setRating(rating);

        myAdapter.notifyDataSetChanged();
    }


    public static class MyAdapter extends RecyclerView.Adapter<MyHodler>{

        int numberStars;
        double rating;
        public void setRating(double  rating){//评级0-100
            this.rating=rating;
        }
        public void setNumberStars(int numberStars){//0-100
                this.numberStars=numberStars;
        }
        @Override
        public int getItemCount() {
            return numberStars;
        }
        @NonNull
        @Override
        public MyHodler onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
            Context context=viewGroup.getContext();
            View view=LayoutInflater.from(context).inflate(R.layout.item_start_img,null);
            return new MyHodler(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyHodler myHodler, int position) {
            int index=position+1;
            double halfT=index-rating;
            boolean hasHalf=Math.abs(halfT)>0 && Math.abs(halfT)<1;
            if(index<=rating){
                myHodler.viewStarsImg.setImageResource(R.mipmap.ic_stars_p);
            }else if(hasHalf){
                myHodler.viewStarsImg.setImageResource(R.mipmap.ic_stars_half);
            }else {
                myHodler.viewStarsImg.setImageResource(R.mipmap.ic_stars);
            }
        }


    }

    public static class MyHodler extends RecyclerView.ViewHolder{
        ImageView viewStarsImg;
        public MyHodler(@NonNull View itemView) {
            super(itemView);
            viewStarsImg=itemView.findViewById(R.id.viewStarsImg);
        }
    }

}
