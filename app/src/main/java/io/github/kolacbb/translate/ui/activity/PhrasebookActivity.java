package io.github.kolacbb.translate.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import io.github.kolacbb.translate.R;
import io.github.kolacbb.translate.base.BaseActivity;
import io.github.kolacbb.translate.base.BaseFragment;
import io.github.kolacbb.translate.ui.fragment.PhrasebookFragment;
import io.github.kolacbb.translate.mvp.view.TranslateFragment;

public class PhrasebookActivity extends BaseActivity {

    BaseFragment mPhrasebookFragment;

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
        mPhrasebookFragment = PhrasebookFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_container, mPhrasebookFragment, TranslateFragment.TAG);
        ft.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!mPhrasebookFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }
}
