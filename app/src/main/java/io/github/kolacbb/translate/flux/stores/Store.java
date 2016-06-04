package io.github.kolacbb.translate.flux.stores;

import io.github.kolacbb.translate.flux.actions.Action;
import io.github.kolacbb.translate.flux.dispatcher.Dispatcher;

/**
 * Created by Kola on 2016/6/4.
 */
public abstract class Store {

    final Dispatcher dispatcher;

    protected Store(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    void emitStoreChange() {
        dispatcher.emitChange(changeEvent());
    }

    abstract StoreChangeEvent changeEvent();
    public abstract void onAction(Action action);

    public interface StoreChangeEvent {}
}
