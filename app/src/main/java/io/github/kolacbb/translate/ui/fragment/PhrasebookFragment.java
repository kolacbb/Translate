package io.github.kolacbb.translate.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import io.github.kolacbb.translate.R;
import io.github.kolacbb.translate.base.BaseFragment;
import io.github.kolacbb.translate.base.DividerItemDecoration;
import io.github.kolacbb.translate.flux.stores.PhrasebookStore;
import io.github.kolacbb.translate.flux.stores.base.Store;
import io.github.kolacbb.translate.model.entity.Result;
import io.github.kolacbb.translate.ui.activity.HomeActivity;
import io.github.kolacbb.translate.ui.adapter.WordListAdapter;

/**
 * Created by Kola on 2016/6/12.
 */
public class PhrasebookFragment extends BaseFragment {

    WordListAdapter adapter;
    @BindView(R.id.dictionary_view)
    RecyclerView dictionaryView;

    PhrasebookStore phrasebookStore;

    public static String BUNDLE_KEY = "bundle_phrasebook_store";

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_phrasebook;
    }

    @Override
    protected Store getStore() {
        if (phrasebookStore == null) {
            phrasebookStore = new PhrasebookStore();
        }
        return phrasebookStore;
    }

    @Override
    protected void afterCreate(Bundle saveInstanceState) {
        if (saveInstanceState != null) {
            phrasebookStore = (PhrasebookStore) saveInstanceState.getSerializable(BUNDLE_KEY);
        } else if (phrasebookStore == null){
            phrasebookStore = new PhrasebookStore();
        }
        init();
        getActionCreatorManager().getTranslateActionCreator().fetchFavorList();
    }

    public static Fragment newInstance() {
        return new PhrasebookFragment();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BUNDLE_KEY, phrasebookStore);
    }

    private void init() {
        RecyclerView.LayoutManager manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        dictionaryView.setLayoutManager(manager);
        dictionaryView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL_LIST));
        adapter = new WordListAdapter(new ArrayList<Result>(),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Result result = adapter.getItemData(dictionaryView.getChildAdapterPosition(v));
                        HomeActivity.start(getContext(), result.getQuery());
                    }
                });
        dictionaryView.setAdapter(adapter);
    }

    @Subscribe
    public void onStoreChange(PhrasebookStore.PhrasebookStoreChangeEvent event) {
        render();

    }

    public void render() {
        adapter.setData(phrasebookStore.getFavorList());
        adapter.notifyDataSetChanged();
    }
}
