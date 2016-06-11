package io.github.kolacbb.translate.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import io.github.kolacbb.translate.R;
import io.github.kolacbb.translate.model.entity.Result;

/**
 * Created by Kola on 2016/6/11.
 */
public class WordListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Result> list;
    boolean showFavorButton;
    View.OnClickListener itemListener;

    View.OnClickListener starListener;
    View.OnClickListener unStarListener;

    public WordListAdapter(List<Result> list,View.OnClickListener itemListener) {
        this.list = list;
        this.itemListener = itemListener;
        this.showFavorButton = false;
    }

    public WordListAdapter(List<Result> list, View.OnClickListener itemListener,
                           View.OnClickListener starListener, View.OnClickListener unStarListener) {
        this.list = list;
        this.itemListener = itemListener;
        this.showFavorButton = true;
        this.starListener = starListener;
        this.unStarListener = unStarListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dictionary, parent, false);
        return new ItemVH(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemVH itemVH = (ItemVH) holder;
        Result result = list.get(position);
        itemVH.tvQuery.setText(result.getQuery());
        itemVH.tvTranslation.setText(result.getTranslation());
        itemVH.itemView.setOnClickListener(itemListener);
        if (showFavorButton) {
            if (result.isFavor()) {
                itemVH.btFavor.setImageResource(R.drawable.ic_star_black_24px);
                itemVH.btFavor.setOnClickListener(unStarListener);
            } else {
                itemVH.btFavor.setImageResource(R.drawable.ic_star_border_black_24px);
                itemVH.btFavor.setOnClickListener(starListener);
            }
            //设置Tag：分辨是哪一个Item中的View
            itemVH.btFavor.setTag(position);
            itemVH.btFavor.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(List<Result> list) {
        this.list = list;
    }

    public Result getItemData(int position) {
        return list.get(position);
    }

//    public interface CallBack {
//        void starclick(Result result);
//        void unStarClick(Result result);
//    }

    public static class ItemVH extends RecyclerView.ViewHolder {
        TextView tvQuery;
        TextView tvTranslation;
        ImageButton btFavor;
        public ItemVH(View itemView) {
            super(itemView);
            tvQuery = (TextView) itemView.findViewById(R.id.query);
            tvTranslation = (TextView) itemView.findViewById(R.id.translation);
            btFavor = (ImageButton) itemView.findViewById(R.id.bt_favor);
        }
    }
}
