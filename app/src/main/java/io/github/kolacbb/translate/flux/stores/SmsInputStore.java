package io.github.kolacbb.translate.flux.stores;

import java.util.ArrayList;
import java.util.List;

import io.github.kolacbb.translate.flux.actions.TranslateActions;
import io.github.kolacbb.translate.flux.actions.base.Action;
import io.github.kolacbb.translate.flux.stores.base.Store;
import io.github.kolacbb.translate.model.entity.SmsEntry;

/**
 * Created by Kola on 2016/6/26.
 */
public class SmsInputStore extends Store {
    List<SmsEntry> list = new ArrayList<>();
    @Override
    public StoreChangeEvent getChangeEvent() {
        return mChangeEvent;
    }

    @Override
    public void onAction(Action action) {
        System.out.println("Action coming");
        switch (action.getType()) {
            case TranslateActions.ACTION_SMS_INIT: {
                list = (List<SmsEntry>) action.getData().get(TranslateActions.KEY_SMS_LIST);
                System.out.println(list.size());
                mChangeEvent = new SmsInputStoreChangeEventStore();
                emitStoreChange();
                break;
            }
            case TranslateActions.ACTION_SMS_ERROR: {
                mChangeEvent = new SmsInputStoreChangeEventStore();
                emitStoreChange();
                break;
            }
        }
    }

    public List<SmsEntry> getList() {
        return list;
    }

    public class SmsInputStoreChangeEventStore implements Store.StoreChangeEvent {
    }

}
