package io.github.kolacbb.translate.data.local;

import java.util.List;

import io.github.kolacbb.translate.data.TranslateDataSource;
import io.github.kolacbb.translate.data.entity.Translate;
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

    @Override
    public void getTranslate(String query, String source, GetTranslateCallback callback) {
        Translate translate = TranslateDB.getInstance()
                .getTranslate(query, source);
        if (translate != null) {
            callback.onTranslateLoaded(translate);
        } else {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public List<Translate> getPhrasebook() {
        return TranslateDB.getInstance().getTranslateFromPhrasebook();
    }

    @Override
    public void addPhrasebook(Translate translate) {
        TranslateDB.getInstance().addTranslate(TranslateOpenHelper.TABLE_NAME_PHRASEBOOK, translate);
    }

    @Override
    public void addHistory(Translate translate) {
        TranslateDB.getInstance().addTranslate(TranslateOpenHelper.TABLE_NAME_DICT, translate);
        TranslateDB.getInstance().addTranslate(TranslateOpenHelper.TABLE_NAME_HISTORY, translate);
    }

    @Override
    public void deletePhrasebook(Translate translate) {
        TranslateDB.getInstance().removeTranslate(TranslateOpenHelper.TABLE_NAME_PHRASEBOOK, translate);
    }

    @Override
    public void deletePhrasebooks(List<Translate> translates) {
        TranslateDB.getInstance().removeTranslate(TranslateOpenHelper.TABLE_NAME_PHRASEBOOK, translates);
    }

    @Override
    public List<Translate> getHistory() {
        return TranslateDB.getInstance().getTranslateFromHistory();
    }

    @Override
    public void deleteAllHistory() {
        TranslateDB.getInstance().clearTranslate(TranslateOpenHelper.TABLE_NAME_HISTORY);
    }

    @Override
    public void deleteHistory(Translate translate) {
        TranslateDB.getInstance().removeTranslate(TranslateOpenHelper.TABLE_NAME_HISTORY, translate);
    }
}
