package io.github.kolacbb.translate.data;

import android.content.Context;

import java.util.List;

import io.github.kolacbb.translate.data.entity.SmsEntry;
import io.github.kolacbb.translate.data.entity.Translate;
import io.github.kolacbb.translate.model.entity.Result;

/**
 * Created by zhangd on 2017/9/14.
 */

public interface TranslateDataSource {

    interface LoadTranslationCallback {
        void onTranslationLoaded(Result result);
    }

    void getTranslation(String message, LoadTranslationCallback callback);

    interface GetTranslateCallback {

        void onTranslateLoaded(Translate translate);

        void onDataNotAvailable();
    }

    void getTranslate(String query, String source, GetTranslateCallback callback);

    List<Translate> getPhrasebook();

    void addPhrasebook(Translate translate);

    void addHistory(Translate translate);

    void deletePhrasebook(Translate translate);

    void deletePhrasebooks(List<Translate> translates);

    List<Translate> getHistory();

    void deleteAllHistory();

    void deleteHistory(Translate translate);

    List<SmsEntry> getSms(Context context);
}
