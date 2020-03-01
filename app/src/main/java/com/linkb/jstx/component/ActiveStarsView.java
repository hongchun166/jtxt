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
    public void setStartValue(float value){
        value=value/2;
        myAdapter.setRating(value);
        if(value<=5){
            myAdapter.setNumberStars(5);
        }else if(value<=10){
            myAdapter.setNumberStars(10);
        }else {
            myAdapter.setNumberStars((int) (value+1));
        }
        myAdapter.notifyDataSetChanged();
    }


    public static class MyAdapter extends RecyclerView.Adapter<MyHodler>{

        int numberStars;
        float rating;
        public void setRating(float  rating){//评级0-100
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
            float half=position-rating;
            boolean hasHalf=Math.abs(half)>0 && Math.abs(half)<1;
            if(position<rating){
                myHodler.viewStarsImg.setImageResource(R.mipmap.ic_stars_p);
            }else if(hasHalf){
                //half stars
                myHodler.viewStarsImg.setImageResource(R.mipmap.ic_stars_p);
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
