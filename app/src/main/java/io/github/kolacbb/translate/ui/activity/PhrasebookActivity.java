package io.github.kolacbb.translate.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import io.github.kolacbb.translate.R;
import io.github.kolacbb.translate.base.BaseActivity;
import io.github.kolacbb.translate.ui.fragment.PhrasebookFragment;
import io.github.kolacbb.translate.ui.fragment.TranslateMainFragment;

public class PhrasebookActivity extends BaseActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, PhrasebookActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_phrasebook;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        Fragment fragment = PhrasebookFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_container, fragment, TranslateMainFragment.TAG);
        ft.commit();
    }

}
