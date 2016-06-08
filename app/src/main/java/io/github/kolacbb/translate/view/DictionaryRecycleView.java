package io.github.kolacbb.translate.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.kolacbb.translate.R;
import io.github.kolacbb.translate.model.entity.Result;

/**
 * Created by Kola on 2016/6/8.
 */
public class DictionaryRecycleView extends RecyclerView {

    private DictionaryAdapter adapter;

    public DictionaryRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.recycle_view_dictionary, this);
        adapter = new DictionaryAdapter(getContext(), true);
        this.setAdapter(adapter);
    }

    public void setData(List<Result> list) {
        adapter.setData(list);
        adapter.notifyDataSetChanged();
    }

    public class DictionaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<Result> list;
        Context context;
        boolean btVisible;
        public DictionaryAdapter(Context context, boolean btVisible) {
            this.context = context;
            this.btVisible = btVisible;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_dictionary, parent, false);

            return new DictionaryViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            DictionaryViewHolder viewHolder = (DictionaryViewHolder) holder;
            Result result = list.get(position);
            viewHolder.tvQuery.setText(result.getQuery());
            viewHolder.tvTranslation.setText(result.getTranslation());
            if (btVisible) {
                viewHolder.btFavor.setVisibility(View.VISIBLE);
            } else {
                viewHolder.btFavor.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            if (list == null) {
                return 0;
            }
            return list.size();
        }

        public void setData(List<Result> list) {
            this.list = list;
        }

        public List<Result> getData() {
            return list;
        }

        public Result getItemData(int position) {
            return list.get(position);
        }
    }

    public class DictionaryViewHolder extends ViewHolder {
        @BindView(R.id.query)
        TextView tvQuery;
        @BindView(R.id.translation)
        TextView tvTranslation;
        @BindView(R.id.bt_favor)
        ImageButton btFavor;
        public DictionaryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
        }
    }
}
