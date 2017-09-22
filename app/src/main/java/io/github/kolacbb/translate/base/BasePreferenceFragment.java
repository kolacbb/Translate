package io.github.kolacbb.translate.base;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.View;

/**
 * Created by Kola on 2016/6/14.
 */
public abstract class BasePreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(getXMLId());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        afterCreate(savedInstanceState);
    }

    protected abstract int getXMLId();

    protected abstract void afterCreate(Bundle saveInstanceState);
}
