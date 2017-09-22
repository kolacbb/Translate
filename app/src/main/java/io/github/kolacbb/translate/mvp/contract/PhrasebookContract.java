package io.github.kolacbb.translate.mvp.contract;

import java.util.List;

import io.github.kolacbb.translate.base.BasePresenter;
import io.github.kolacbb.translate.base.BaseView;
import io.github.kolacbb.translate.model.entity.Result;

/**
 * Created by zhangd on 2017/8/10.
 */

public interface PhrasebookContract {
    interface Presenter extends BasePresenter {

        List<Result> getAllPhrasebookContract();

        List<Result> getSortedPhrasebook(int type);

        void removePhrase(Result result);
    }

    interface View extends BaseView<Presenter> {

        void showLoadingView();

        void showEmptyView();

    }
}
