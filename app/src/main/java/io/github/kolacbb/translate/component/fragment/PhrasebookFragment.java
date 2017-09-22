package io.github.kolacbb.translate.component.fragment;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.github.kolacbb.translate.R;
import io.github.kolacbb.translate.base.BaseFragment;
import io.github.kolacbb.translate.base.DividerItemDecoration;
import io.github.kolacbb.translate.data.TranslateRepository;
import io.github.kolacbb.translate.data.entity.Translate;
import io.github.kolacbb.translate.component.activity.HomeActivity;
import io.github.kolacbb.translate.component.adapter.WordListAdapter;

/**
 * Created by Kola on 2016/6/12.
 */
public class PhrasebookFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    List<Translate> mTranslates;
    WordListAdapter adapter;
    @BindView(R.id.dictionary_view)
    RecyclerView dictionaryView;

    public static String BUNDLE_KEY = "bundle_phrasebook_store";

    public static final String TITLE_STRING = "Phrasebook";

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_phrasebook;
    }

    @Override
    protected void afterCreate(Bundle saveInstanceState) {
        init();
    }

    public static BaseFragment newInstance() {
        return new PhrasebookFragment();
    }

    private void init() {
        // 设置toolbar
        mToolbar.setTitle(TITLE_STRING);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setHasOptionsMenu(true);

        RecyclerView.LayoutManager manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        dictionaryView.setLayoutManager(manager);
        dictionaryView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL_LIST));
        mTranslates = TranslateRepository.getInstance().getPhrasebook();
        adapter = new WordListAdapter(mTranslates,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = dictionaryView.getChildAdapterPosition(v);
                        if (adapter.isMultiMode()) {
                            if (adapter.getMultiSelectedItems().get(position, false)) {
                                adapter.removeItemFromMultiList(position);
                            } else {
                                adapter.addItemToMultiList(position);
                            }
                            getActivity().invalidateOptionsMenu();
                        } else {
                            Translate translate = adapter.getItemData(position);
                            HomeActivity.start(getContext(), translate.getQuery());
                        }
                    }
                },
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if (!adapter.isMultiMode()) {
                            adapter.setMultiSelectMode(true);
                            adapter.addItemToMultiList(dictionaryView.getChildAdapterPosition(view));
                            getActivity().invalidateOptionsMenu();
                            return true;
                        }
                        return false;
                    }
                });
        dictionaryView.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.phrasebook_menu, menu);
        if (searchView == null) {
            MenuItem item = menu.findItem(R.id.action_search);
            searchView = (SearchView) item.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                ArrayList mQueryedList = new ArrayList();
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    //Toast.makeText(getActivity(), newText, Toast.LENGTH_SHORT).show();
                    String text = newText.trim();
                    if (text.trim().length() == 0) {
                        adapter.setData(mTranslates);
                    } else {
                        mQueryedList.clear();
//                        for (Result result : phrasebookStore.getFavorList()) {
//                            if (result.getQuery().contains(newText)) {
//                                mQueryedList.add(result);
//                            }
//                        }
                        adapter.setData(mQueryedList);
                    }
                    adapter.notifyDataSetChanged();
                    return true;
                }
            });
        }

        if (adapter != null && adapter.isMultiMode()) {
            menu.findItem(R.id.action_search).setVisible(false);
            menu.findItem(R.id.sort).setVisible(false);
            int size = adapter.getMultiSelectedItems().size();
            if (size > 0) {
                menu.findItem(R.id.action_delete).setVisible(true);
                mToolbar.setTitle(String.valueOf(size));
            } else {
                menu.findItem(R.id.action_delete).setVisible(false);
                mToolbar.setTitle("");
            }

        } else {
            menu.findItem(R.id.action_search).setVisible(true);
            menu.findItem(R.id.action_delete).setVisible(false);
            menu.findItem(R.id.sort).setVisible(true);
            mToolbar.setTitle(TITLE_STRING);
        }
    }

    SearchView searchView;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.sort_by_alpha:
                //getActionCreatorManager().getTranslateActionCreator().fetchFavorListSortByAlpha();
                break;
            case R.id.sort_by_time:
                //getActionCreatorManager().getTranslateActionCreator().fetchFavorList();
                break;
            case R.id.action_delete:
                SparseBooleanArray array = adapter.getMultiSelectedItems();
                List<Translate> removeList = new ArrayList<>();
                for (int i = 0; i < adapter.getItemCount(); i++) {
                    if (array.get(i, false)) {
                        removeList.add(adapter.getItemData(i));
                    }
                }
                adapter.removeData(removeList);
                adapter.setMultiSelectMode(false);
                TranslateRepository.getInstance().deletePhrasebooks(removeList);
                getActivity().invalidateOptionsMenu();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onBackPressed() {
        if (adapter.isMultiMode()) {
            adapter.setMultiSelectMode(false);
            getActivity().invalidateOptionsMenu();
            return true;
        }
        return super.onBackPressed();
    }
}
