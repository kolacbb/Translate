package io.github.kolacbb.translate.ui.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import io.github.kolacbb.translate.R;
import io.github.kolacbb.translate.data.TranslateRepository;
import io.github.kolacbb.translate.data.entity.Translate;
import io.github.kolacbb.translate.ui.view.ItemTouchHelperCallBack;

/**
 * Created by Kola on 2016/6/11.
 */
public class WordListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperCallBack.ItemTouchHelperListener{
    List<Translate> list;
    boolean showFavorButton;
    View.OnClickListener itemListener;
    View.OnLongClickListener mOnLongClickListener;
    View.OnClickListener starListener;
    View.OnClickListener unStarListener;
    private SparseBooleanArray mMultiSelectedItem;

    //int[] data;
    //int dataLength = 0;

    public WordListAdapter(List<Translate> list, View.OnClickListener itemListener, View.OnLongClickListener longClickListener) {
        this.list = list;
        this.itemListener = itemListener;
        this.mOnLongClickListener = longClickListener;
        this.showFavorButton = false;
        //initData();
    }

    public WordListAdapter(List<Translate> list, View.OnClickListener itemListener,
                           View.OnClickListener starListener, View.OnClickListener unStarListener) {
        this.list = list;
        this.itemListener = itemListener;
        this.showFavorButton = true;
        this.starListener = starListener;
        this.unStarListener = unStarListener;
        //initData();
    }

//    public void initData() {
//        dataLength = list.size();
//        data = new int[dataLength];
//        for (int i = 0; i < data.length; i++) {
//            data[i] = i;
//        }
//    }

    public void setQueryWord(String query) {
        int flag = 0;
        for (int i = 0; i < list.size(); i++) {
            Translate translate = list.get(i);
            if (translate.getQuery().startsWith(query)) {
                //data[flag++] = i;
            }
        }
        //dataLength = flag;
        //notifyDataSetChanged();
    }

//    public void setData(int[] data) {
//        this.data = data;
//        dataLength = data.length;
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dictionary, parent, false);
        return new ItemVH(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemVH itemVH = (ItemVH) holder;

        Translate translate = list.get(position);
        //Result translate = list.get(data[position]);

        itemVH.tvQuery.setText(translate.getQuery());
        itemVH.tvTranslation.setText(translate.getTranslation());
        itemVH.itemView.setOnClickListener(itemListener);
        if (mMultiSelectedItem != null && mMultiSelectedItem.get(position, false)) {
            itemVH.itemView.setBackgroundColor(Color.parseColor("#e0e0e0"));
        } else {
            itemVH.itemView.setBackgroundColor(Color.parseColor("#fafafa"));
        }
        if (mOnLongClickListener != null) {
            itemVH.itemView.setOnLongClickListener(mOnLongClickListener);
        }
        if (showFavorButton) {
            if (translate.isFavor()) {
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
        //return dataLength;
    }

    public void setData(List<Translate> list) {
        this.list = list;
        //initData();
    }

    public Translate getItemData(int position) {
        return list.get(position);
    }

    public void removeData(List<Translate> list) {
        this.list.removeAll(list);
        notifyDataSetChanged();
    }

    public void setMultiSelectMode(boolean isMulti) {
        if (isMulti) {
            if (mMultiSelectedItem == null) {
                mMultiSelectedItem = new SparseBooleanArray();
            }
            mMultiSelectedItem.clear();
        } else {
            mMultiSelectedItem = null;
            notifyDataSetChanged();
        }
    }

    public boolean isMultiMode() {
        return mMultiSelectedItem != null;
    }

    public SparseBooleanArray getMultiSelectedItems() {
        return mMultiSelectedItem;
    }

    public void addItemToMultiList(int position) {
        mMultiSelectedItem.append(position, true);
        notifyItemChanged(position);
    }

    public void removeItemFromMultiList(int position) {
        mMultiSelectedItem.delete(position);
        TranslateRepository.getInstance().deleteHistory(getItemData(position));
        notifyItemChanged(position);
    }

    @Override
    public void onItemRemoved(int position) {
        Translate translate = getItemData(position);
        TranslateRepository.getInstance().deleteHistory(translate);
        list.remove(translate);
        notifyItemRemoved(position);
    }

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
