package io.github.kolacbb.translate.mvp.presenter;

import java.util.List;

import io.github.kolacbb.translate.data.TranslateDataSource;
import io.github.kolacbb.translate.data.TranslateRepository;
import io.github.kolacbb.translate.data.entity.Translate;
import io.github.kolacbb.translate.mvp.contract.TranslateContract;

/**
 * Created by zhangd on 2017/9/22.
 */

public class TranslatePresenter implements TranslateContract.Presenter {

    private TranslateRepository mRepository;
    private TranslateContract.View mView;

    public TranslatePresenter(TranslateRepository repository, TranslateContract.View view) {
        mRepository = repository;
        mView = view;
    }

    @Override
    public void getTranslate(String query, String source) {
        mView.showLoading();
        mRepository.getTranslate(query, source, new TranslateDataSource.GetTranslateCallback() {
            @Override
            public void onTranslateLoaded(Translate translate) {
                mView.showTranslate(translate);
            }

            @Override
            public void onDataNotAvailable() {
                mView.showErrorView();
            }
        });
    }

    @Override
    public List<Translate> getHistory() {
        return mRepository.getHistory();
    }

    @Override
    public void addPhrasebook(Translate translate) {
        mRepository.addPhrasebook(translate);
    }

    @Override
    public void removePhrasebook(Translate translate) {
        mRepository.deletePhrasebook(translate);
    }
}
