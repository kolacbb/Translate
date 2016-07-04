package io.github.kolacbb.translate.service;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import io.github.kolacbb.translate.model.entity.Result;
import io.github.kolacbb.translate.model.entity.SmsEntry;
import io.github.kolacbb.translate.model.entity.YouDaoResult;
import io.github.kolacbb.translate.protocol.ApiKey;
import io.github.kolacbb.translate.service.base.BaseManager;
import io.github.kolacbb.translate.service.base.DataLayer;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by Kola on 2016/6/11.
 */
public class TranslateManager extends BaseManager implements DataLayer.TranslateService {
    @Override
    public Observable<Result> getTranslation(String query) {
        return getApi().getTranslationYouDao(ApiKey.YOUDAO_KEY_FROM,
                ApiKey.YOUDAO_KEY,
                ApiKey.YOUDAO_TYPE,
                ApiKey.YOUDAO_DOCTYPE,
                ApiKey.YOUDAO_VERSION,
                query)
                .map(new Func1<YouDaoResult, Result>() {
                    @Override
                    public Result call(YouDaoResult youDaoResult) {
                        return youDaoResult.getResult();
                    }
                });
    }

    @Override
    public Observable<Result> getLocalTranslation(final String query) {
        return Observable.create(new Observable.OnSubscribe<Result>() {
            @Override
            public void call(Subscriber<? super Result> subscriber) {
                try {
                    subscriber.onStart();
                    Result result = getTranslateDB().getTransalte(query);
                    subscriber.onNext(result);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<Result>> getAllHistory() {
        return Observable.create(new Observable.OnSubscribe<List<Result>>() {
            @Override
            public void call(Subscriber<? super List<Result>> subscriber) {
                try {
                    subscriber.onStart();
                    List<Result> list = getTranslateDB().getAllHistory();
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<SmsEntry>> getSmsList() {
        return Observable.create(new Observable.OnSubscribe<List<SmsEntry>>() {
            @Override
            public void call(Subscriber<? super List<SmsEntry>> subscriber) {
                Cursor cursor = null;
                try {
                    subscriber.onStart();
                    cursor = getApplication().getContentResolver().query(Uri.parse("content://sms/"),
                            null, null, null, null);
                    List<SmsEntry> list = new ArrayList<>();
                    while (cursor.moveToNext()) {
                        SmsEntry smsEntry = new SmsEntry();
                        smsEntry.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                        smsEntry.setContent(cursor.getString(cursor.getColumnIndex("body")));
                        smsEntry.setDate(cursor.getString(cursor.getColumnIndex("date")));
                        list.add(smsEntry);
                    }
                    subscriber.onNext(list);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
        });
    }

    @Override
    public void saveToHistory(Result result) {
        getTranslateDB().saveToHistory(result);
    }

    @Override
    public void saveToPhrasebook(Result result) {
        getTranslateDB().saveToPhrasebook(result);
    }


}