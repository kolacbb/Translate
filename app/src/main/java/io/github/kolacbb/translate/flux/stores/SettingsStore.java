package io.github.kolacbb.translate.flux.stores;

import java.io.Serializable;

import io.github.kolacbb.translate.flux.actions.base.Action;
import io.github.kolacbb.translate.flux.stores.base.Store;

/**
 * Created by Kola on 2016/6/14.
 */
public class SettingsStore extends Store {
    @Override
    public StoreChangeEvent getChangeEvent() {
        return mChangeEvent;
    }

    @Override
    public void onAction(Action action) {

    }

    public class SettingsStoreChangeEvent implements Store.StoreChangeEvent {}
}
