package io.github.kolacbb.translate.flux.actions.creator;

/**
 * Created by Kola on 2016/6/12.
 */

import java.util.List;

import io.github.kolacbb.translate.db.TranslateDB;
import io.github.kolacbb.translate.flux.actions.TranslateActions;
import io.github.kolacbb.translate.flux.actions.base.Action;
import io.github.kolacbb.translate.flux.actions.creator.base.BaseActionCreator;
import io.github.kolacbb.translate.flux.dispatcher.Dispatcher;
import io.github.kolacbb.translate.model.entity.Result;
import io.github.kolacbb.translate.model.entity.YouDaoResult;
import io.github.kolacbb.translate.service.base.DataLayer;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Kola on 2016/6/11.
 */
public class TranslateActionCreator extends BaseActionCreator {
    public TranslateActionCreator(Dispatcher dispatcher, DataLayer dataLayer) {
        super(dispatcher, dataLayer);
    }

//    public void fetchHistoryListWord() {
//        getDispatcher().dispatch(new Action.Builder().with(TranslateActions.ACTION_TRANSLATION_LOADING).build());
//    }

    public void fetchTranslation(String query) {
        // 分发开始刷新列表事件
        getDispatcher().dispatch(new Action.Builder().with(TranslateActions.ACTION_TRANSLATION_LOADING).build());

        // 本地数据库数据源
        Observable<Result> cache = getDataLayer().getTranslateService().getLocalTranslation(query);

        // 服务端数据源
        Observable<Result> network = getDataLayer().getTranslateService().getTranslation(query);

        // 没有本地数据在使用网络数据
        Observable<Result> source = Observable
                .concat(cache, network)
                // 依次遍历序列中的数据源， 返回第一个符合条件的数据源
                .first(new Func1<Result, Boolean>() {
                    @Override
                    public Boolean call(Result result) {
                        return result != null;
                    }
                });

        // 重新查询数据则更新history列表，在save方法中有判断，具体见TranslateDB
        source = source.doOnNext(new Action1<Result>() {
            @Override
            public void call(Result result) {
                getDataLayer().getTranslateService().saveToHistory(result);
            }
        });

        source.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Result>() {
                    @Override
                    public void call(Result result) {
                        getDispatcher().dispatch(new Action.Builder().with(TranslateActions.ACTION_TRANSLATION_FINISH)
                                .bundle(TranslateActions.KEY_TRANSLATION_ANSWER, result)
                                .build());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        System.out.println(throwable.getMessage());
                        Action action = new Action.Builder()
                                .with(TranslateActions.ACTION_TRANSLATION_NET_ERROR)
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

    public void clearHistory() {
        TranslateDB.getInstance().clearHistory();
        initView();
    }
}

