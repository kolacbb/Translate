package io.github.kolacbb.translate.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import io.github.kolacbb.translate.flux.dispatcher.Dispatcher;

/**
 * Created by Kola on 2016/6/5.
 */
public abstract class BaseActivity extends AppCompatActivity{
    Dispatcher dispatcher;


    public BaseActivity() {
        dispatcher = Dispatcher.get();
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    protected abstract int getLayoutId();

    protected abstract void afterCreate(Bundle savedInstanceState);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        afterCreate(savedInstanceState);
    }
}
