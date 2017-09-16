package io.github.kolacbb.translate.data.local;

import io.github.kolacbb.translate.data.TranslateDataSource;
import io.github.kolacbb.translate.model.entity.Result;

/**
 * Created by zhangd on 2017/9/14.
 */

public class TranslateLocalDataSource implements TranslateDataSource {
    @Override
    public void getTranslation(String message, LoadTranslationCallback callback) {
        Result result = TranslateDB.getInstance().getTransalte(message);
        callback.onTranslationLoaded(result);
    }
}
