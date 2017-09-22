package io.github.kolacbb.translate.component.contract;

import java.util.List;

import io.github.kolacbb.translate.base.BasePresenter;
import io.github.kolacbb.translate.base.BaseView;
import io.github.kolacbb.translate.data.entity.Translate;

/**
 * Created by zhangd on 2017/9/22.
 */

public class TranslateContract {
    public interface Presenter extends BasePresenter {

        void getTranslate(String query, String source);

        List<Translate> getHistory();

        void addPhrasebook(Translate translate);

        void removePhrasebook(Translate translate);
    }

    public interface View extends BaseView<Presenter> {
        void showLoading();

        void showErrorView();

        void showHistory();

        void showTranslate(Translate translate);
    }
}
