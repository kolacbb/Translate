package io.github.kolacbb.translate.ui.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.kolacbb.translate.R;
import io.github.kolacbb.translate.flux.actions.ActionCreator;
import io.github.kolacbb.translate.flux.dispatcher.Dispatcher;
import io.github.kolacbb.translate.flux.stores.PhrasebookStore;
import io.github.kolacbb.translate.view.DictionaryRecycleView;

public class PhrasebookActivity extends AppCompatActivity {

    @BindView(R.id.dictionary_view)
    DictionaryRecycleView dictionaryView;

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
        //System.out.println("enenenenenen");
        render(phrasebookStore);

    }

    public void render(PhrasebookStore store) {
        dictionaryView.setData(store.getFavorList());
        //System.out.println(store.getFavorList().size());
    }
}
