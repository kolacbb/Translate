package io.github.kolacbb.translate.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import io.github.kolacbb.translate.R;
import io.github.kolacbb.translate.flux.actions.creator.ActionCreatorManager;
import io.github.kolacbb.translate.flux.dispatcher.Dispatcher;
import io.github.kolacbb.translate.inject.component.ApplicationComponent;
import io.github.kolacbb.translate.service.base.DataLayer;

/**
 * Created by Kola on 2016/6/5.
 */
public abstract class BaseActivity extends AppCompatActivity{
    @Inject
    Dispatcher dispatcher;
    @Inject
    DataLayer dataLayer;
    @Inject
    ActionCreatorManager actionCreatorManager;

    public BaseActivity() {
//        dispatcher = Dispatcher.get();
        ApplicationComponent.Instance.get().inject(this);
    }

    public ActionCreatorManager getActionCreatorManager() {
        return actionCreatorManager;
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    protected abstract int getLayoutId();

    protected abstract void afterCreate(Bundle savedInstanceState);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        afterCreate(savedInstanceState);
    }
}
