package com.linkb.jstx.adapter.trend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.network.result.GameResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private GameItemClickListener mListener;

    private List<GameResult.DataBean> mGameList = new ArrayList<>();

    private Context mContext;

    public GameAdapter(GameItemClickListener mListener, Context mContext) {
        this.mListener = mListener;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new GameViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_game, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder gameViewHolder, int i) {
        GameResult.DataBean dataBean = mGameList.get(i);
        gameViewHolder.gameIconImageView.load(dataBean.getDownLoadUrl(), R.mipmap.huoshentouxiang,999);
        gameViewHolder.nameTv.setText(dataBean.getName());
        gameViewHolder.gamedescriptionTv.setText(mContext.getString(R.string.game_type, dataBean.getType(), dataBean.getDesription()));
        gameViewHolder.downloadTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {mListener.downloadGame();}
            }
        });
        gameViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {mListener.checkGameDetail();}
            }
        });
    }

    public void addAll(List<GameResult.DataBean> list) {
        if (mGameList.equals(list)) {
            return;
        }
        mGameList.clear();
        mGameList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mGameList.size();
    }

    class GameViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView27)
        WebImageView gameIconImageView;
        @BindView(R.id.textView80)
        TextView nameTv;
        @BindView(R.id.textView81)
        TextView gamedescriptionTv;
        @BindView(R.id.download_tv)
        TextView downloadTv;
        @BindView(R.id.game_type_tv)
        TextView gameTypeTv;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface GameItemClickListener {
        void checkGameDetail();
        void downloadGame();
    }
}
