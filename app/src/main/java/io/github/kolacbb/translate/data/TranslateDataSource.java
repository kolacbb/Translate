package io.github.kolacbb.translate.data;

import io.github.kolacbb.translate.model.entity.Result;

/**
 * Created by zhangd on 2017/9/14.
 */

public interface TranslateDataSource {

    interface LoadTranslationCallback {
        void onTranslationLoaded(Result result);
    }

    void getTranslation(String message, LoadTranslationCallback callback);
}
