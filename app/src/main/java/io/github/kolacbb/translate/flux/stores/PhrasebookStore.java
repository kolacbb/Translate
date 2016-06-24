package io.github.kolacbb.translate.flux.stores;

import android.util.Log;

import java.util.List;

import io.github.kolacbb.translate.flux.actions.base.Action;
import io.github.kolacbb.translate.flux.actions.TranslateActions;
import io.github.kolacbb.translate.flux.stores.base.Store;
import io.github.kolacbb.translate.model.entity.Result;

/**
 * Created by Kola on 2016/6/8.
 */
public class PhrasebookStore extends Store {

    private List<Result> favorList;

    @Override
    public StoreChangeEvent getChangeEvent() {
        return mChangeEvent;
    }

    @Override
    public void onAction(Action action) {
        switch (action.getType()) {
            case TranslateActions.ACTION_PHRASEBOOK_INIT: {
//                Log.e("OnAction", "有了Action");
                favorList = (List<Result>) action.getData().get(TranslateActions.KEY_PHRASEBOOK_FAVORITE);
//                for (Result r : favorList) {
//                    System.out.println(r.getQuery());
//                }
                mChangeEvent = new PhrasebookStoreChangeEvent();
                emitStoreChange();
                break;
            }
        }
    }

    public List<Result> getFavorList() {
        return favorList;
    }


    public class PhrasebookStoreChangeEvent implements Store.StoreChangeEvent {}
}
