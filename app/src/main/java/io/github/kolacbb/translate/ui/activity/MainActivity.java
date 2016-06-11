package io.github.kolacbb.translate.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.kolacbb.translate.R;
import io.github.kolacbb.translate.base.DividerItemDecoration;
import io.github.kolacbb.translate.db.TranslateDB;
import io.github.kolacbb.translate.flux.actions.ActionCreator;
import io.github.kolacbb.translate.flux.dispatcher.Dispatcher;
import io.github.kolacbb.translate.flux.stores.MainStore;
import io.github.kolacbb.translate.flux.stores.PhrasebookStore;
import io.github.kolacbb.translate.model.entity.Result;
import io.github.kolacbb.translate.model.entity.YouDaoResult;
import io.github.kolacbb.translate.ui.adapter.WordListAdapter;
import io.github.kolacbb.translate.view.DictionaryRecycleView;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_point)
    TextView pointTextView;
    @BindView(R.id.phonetic)
    TextView tvPhonetic;

    @BindView(R.id.error_view)
    View errorView;
    @BindView(R.id.history_rec_view)
    RecyclerView historyRecView;
    @BindView(R.id.progress_bar)
    ContentLoadingProgressBar progressBar;
    @BindView(R.id.translate_view)
    ScrollView translateView;
    @BindView(R.id.translation)
    TextView translation;
    @BindView(R.id.dictionary_view)
    CardView dictionaryView;
    @BindView(R.id.basic)
    TextView basicTextView;

    @BindView(R.id.add_book)
    ImageButton btAddPhrasebook;

    WordListAdapter adapter;

    Dispatcher dispatcher;
    MainStore mainStore;
    ActionCreator actionCreator;

    public static void start(Context context, String query) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("query", query);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TranslateDB.init(this);
        ButterKnife.bind(this);
        initDependencies();

        mainStore.register(this);

        RecyclerView.LayoutManager manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        historyRecView.setLayoutManager(manager);
        historyRecView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        adapter = new WordListAdapter(new ArrayList<Result>(),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = historyRecView.getChildAdapterPosition(v);
                        Result result = adapter.getItemData(position);
                        MainActivity.start(MainActivity.this, result.getQuery());
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(MainActivity.this, "star", Toast.LENGTH_SHORT).show();
                        int position = (int) v.getTag();
                        Result result = adapter.getItemData(position);
                        actionCreator.starWord(result);
                        //((ImageButton) v).setImageResource(R.drawable.ic_star_black_24px);
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(MainActivity.this, "unstar", Toast.LENGTH_SHORT).show();
                        int position = (int) v.getTag();
                        Result result = adapter.getItemData(position);
                        actionCreator.unstarWord(result);
                        //((ImageButton) v).setImageResource(R.drawable.ic_star_border_black_24px);
                    }
                });
        historyRecView.setAdapter(adapter);
        actionCreator.initView();
    }

    private void initDependencies() {
        dispatcher = Dispatcher.get();
        actionCreator = ActionCreator.get(dispatcher);
        mainStore = new MainStore();
        dispatcher.register(mainStore);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainStore.unregister(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String query = intent.getStringExtra("query");
        System.out.println("query");
        actionCreator.fetchTranslation(query);
    }

    @SuppressWarnings("ResourceType")
    public void render(MainStore store) {
        historyRecView.setVisibility(store.getHistoryRecycleViewVisiableState());
//        historyRecView.setData(store.getHistoryData());
        if (store.isInit()) {
            adapter.setData(store.getHistoryData());
            adapter.notifyDataSetChanged();
        }
        if (store.isLoading()) {
            System.out.println("Loading");
            progressBar.show();
        } else {
            progressBar.hide();
        }
        translateView.setVisibility(store.getTranslateViewVisiableState());
        errorView.setVisibility(store.getErrorViewState());

        if (store.isFinish() && store.getData() != null) {
            Result result = store.getData();
            translation.setText(result.getTranslation());
            if (result.isFavor()) {
                btAddPhrasebook.setImageResource(R.drawable.ic_star_black_24px);
            } else {
                btAddPhrasebook.setImageResource(R.drawable.ic_star_border_black_24px);
            }

            if (result.getUk_phonetic() != null) {
                tvPhonetic.setText(result.getUk_phonetic());
                dictionaryView.setVisibility(View.VISIBLE);
            } else {
                tvPhonetic.setVisibility(View.GONE);
            }
            if (result.getBasic() != null) {
                basicTextView.setText(result.getBasic());
                dictionaryView.setVisibility(View.VISIBLE);
            } else {
                dictionaryView.setVisibility(View.GONE);
            }
        }

    }

    @Subscribe
    public void onStoreChange(MainStore.MainStoreChangeEvent event) {
        render(mainStore);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.phrasebook:
                PhrasebookActivity.start(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.tv_clear, R.id.bt_translate, R.id.add_book})
    public void widgetOnClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_clear:
                pointTextView.setText("");
                actionCreator.initView();
                break;
            case R.id.bt_translate:
                //Toast.makeText(MainActivity.this, "translate", Toast.LENGTH_SHORT).show();
                if (pointTextView.getText().toString().trim().length() != 0)
                    actionCreator.fetchTranslation(pointTextView.getText().toString().trim());
                break;
            case R.id.add_book:

                if (mainStore.getData() != null) {
                    actionCreator.saveToPhrasebook(mainStore.getData());
                    Toast.makeText(MainActivity.this, "已加入生词本", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


}
