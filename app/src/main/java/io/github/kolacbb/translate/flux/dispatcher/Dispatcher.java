package io.github.kolacbb.translate.flux.dispatcher;

import java.util.ArrayList;
import java.util.List;

import io.github.kolacbb.translate.flux.actions.base.Action;
import io.github.kolacbb.translate.flux.stores.base.Store;

/**
 * Created by Kola on 2016/6/4.
 */
public class Dispatcher {
    private static Dispatcher instance;
    private final List<Store> stores = new ArrayList<>();
    public static Dispatcher get() {
        if (instance == null) {
            instance = new Dispatcher();
        }
        return instance;
    }

    Dispatcher() {
    }

    /**
     * 注册每个Store的回调接口
     * */
    public void register(final Store store) {
        stores.add(store);
    }

    /**
     * 解除Store的回调接口
     * */
    public void unregister(final Store store) {
        stores.remove(store);
    }

    /**
     * 触发Store的回调接口
     * */
    public void dispatch(Action action) {
        post(action);
    }

    private void post(final Action action) {
        for (Store store : stores) {
            store.onAction(action);
        }
    }
}
