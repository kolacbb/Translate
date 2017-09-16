package io.github.kolacbb.translate.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.kolacbb.translate.R;
import io.github.kolacbb.translate.base.BaseFragment;
import io.github.kolacbb.translate.base.DividerItemDecoration;
import io.github.kolacbb.translate.flux.stores.base.Store;
import io.github.kolacbb.translate.flux.stores.TranslateMainStore;
import io.github.kolacbb.translate.model.entity.Result;
import io.github.kolacbb.translate.ui.activity.HomeActivity;
import io.github.kolacbb.translate.ui.adapter.WordListAdapter;
import io.github.kolacbb.translate.ui.view.ItemTouchHelperCallBack;
import io.github.kolacbb.translate.util.InputMethodUtils;
import io.github.kolacbb.translate.util.SpUtil;

/**
 * Created by Kola on 2016/6/12.
 */
public class TranslateMainFragment extends BaseFragment {

    WordListAdapter adapter;
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
    FrameLayout dictionaryView;
    @BindView(R.id.basic)
    TextView basicTextView;
    @BindView(R.id.add_book)
    ImageButton btAddPhrasebook;

    @BindView(R.id.bt_translate)
    Button translateButton;

    TranslateMainStore translateMainStore;
    public static String BUNDLE_KEY = "translate_store";

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_translate_main;
    }

    @Override
    protected Store getStore() {
        if (translateMainStore == null) {
            translateMainStore = new TranslateMainStore();
        }
        return translateMainStore;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BUNDLE_KEY, translateMainStore);
        Log.e("HomePage", "onSaveInstanceState");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.e("HomePage", "onViewStateRestored");
    }

    @Override
    protected void afterCreate(Bundle saveInstanceState) {
        if (saveInstanceState != null) {
            translateMainStore = (TranslateMainStore) saveInstanceState.getSerializable(BUNDLE_KEY);
            Log.e("HomePage", "afterCreate");
            return;
        }

        if (translateMainStore == null) {
            translateMainStore = new TranslateMainStore();
        }

        init();
        //dispatchFetchListNews();
        Log.e("HomePage", "dispatch initView action");
        getActionCreatorManager().getTranslateActionCreator().initView();

    }


    public static Fragment newInstance() {
        return new TranslateMainFragment();
    }

    private void init() {
        RecyclerView.LayoutManager manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        historyRecView.setLayoutManager(manager);
        historyRecView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL_LIST));
        adapter = new WordListAdapter(new ArrayList<Result>(),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = historyRecView.getChildAdapterPosition(v);
                        Result result = adapter.getItemData(position);
                        HomeActivity.start(getContext(), result.getQuery());
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (int) v.getTag();
                        Result result = adapter.getItemData(position);
                        getActionCreatorManager().getTranslateActionCreator().starWord(result);

                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (int) v.getTag();
                        Result result = adapter.getItemData(position);
                        getActionCreatorManager().getTranslateActionCreator().unstarWord(result);

                    }
                });
        historyRecView.setAdapter(adapter);
        ItemTouchHelperCallBack callBack = new ItemTouchHelperCallBack(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callBack);
        touchHelper.attachToRecyclerView(historyRecView);
    }

    @OnClick({R.id.tv_clear, R.id.bt_translate, R.id.add_book})
    public void widgetOnClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_clear:
                pointTextView.setText("");
                getActionCreatorManager().getTranslateActionCreator().initView();
                break;
            case R.id.bt_translate:
                InputMethodUtils.closeSoftKeyboard(translateButton);
                if (pointTextView.getText().toString().trim().length() != 0)
                    getActionCreatorManager().getTranslateActionCreator().fetchTranslation(pointTextView.getText().toString().trim());
                break;
            case R.id.add_book:

                if (translateMainStore.getData() != null) {
                    getActionCreatorManager().getTranslateActionCreator().saveToPhrasebook(translateMainStore.getData());
                }
                break;
        }
    }

    @Override
    public boolean onBackPressed() {
        if (translateMainStore.isFinish()) {
            getActionCreatorManager().getTranslateActionCreator().initView();
        } else {
            return super.onBackPressed();
        }
        return true;
    }

    @SuppressWarnings("ResourceType")
    public void render() {
        //Toast.makeText(getContext(), "action", Toast.LENGTH_SHORT).show();
        historyRecView.setVisibility(translateMainStore.getHistoryRecycleViewVisiableState());
        if (translateMainStore.isInit() && translateMainStore.getHistoryData() != null) {
            tvPhonetic.setVisibility(View.GONE);
            adapter.setData(translateMainStore.getHistoryData());
            adapter.notifyDataSetChanged();
        }
        if (translateMainStore.isLoading()) {
            progressBar.show();
        } else {
            progressBar.hide();
        }
        translateView.setVisibility(translateMainStore.getTranslateViewVisiableState());
        errorView.setVisibility(translateMainStore.getErrorViewState());

        if (translateMainStore.isFinish() && translateMainStore.getData() != null) {
            //System.out.println(translateMainStore.getData().getTranslation());
            Result result = translateMainStore.getData();
            pointTextView.setText(result.getQuery());
            translation.setText(result.getTranslation());
            if (result.isFavor()) {
                btAddPhrasebook.setImageResource(R.drawable.ic_star_black_24px);
            } else {
                btAddPhrasebook.setImageResource(R.drawable.ic_star_border_black_24px);
            }

            if (result.getUs_phonetic() != null) {
                String setting = SpUtil.findString(SettingsFragment.KEY_PHONETIC_LIST);
                if (setting == null || setting.equals(getString(R.string.pref_phonetic_default))) {
                    tvPhonetic.setText(result.getUk_phonetic());
                } else {
                    tvPhonetic.setText(result.getUs_phonetic());
                }

                tvPhonetic.setVisibility(View.VISIBLE);
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
    public void onStoreChange(TranslateMainStore.TranslateMainStoreChangeEvent event) {
        render();
    }

}
