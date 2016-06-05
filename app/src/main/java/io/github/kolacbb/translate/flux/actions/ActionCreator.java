package io.github.kolacbb.translate.flux.actions;

import android.util.Log;

import io.github.kolacbb.translate.flux.dispatcher.Dispatcher;
import io.github.kolacbb.translate.inject.modules.ClientApiModel;
import io.github.kolacbb.translate.model.entity.YouDaoResult;
import io.github.kolacbb.translate.protocol.WarpClientApi;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Kola on 2016/6/5.
 */
public class ActionCreator {
    private static ActionCreator instance;
    final Dispatcher dispatcher;
    private WarpClientApi mWarpClientApi;

    ActionCreator(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
        mWarpClientApi = new WarpClientApi(ClientApiModel.provideClientApi());
    }

    public static ActionCreator get(Dispatcher dispatcher) {
        if (instance == null) {
            instance = new ActionCreator(dispatcher);
        }
        return instance;
    }

    public void fetchTranslation(String word) {
        dispatcher.dispatch(new Action.Builder().with(TranslateActions.ACTION_TRANSLATION_LOADING).build());
        Observable<YouDaoResult> observable = mWarpClientApi.translate(word);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<YouDaoResult>() {
                    @Override
                    public void call(YouDaoResult youDaoResult) {
                        //Log.e("有结果了", youDaoResult.getTranslation().get(0));
                        Action action = new Action.Builder().with(TranslateActions.ACTION_TRANSLATION_FINISH)
                                .bundle(TranslateActions.KEY_TRANSLATION_ANSWER, youDaoResult)
                                .build();
                        dispatcher.dispatch(action);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }
}
