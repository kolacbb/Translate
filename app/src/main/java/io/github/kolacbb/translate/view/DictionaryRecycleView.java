package io.github.kolacbb.translate.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.github.kolacbb.translate.R;
import io.github.kolacbb.translate.base.DividerItemDecoration;
import io.github.kolacbb.translate.db.TranslateDB;
import io.github.kolacbb.translate.flux.actions.ActionCreator;
import io.github.kolacbb.translate.flux.dispatcher.Dispatcher;
import io.github.kolacbb.translate.model.entity.Result;
import io.github.kolacbb.translate.ui.activity.MainActivity;

/**
 * Created by Kola on 2016/6/8.
 */
public class DictionaryRecycleView extends LinearLayout implements View.OnClickListener{

    private static DictionaryAdapter adapter;

//    public DictionaryRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//        LayoutInflater.from(context).inflate(R.layout.recycle_view_dictionary, this);
//        adapter = new DictionaryAdapter(getContext(), true);
//        this.setAdapter(adapter);
//    }

    Dispatcher dispatcher;
    ActionCreator actionCreator;
    RecyclerView recyclerView;

    public DictionaryRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //LayoutInflater.from(context).inflate(R.layout.recycle_view_dictionary, this);
        LayoutInflater.from(context).inflate(R.layout.recycle_view_dictionary, this);
//        dispatcher = Dispatcher.get();
//        actionCreator = ActionCreator.get(dispatcher);
        recyclerView = (RecyclerView) findViewById(R.id.rec_view);
        RecyclerView.LayoutManager manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL_LIST));

        adapter = new DictionaryAdapter(context, true, this);
        recyclerView.setAdapter(adapter);
        //设置ItemView的divider

    }



    public void setData(List<Result> list) {
        adapter.setData(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        //int id = v.getId();
        int position = recyclerView.getChildAdapterPosition(v);
        Result result = adapter.getItemData(position);
        if (position != RecyclerView.NO_POSITION) {
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.putExtra("query", result.getQuery());
            getContext().startActivity(intent);
        }

//        Result result = adapter.getItemData(position);
//        if (id == R.id.bt_favor) {
//            TranslateDB.getInstance().updateHistoryToDict(result);
//        } else {
//            Intent intent = new Intent(getContext(), MainActivity.class);
//            getContext().startActivity(intent);
//        }
    }


    // custom RecyclerView Adapter
    public class DictionaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<Result> list = new ArrayList<>();
        View.OnClickListener listener;
        Context context;
        boolean btVisible;

        public DictionaryAdapter(Context context, boolean btVisible, View.OnClickListener listener) {
            this.context = context;
            this.btVisible = btVisible;
            this.listener = listener;
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_dictionary, parent, false);
            return new DictionaryViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            DictionaryViewHolder viewHolder = (DictionaryViewHolder) holder;
            final Result result = list.get(position);
            viewHolder.tvQuery.setText(result.getQuery());
            viewHolder.tvTranslation.setText(result.getTranslation());
            if (btVisible) {
                viewHolder.btFavor.setVisibility(View.VISIBLE);
                if (result.isFavor()) {
                    viewHolder.btFavor.setImageResource(R.drawable.ic_star_black_24px);
                    viewHolder.btFavor.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //TranslateDB.getInstance().deleteWordFromDict(result);
                            TranslateDB.getInstance().deleteFromPhrasebook(result);

                            adapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    viewHolder.btFavor.setImageResource(R.drawable.ic_star_border_black_24px);
                    viewHolder.btFavor.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //TranslateDB.getInstance().updateHistoryToDict(result);
                            TranslateDB.getInstance().saveToPhrasebook(result);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
                //actionCreator.initView();
            } else {
                viewHolder.btFavor.setVisibility(View.GONE);
            }
            viewHolder.itemView.setOnClickListener(listener);
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

    public class DictionaryViewHolder extends RecyclerView.ViewHolder {
        //@BindView(R.id.query)
        TextView tvQuery;
        //@BindView(R.id.translation)
        TextView tvTranslation;
        //@BindView(R.id.bt_favor)
        ImageButton btFavor;
        public DictionaryViewHolder(View itemView) {
            super(itemView);
            tvQuery = (TextView) itemView.findViewById(R.id.query);
            tvTranslation = (TextView) itemView.findViewById(R.id.translation);
            btFavor = (ImageButton) itemView.findViewById(R.id.bt_favor);
        }
    }
}
