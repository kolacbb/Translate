package io.github.kolacbb.translate.flux.stores;

import android.util.Log;
import android.view.View;

import io.github.kolacbb.translate.flux.actions.Action;
import io.github.kolacbb.translate.flux.actions.TranslateActions;
import io.github.kolacbb.translate.model.entity.YouDaoResult;

/**
 * Created by Kola on 2016/6/5.
 */
public class MainStore extends Store{

    private YouDaoResult youDaoResult;
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
                youDaoResult = (YouDaoResult) action.getData().get(TranslateActions.KEY_TRANSLATION_ANSWER);
                Log.e("OnAction", "这里有一个Action过来了 " + youDaoResult.getTranslation().get(0));
                mChangeEvent = new MainStoreChangeEvent();
                fetchStatue = FetchStatue.FINISH;
                emitStoreChange();
                break;
            }
            case TranslateActions.ACTION_TRANSLATION_LOADING: {
                fetchStatue = FetchStatue.LOADING;
                mChangeEvent = new MainStoreChangeEvent();
                emitStoreChange();
            }


        }
    }

    public static final int VIS = View.VISIBLE;
    public static final int GONE = View.GONE;

    public int getLoadingViewVisiableState() {
        return fetchStatue == FetchStatue.LOADING ? VIS : GONE;
    }

    public int getHistoryRecycleViewVisiableState() {
        return fetchStatue == FetchStatue.INIT ? View.VISIBLE : View.GONE;
    }

    public int getTranslateViewVisiableState() {
        return fetchStatue == FetchStatue.FINISH ? View.VISIBLE : View.GONE;
    }

    public boolean isFinish() {
        return fetchStatue == FetchStatue.FINISH;
    }

    public YouDaoResult getData() {
        return youDaoResult;
    }
    public class MainStoreChangeEvent implements Store.StoreChangeEvent {}
}
