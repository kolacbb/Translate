package io.github.kolacbb.translate.flux.stores;

import android.util.Log;
import android.view.View;

import java.util.List;

import io.github.kolacbb.translate.flux.actions.Action;
import io.github.kolacbb.translate.flux.actions.TranslateActions;
import io.github.kolacbb.translate.model.entity.Result;
import io.github.kolacbb.translate.model.entity.YouDaoResult;

/**
 * Created by Kola on 2016/6/5.
 */
public class MainStore extends Store{

    private Result result;
    private List<Result> historyList;
    private FetchStatue fetchStatue = FetchStatue.INIT;
    private enum FetchStatue {
        INIT, LOADING, FINISH, ERROR
    }
    @Override
    public StoreChangeEvent getChangeEvent() {
        return mChangeEvent;
    }

    @Override
    public void onAction(Action action) {
        switch (action.getType()) {
            case TranslateActions.ACTION_TRANSLATION_FINISH: {
                result = (Result) action.getData().get(TranslateActions.KEY_TRANSLATION_ANSWER);
                Log.e("OnAction", "这里有一个Action过来了 " + result.getQuery());
                mChangeEvent = new MainStoreChangeEvent();
                fetchStatue = FetchStatue.FINISH;
                emitStoreChange();
                break;
            }
            case TranslateActions.ACTION_TRANSLATION_LOADING: {
                Log.e("OnAction", "这里有一个Loading Action过来了 ");
                fetchStatue = FetchStatue.LOADING;
                mChangeEvent = new MainStoreChangeEvent();
                emitStoreChange();
                break;
            }
            case TranslateActions.ACTION_TRANSLATION_INIT_VIEW: {
                Log.e("OnAction", "这里有一个Init Action过来了 ");
                fetchStatue = FetchStatue.INIT;
                historyList = (List<Result>) action.getData().get(TranslateActions.KEY_TRANSLATION_HISTORY);
                mChangeEvent = new MainStoreChangeEvent();
                emitStoreChange();
                break;
            }
            case TranslateActions.ACTION_TRANSLATION_NET_ERROR: {
                Log.e("OnAction", "这里有一个Error Action过来了 ");
                fetchStatue = FetchStatue.ERROR;
                mChangeEvent = new MainStoreChangeEvent();
                emitStoreChange();
                break;
            }

        }

        System.out.println(fetchStatue == FetchStatue.LOADING);
    }

    public List<Result> getHistoryData() {
        return historyList;
    }

    public int getLoadingViewVisiableState() {
        return fetchStatue == FetchStatue.LOADING ? View.VISIBLE : View.GONE;
    }

    public int getHistoryRecycleViewVisiableState() {
        return fetchStatue == FetchStatue.INIT ? View.VISIBLE : View.GONE;
    }

    public int getTranslateViewVisiableState() {
        return fetchStatue == FetchStatue.FINISH ? View.VISIBLE : View.GONE;
    }

    public int getErrorViewState() {
        return fetchStatue == FetchStatue.ERROR ? View.VISIBLE : View.GONE;
    }

    public boolean isLoading() {
        return fetchStatue == FetchStatue.LOADING;
    }

    public boolean isFinish() {
        return fetchStatue == FetchStatue.FINISH;
    }

    public boolean isInit() {
        return fetchStatue == FetchStatue.INIT;
    }

    public Result getData() {
        return result;
    }
    public class MainStoreChangeEvent implements Store.StoreChangeEvent {}
}
