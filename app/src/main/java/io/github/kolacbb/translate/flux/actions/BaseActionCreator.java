package io.github.kolacbb.translate.flux.actions;

import io.github.kolacbb.translate.flux.dispatcher.Dispatcher;
import io.github.kolacbb.translate.protocol.DataLayer;

/**
 * Created by Kola on 2016/6/12.
 */
public abstract class BaseActionCreator {
    Dispatcher dispatcher;
    DataLayer dataLayer;

    public BaseActionCreator(Dispatcher dispatcher, DataLayer dataLayer) {
        this.dispatcher = dispatcher;
        this.dataLayer = dataLayer;
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    public DataLayer getDataLayer() {
        return dataLayer;
    }
}
