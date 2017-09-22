package io.github.kolacbb.translate.base;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.View;

import javax.inject.Inject;

import io.github.kolacbb.translate.R;
import io.github.kolacbb.translate.flux.actions.creator.ActionCreatorManager;
import io.github.kolacbb.translate.flux.dispatcher.Dispatcher;
import io.github.kolacbb.translate.flux.stores.base.Store;
import io.github.kolacbb.translate.inject.component.ApplicationComponent;

/**
 * Created by Kola on 2016/6/14.
 */
public abstract class BasePreferenceFragment extends PreferenceFragment {

    @Inject
    Dispatcher dispatcher;
    @Inject
    ActionCreatorManager actionCreatorManager;

    public BasePreferenceFragment() {
        ApplicationComponent.Instance.get().inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preference from an xml resource
        addPreferencesFromResource(getXMLId());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        getDispatcher().register(getStore());
//        getStore().register(this);
        afterCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        getDispatcher().unregister(getStore());
//        getStore().unregister(this);
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    public ActionCreatorManager getActionCreatorManager() {
        return actionCreatorManager;
    }

    protected abstract int getXMLId();

    //protected abstract Store getStore();

    protected abstract void afterCreate(Bundle saveInstanceState);
}
