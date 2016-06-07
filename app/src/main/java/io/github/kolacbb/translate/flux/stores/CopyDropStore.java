package io.github.kolacbb.translate.flux.stores;

import android.util.Log;

import io.github.kolacbb.translate.flux.actions.Action;
import io.github.kolacbb.translate.flux.actions.TranslateActions;
import io.github.kolacbb.translate.model.entity.Result;
import io.github.kolacbb.translate.model.entity.YouDaoResult;

/**
 * 对应CopyDropActivity
 * Created by Kola on 2016/6/5.
 */
public class CopyDropStore extends Store {

    private Result result;

    CopyDropStoreChangeEvent copyDropStoreChangeEvent;
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
                //Log.e("OnAction", "这里有一个Action过来了 " + youDaoResult.getTranslation().get(0));
                mChangeEvent = new CopyDropStoreChangeEvent();
                emitStoreChange();
                break;
            }

        }
    }

    public Result getData() {
        return result;
    }

    public class CopyDropStoreChangeEvent implements Store.StoreChangeEvent {}
}
