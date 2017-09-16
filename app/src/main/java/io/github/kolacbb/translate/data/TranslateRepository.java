package io.github.kolacbb.translate.data;

import io.github.kolacbb.translate.data.local.TranslateLocalDataSource;
import io.github.kolacbb.translate.data.remote.TranslateRemoteDataSource;
import io.github.kolacbb.translate.model.entity.Result;

/**
 * Created by zhangd on 2017/9/14.
 */

public class TranslateRepository implements TranslateDataSource{

    private static TranslateRepository sRepository;
    final TranslateLocalDataSource mTranslateLocalDataSource;
    final TranslateRemoteDataSource mTranslateRemoteDataSource;

    private TranslateRepository(){
        mTranslateLocalDataSource = new TranslateLocalDataSource();
        mTranslateRemoteDataSource = new TranslateRemoteDataSource();
    }

    public static TranslateRepository getInstance() {
        if (sRepository == null) {
            sRepository = new TranslateRepository();
        }
        return sRepository;
    }

    @Override
    public void getTranslation(final String message, final LoadTranslationCallback callback) {
        if (message == null || callback == null) {
            return;
        }

        mTranslateLocalDataSource.getTranslation(message, new LoadTranslationCallback() {
            @Override
            public void onTranslationLoaded(Result result) {
                if (result != null) {
                    callback.onTranslationLoaded(result);
                } else {
                    mTranslateRemoteDataSource.getTranslation(message, callback);
                }
            }
        });

    }
}
