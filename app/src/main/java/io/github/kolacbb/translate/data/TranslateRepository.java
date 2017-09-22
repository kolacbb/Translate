package io.github.kolacbb.translate.data;

import android.content.Context;

import java.util.List;

import io.github.kolacbb.translate.data.entity.SmsEntry;
import io.github.kolacbb.translate.data.entity.Translate;
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

    private TranslateRepository() {
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

    @Override
    public void getTranslate(final String query, final String source, final GetTranslateCallback callback) {
        mTranslateLocalDataSource.getTranslate(query, source, new GetTranslateCallback() {
            @Override
            public void onTranslateLoaded(Translate translate) {
                callback.onTranslateLoaded(translate);
                mTranslateLocalDataSource.addHistory(translate);
            }

            @Override
            public void onDataNotAvailable() {
                mTranslateRemoteDataSource.getTranslate(query, source, new GetTranslateCallback() {
                    @Override
                    public void onTranslateLoaded(Translate translate) {
                        mTranslateLocalDataSource.addDict(translate);
                        mTranslateLocalDataSource.addHistory(translate);
                        callback.onTranslateLoaded(translate);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public List<Translate> getPhrasebook() {
        return mTranslateLocalDataSource.getPhrasebook();
    }

    @Override
    public void addPhrasebook(Translate translate) {
        mTranslateLocalDataSource.addPhrasebook(translate);
    }

    @Override
    public void addHistory(Translate translate) {
        mTranslateLocalDataSource.addHistory(translate);
    }

    @Override
    public void deletePhrasebook(Translate translate) {
        mTranslateLocalDataSource.deletePhrasebook(translate);
    }

    @Override
    public void deletePhrasebooks(List<Translate> translates) {
        mTranslateLocalDataSource.deletePhrasebooks(translates);
    }

    @Override
    public List<Translate> getHistory() {
        return mTranslateLocalDataSource.getHistory();
    }

    @Override
    public void deleteAllHistory() {
        mTranslateLocalDataSource.deleteAllHistory();
    }

    @Override
    public void deleteHistory(Translate translate) {
        mTranslateLocalDataSource.deleteHistory(translate);
    }

    @Override
    public List<SmsEntry> getSms(Context context) {
        return mTranslateLocalDataSource.getSms(context);
    }
}
