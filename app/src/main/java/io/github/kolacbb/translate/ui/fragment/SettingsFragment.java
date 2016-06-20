package io.github.kolacbb.translate.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.View;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import io.github.kolacbb.translate.R;
import io.github.kolacbb.translate.base.BasePreferenceFragment;
import io.github.kolacbb.translate.component.service.ClipboardListenerService;
import io.github.kolacbb.translate.flux.stores.SettingsStore;
import io.github.kolacbb.translate.flux.stores.base.Store;

/**
 * Created by Kola on 2016/6/13.
 */
public class SettingsFragment extends BasePreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener{

    SettingsStore settingsStore;

    public static String BUNDLE_KEY = "settings_fragment";

    // 清除单词搜索历史纪录
    public static final String KEY_CLEAR_HISTORY = "pref_key_clear_history";
    // 复制查词功能（check box）
    public static final String KEY_TAP_TO_TRANSLATE = "pref_key_tap_to_translate";
    // 音标显示 （list）
    public static final String KEY_PHONETIC_LIST = "pref_key_phonetic_list";
    // 复制查词结果显示方式(list)
    public static final String KEY_SHOW_TRANSLATE_WAYS = "pref_key_show_translate_ways";

    @Override
    protected int getXMLId() {
        return R.xml.preferences;
    }

    @Override
    protected Store getStore() {
        if (settingsStore == null) {
            settingsStore = new SettingsStore();
        }
        return settingsStore;
    }

    @Override
    protected void afterCreate(Bundle saveInstanceState) {
        if (saveInstanceState != null) {
            settingsStore = (SettingsStore) saveInstanceState.getSerializable(BUNDLE_KEY);
        } else if (settingsStore == null) {
            settingsStore = new SettingsStore();
        }

        findPreference(KEY_CLEAR_HISTORY).setOnPreferenceClickListener(this);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(BUNDLE_KEY, settingsStore);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Subscribe
    public void onStoreChange(SettingsStore.SettingsStoreChangeEvent event) {
        render();
    }

    public void render() {

    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(KEY_TAP_TO_TRANSLATE)) {
            // 处理CopyTap选项，开启或者关闭Tap to translate 功能
            CheckBoxPreference checkBoxPref = (CheckBoxPreference) findPreference(key);
            if (checkBoxPref.isChecked()) {
                getActivity().startService(new Intent(getActivity(), ClipboardListenerService.class));
                Toast.makeText(getActivity(), "turn on tap to translate", Toast.LENGTH_SHORT).show();
            } else {
                getActivity().stopService(new Intent(getActivity(), ClipboardListenerService.class));
                Toast.makeText(getActivity(), "turn off tap to translate", Toast.LENGTH_SHORT).show();
            }
        } else if (key.equals(KEY_PHONETIC_LIST)) {
            Preference pref = findPreference(key);
            pref.setSummary(sharedPreferences.getString(key, ""));
        } else if (key.equals(KEY_SHOW_TRANSLATE_WAYS)) {
            Preference pref = findPreference(key);
            pref.setSummary(sharedPreferences.getString(key, ""));
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals(KEY_CLEAR_HISTORY)) {
            showClearHistoryDialog();
            return true;
        }

        return false;
    }

    private void showClearHistoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Clear history")
                .setMessage("Clear translation history?")
                .setNegativeButton("NO", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActionCreatorManager().getTranslateActionCreator().clearHistory();
                    }
                });
        //显示Dialog
        builder.create().show();
    }


}
