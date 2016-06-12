package io.github.kolacbb.translate.flux.actions.creator.base;

import io.github.kolacbb.translate.flux.dispatcher.Dispatcher;
import io.github.kolacbb.translate.service.base.DataLayer;

/**
 * Created by Kola on 2016/6/12.
 */
public abstract class BaseActionCreator {
    public Dispatcher dispatcher;
    public  DataLayer dataLayer;

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
