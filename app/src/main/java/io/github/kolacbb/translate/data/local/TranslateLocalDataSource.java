package io.github.kolacbb.translate.data.local;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import io.github.kolacbb.translate.data.TranslateDataSource;
import io.github.kolacbb.translate.data.entity.SmsEntry;
import io.github.kolacbb.translate.data.entity.Translate;
import io.github.kolacbb.translate.model.entity.Result;
import io.github.kolacbb.translate.util.DateUtils;

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
        translate.setFavor(true);
    }

    @Override
    public void addHistory(Translate translate) {
        TranslateDB.getInstance().addTranslate(TranslateOpenHelper.TABLE_NAME_DICT, translate);
        TranslateDB.getInstance().addTranslate(TranslateOpenHelper.TABLE_NAME_HISTORY, translate);
    }

    @Override
    public void deletePhrasebook(Translate translate) {
        TranslateDB.getInstance().removeTranslate(TranslateOpenHelper.TABLE_NAME_PHRASEBOOK, translate);
        translate.setFavor(false);
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

    @Override
    public List<SmsEntry> getSms(Context context) {
        Cursor cursor = null;
        List<SmsEntry> list = new ArrayList<>();
        try {
            cursor = context.getContentResolver().query(Uri.parse("content://sms/"),
                    null, null, null, null);
            while (cursor.moveToNext()) {
                SmsEntry smsEntry = new SmsEntry();
                smsEntry.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                smsEntry.setContent(cursor.getString(cursor.getColumnIndex("body")));
                smsEntry.setDate(DateUtils.getDateString(cursor.getLong(cursor.getColumnIndex("date"))));
                list.add(smsEntry);
            }
        } catch (Exception e) {
            //subscriber.onError(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }
}
