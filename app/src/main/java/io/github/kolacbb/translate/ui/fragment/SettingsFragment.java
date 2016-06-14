package io.github.kolacbb.translate.ui.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import io.github.kolacbb.translate.R;

/**
 * Created by Kola on 2016/6/13.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preference from an xml resource
        addPreferencesFromResource(R.xml.preferences);
    }
}
