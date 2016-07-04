package io.github.kolacbb.translate.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.ButterKnife;
import io.github.kolacbb.translate.flux.actions.creator.ActionCreatorManager;
import io.github.kolacbb.translate.flux.dispatcher.Dispatcher;
import io.github.kolacbb.translate.flux.stores.base.Store;
import io.github.kolacbb.translate.inject.component.ApplicationComponent;

/**
 * Created by Kola on 2016/6/12.
 */
public abstract class BaseFragment extends Fragment {
    public static final String TAG = BaseFragment.class.getSimpleName();
    protected View mRootView;

    @Inject
    Dispatcher dispatcher;
    @Inject
    ActionCreatorManager actionCreatorManager;

    public BaseFragment() {
        ApplicationComponent.Instance.get().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutId(), container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        // 订阅View
        getStore().register(this);
        getDispatcher().register(getStore());//订阅Action
        afterCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //取消订阅Action
        getDispatcher().unregister(getStore());
        //取消订阅View
        getStore().unregister(this);

    }

    public boolean onBackPressed() {
        return false;
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    public ActionCreatorManager getActionCreatorManager() {
        return actionCreatorManager;
    }

    protected abstract int getLayoutId();

    protected abstract Store getStore();

    protected abstract void afterCreate(Bundle saveInstanceState);
}
