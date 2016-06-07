package io.github.kolacbb.translate.protocol;

import android.content.Context;
import android.provider.ContactsContract;

import io.github.kolacbb.translate.db.TranslateDB;
import io.github.kolacbb.translate.model.entity.Result;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Kola on 2016/6/6.
 */
public class DataLayer {

    private static DataLayer dataLayer;

    public static DataLayer getInstance() {
        if (dataLayer == null) {
            dataLayer = new DataLayer();
        }
        return dataLayer;
    }

//    public DataLayer(Context context) {
//        this.context = context;
//    }

    public Observable<Result> getDBWord(final String word) {
        return Observable.create(new Observable.OnSubscribe<Result>() {
            @Override
            public void call(Subscriber<? super Result> subscriber) {
                subscriber.onStart();
                Result result = TranslateDB.getInstance().queryWords(word);
                subscriber.onNext(result);
                subscriber.onCompleted();
            }
        });
    }



}
