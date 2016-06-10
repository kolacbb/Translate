package io.github.kolacbb.translate.flux.actions;

import android.util.Log;

import java.util.List;

import io.github.kolacbb.translate.db.TranslateDB;
import io.github.kolacbb.translate.flux.dispatcher.Dispatcher;
import io.github.kolacbb.translate.inject.modules.ClientApiModel;
import io.github.kolacbb.translate.model.entity.Result;
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
        //分发开始刷新列表事件
        dispatcher.dispatch(new Action.Builder().with(TranslateActions.ACTION_TRANSLATION_LOADING).build());

        //服务端数据源
        Observable<YouDaoResult> observable = mWarpClientApi.translate(word);

        //本地数据库数据源


        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<YouDaoResult>() {
                    @Override
                    public void call(YouDaoResult youDaoResult) {
                        Log.e("有结果了", youDaoResult.getTranslation().get(0));
                        Action action = new Action.Builder().with(TranslateActions.ACTION_TRANSLATION_FINISH)
                                .bundle(TranslateActions.KEY_TRANSLATION_ANSWER, youDaoResult.getResult())
                                .build();
                        //save to database
                        //TranslateDB.getInstance().saveWord(youDaoResult.getResult());
                        TranslateDB.getInstance().saveToHistory(youDaoResult.getResult());
                        dispatcher.dispatch(action);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        System.out.println(throwable.getMessage());
                        Action action = new Action.Builder()
                                .with(TranslateActions.ACTION_TRANSLATION_NET_ERROR)
                                .bundle("key", "Value")
                                .build();
                        dispatcher.dispatch(action);
                    }
                });
    }

    public void fetchFavorList() {
        //List<Result> list = TranslateDB.getInstance().getAllDictWord();
        List<Result> list = TranslateDB.getInstance().getAllPhrasebook();
        Action action = new Action.Builder().with(TranslateActions.ACTION_PHRASEBOOK_INIT)
                .bundle(TranslateActions.KEY_PHRASEBOOK_FAVORITE, list)
                .build();
        dispatcher.dispatch(action);
    }

    public void saveToPhrasebook(Result result) {
        //TranslateDB.getInstance().updateHistoryToDict(result);
        TranslateDB.getInstance().saveToPhrasebook(result);
        result.setFavor(true);
        Action action = new Action.Builder().with(TranslateActions.ACTION_TRANSLATION_FINISH)
                .bundle(TranslateActions.KEY_TRANSLATION_ANSWER, result)
                .build();
        dispatcher.dispatch(action);
    }

    public void initView() {
        //List<Result> historyList = TranslateDB.getInstance().getAllHistoryWord();
        List<Result> historyList = TranslateDB.getInstance().getAllHistory();
        dispatcher.dispatch(new Action.Builder()
                .with(TranslateActions.ACTION_TRANSLATION_INIT_VIEW)
                .bundle(TranslateActions.KEY_TRANSLATION_HISTORY, historyList)
                .build());
    }
}
