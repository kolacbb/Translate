package io.github.kolacbb.translate.ui.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.kolacbb.translate.R;
import io.github.kolacbb.translate.base.DividerItemDecoration;
import io.github.kolacbb.translate.flux.actions.ActionCreator;
import io.github.kolacbb.translate.flux.dispatcher.Dispatcher;
import io.github.kolacbb.translate.flux.stores.PhrasebookStore;
import io.github.kolacbb.translate.model.entity.Result;
import io.github.kolacbb.translate.ui.adapter.WordListAdapter;
import io.github.kolacbb.translate.view.DictionaryRecycleView;

public class PhrasebookActivity extends AppCompatActivity {

    @BindView(R.id.dictionary_view)
    RecyclerView dictionaryView;

    WordListAdapter adapter;

    Dispatcher dispatcher;
    PhrasebookStore phrasebookStore;
    ActionCreator actionCreator;

    public static void start(Context context) {
        Intent intent = new Intent(context, PhrasebookActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phrasebook);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
        }
        ButterKnife.bind(this);
        initDependencies();
        phrasebookStore.register(this);

        RecyclerView.LayoutManager manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        dictionaryView.setLayoutManager(manager);
        dictionaryView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        adapter = new WordListAdapter(new ArrayList<Result>(),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Result result = adapter.getItemData(dictionaryView.getChildAdapterPosition(v));
                        //MainActivity.start(PhrasebookActivity.this, result.getQuery());
                    }
                });
        dictionaryView.setAdapter(adapter);

        actionCreator.fetchFavorList();
    }

    public void initDependencies() {
        dispatcher = Dispatcher.get();
        actionCreator = ActionCreator.get(dispatcher);
        phrasebookStore = new PhrasebookStore();
        dispatcher.register(phrasebookStore);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        phrasebookStore.unregister(this);
    }

    @Subscribe
    public void onStoreChange(PhrasebookStore.PhrasebookStoreChangeEvent event) {
        render(phrasebookStore);

    }

    public void render(PhrasebookStore store) {
        adapter.setData(store.getFavorList());
        adapter.notifyDataSetChanged();
        //dictionaryView.setData(store.getFavorList());
    }
}
