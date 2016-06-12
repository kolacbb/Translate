package io.github.kolacbb.translate.flux.stores.base;

import com.squareup.otto.Bus;

import java.io.Serializable;

import io.github.kolacbb.translate.flux.actions.base.Action;

/**
 * Store基类,所有Store都需要继承此类,Store主要对View的状态进行管理,以及发送Store改变事件给View
 * Created by Kola on 2016/6/4.
 */
public abstract class Store implements Serializable{

    private static final Bus bus = new Bus();

    public StoreChangeEvent mChangeEvent;
    /**
     * 注册view
     * */
    public void register(final Object view) {
        bus.register(view);
    }

    /**
     * 接触view
     * */
    public void unregister(final Object view) {
        bus.unregister(view);
    }

    public void emitStoreChange() {
        bus.post(getChangeEvent());
    }

    public abstract StoreChangeEvent getChangeEvent();
    /**
     * 注册在Dispatcher里面的回调接口，当Dispatcher有数据派发过来的时候，可以在这里处理。
     * */
    public abstract void onAction(Action action);

    public interface StoreChangeEvent extends Serializable {}
}
