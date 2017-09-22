package io.github.kolacbb.translate.mvp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import io.github.kolacbb.translate.R;
import io.github.kolacbb.translate.base.BaseFragment;
import io.github.kolacbb.translate.base.DividerItemDecoration;
import io.github.kolacbb.translate.data.TranslateRepository;
import io.github.kolacbb.translate.data.entity.Translate;
import io.github.kolacbb.translate.mvp.contract.TranslateContract;
import io.github.kolacbb.translate.ui.activity.CameraTranslateActivity;
import io.github.kolacbb.translate.ui.activity.HomeActivity;
import io.github.kolacbb.translate.ui.adapter.WordListAdapter;
import io.github.kolacbb.translate.ui.fragment.SettingsFragment;
import io.github.kolacbb.translate.ui.view.ItemTouchHelperCallBack;
import io.github.kolacbb.translate.util.InputMethodUtils;
import io.github.kolacbb.translate.util.SpUtil;

/**
 * Created by Kola on 2016/6/12.
 */
public class TranslateFragment extends BaseFragment implements TranslateContract.View {

    WordListAdapter mAdapter;
    @BindView(R.id.tv_point)
    EditText mTranslateET;
    @BindView(R.id.phonetic)
    TextView mPhoneticTv;
    @BindView(R.id.error_view)
    View mErrorView;
    @BindView(R.id.history_rec_view)
    RecyclerView mHistoryRecView;
    @BindView(R.id.progress_bar)
    ContentLoadingProgressBar mProgressBar;
    @BindView(R.id.translate_view)
    ScrollView mTranslateView;
    @BindView(R.id.translation)
    TextView mTranslationTv;
    @BindView(R.id.dictionary_view)
    FrameLayout mDictionaryView;
    @BindView(R.id.basic)
    TextView mExplainsTv;
    @BindView(R.id.add_book)
    ImageButton mStarBtn;
    @BindView(R.id.cameraTranslate)
    ImageView mCameraTranslateBtn;

    @BindView(R.id.bt_translate)
    Button mTranslateBtn;

    private Translate mTranslate;

    private TranslateContract.Presenter mPresenter;

    public static TranslateFragment newInstance() {
        return new TranslateFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_translate_main;
    }

    @Override
    protected void afterCreate(Bundle saveInstanceState) {
        RecyclerView.LayoutManager manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mHistoryRecView.setLayoutManager(manager);
        mHistoryRecView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL_LIST));
        mAdapter = new WordListAdapter(new ArrayList<Translate>(),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = mHistoryRecView.getChildAdapterPosition(v);
                        Translate translate = mAdapter.getItemData(position);
                        HomeActivity.start(getContext(), translate.getQuery());
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (int) v.getTag();
                        Translate translate = mAdapter.getItemData(position);
                        TranslateRepository.getInstance().addPhrasebook(translate);
                        mAdapter.notifyItemChanged(position);

                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (int) v.getTag();
                        Translate translate = mAdapter.getItemData(position);
                        TranslateRepository.getInstance().deletePhrasebook(translate);
                        mAdapter.notifyItemChanged(position);
                    }
                });
        mHistoryRecView.setAdapter(mAdapter);
        ItemTouchHelperCallBack callBack = new ItemTouchHelperCallBack(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callBack);
        touchHelper.attachToRecyclerView(mHistoryRecView);
        showHistory();
    }

    public void onNewTranslate(String query) {
        mPresenter.getTranslate(query, SpUtil.findString(SettingsFragment.KEY_TRANSLATE_SOURCE));
    }

    @OnClick({R.id.tv_clear, R.id.bt_translate, R.id.add_book, R.id.translation, R.id.cameraTranslate})
    public void widgetOnClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_clear:
                mTranslateET.setText("");
                showHistory();
                break;
            case R.id.bt_translate:
                InputMethodUtils.closeSoftKeyboard(mTranslateBtn);
                if (mTranslateET.getText().toString().trim().length() != 0) {
                    onNewTranslate(mTranslateET.getText().toString().trim());
                }
                break;
            case R.id.add_book:
                if (mTranslate != null) {
                    if (mTranslate.isFavor()) {
                        mPresenter.removePhrasebook(mTranslate);
                        showTranslate(mTranslate);
                    }else {
                        mPresenter.addPhrasebook(mTranslate);
                        showTranslate(mTranslate);
                    }
                }
                break;
            case R.id.translation:
                Intent intent = new Intent(getContext(), HomeActivity.class);
                intent.putExtra("query", mTranslationTv.getText().toString());
                startActivity(intent);
                break;
            case R.id.cameraTranslate:
                startActivity(new Intent(getContext(), CameraTranslateActivity.class));
                break;
        }
    }

    @Override
    public boolean onBackPressed() {
        if (mHistoryRecView.getVisibility() != View.VISIBLE) {
            showHistory();
        } else {
            return super.onBackPressed();
        }
        return true;
    }

    @Override
    public void showLoading() {
        mPhoneticTv.setVisibility(View.GONE);
        mHistoryRecView.setVisibility(View.GONE);
        mTranslateView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
        mProgressBar.show();
    }

    @Override
    public void showErrorView() {
        mPhoneticTv.setVisibility(View.GONE);
        mHistoryRecView.setVisibility(View.GONE);
        mTranslateView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.VISIBLE);
        mProgressBar.hide();
    }

    @Override
    public void showHistory() {
        mPhoneticTv.setVisibility(View.GONE);
        mCameraTranslateBtn.setVisibility(View.VISIBLE);
        mHistoryRecView.setVisibility(View.VISIBLE);
        mTranslateView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
        mProgressBar.hide();
        mAdapter.setData(mPresenter.getHistory());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showTranslate(Translate translate) {
        mHistoryRecView.setVisibility(View.GONE);
        mTranslateView.setVisibility(View.VISIBLE);
        mErrorView.setVisibility(View.GONE);
        mProgressBar.hide();
        // deal data
        mTranslate = translate;
        mTranslateET.setText(translate.getQuery());
        mTranslateET.setSelection(mTranslateET.getText().toString().length());
        mTranslationTv.setText(translate.getTranslation());
        if (translate.isFavor()) {
            mStarBtn.setImageResource(R.drawable.ic_star_black_24px);
        } else {
            mStarBtn.setImageResource(R.drawable.ic_star_border_black_24px);
        }

        if (translate.getUs_phonetic() != null && translate.getUk_phonetic() != null) {
            String setting = SpUtil.findString(SettingsFragment.KEY_PHONETIC_LIST);
            if (setting == null || setting.equals(getString(R.string.pref_phonetic_default))) {
                mPhoneticTv.setText(translate.getUk_phonetic());
            } else {
                mPhoneticTv.setText(translate.getUs_phonetic());
            }

            mPhoneticTv.setVisibility(View.VISIBLE);
            mCameraTranslateBtn.setVisibility(View.GONE);
        } else {
            mPhoneticTv.setVisibility(View.GONE);
            mCameraTranslateBtn.setVisibility(View.VISIBLE);
        }
        if (translate.getExplains() != null) {
            mExplainsTv.setText(translate.getExplains() + "\n" + translate.getWeb());
            mDictionaryView.setVisibility(View.VISIBLE);
        } else {
            mDictionaryView.setVisibility(View.GONE);
        }
    }

    @Override
    public void setPresenter(TranslateContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
