package io.github.kolacbb.translate.flux.actions;

import android.os.Bundle;

import java.util.List;

import io.github.kolacbb.translate.db.TranslateDB;
import io.github.kolacbb.translate.flux.dispatcher.Dispatcher;
import io.github.kolacbb.translate.model.entity.Result;
import io.github.kolacbb.translate.model.entity.YouDaoResult;
import io.github.kolacbb.translate.protocol.DataLayer;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Kola on 2016/6/11.
 */
public class TranslateActionCreator extends BaseActionCreator{
    public TranslateActionCreator(Dispatcher dispatcher, DataLayer dataLayer) {
        super(dispatcher, dataLayer);
    }

//    public void fetchHistoryListWord() {
//        getDispatcher().dispatch(new Action.Builder().with(TranslateActions.ACTION_TRANSLATION_LOADING).build());
//    }

    public void fetchTranslation(String query) {
        //分发开始刷新列表事件
        getDispatcher().dispatch(new Action.Builder().with(TranslateActions.ACTION_TRANSLATION_LOADING).build());

        //服务端数据源
        Observable<YouDaoResult> network = getDataLayer().getTranslateService().getTranslation(query);

        // 订阅事件序列
        network.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<YouDaoResult>() {
                    @Override
                    public void call(YouDaoResult youDaoResult) {
                        //先缓存一下
                        TranslateDB.getInstance().saveToHistory(youDaoResult.getResult());
                        getDispatcher().dispatch(new Action.Builder().with(TranslateActions.ACTION_TRANSLATION_FINISH)
                                .bundle(TranslateActions.KEY_TRANSLATION_ANSWER, youDaoResult.getResult())
                                .build());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
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

    public void starWord(Result result) {
        TranslateDB.getInstance().saveToPhrasebook(result);
        initView();
    }

    public void unstarWord(Result result) {
        TranslateDB.getInstance().deleteFromPhrasebook(result);
        initView();
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
