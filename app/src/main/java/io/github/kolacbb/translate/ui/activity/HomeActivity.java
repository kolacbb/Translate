package io.github.kolacbb.translate.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import io.github.kolacbb.translate.R;
import io.github.kolacbb.translate.base.BaseActivity;
import io.github.kolacbb.translate.db.TranslateDB;
import io.github.kolacbb.translate.ui.fragment.SettingsFragment;
import io.github.kolacbb.translate.ui.fragment.TranslateMainFragment;
import io.github.kolacbb.translate.util.SpUtil;

/**
 * Created by Kola on 2016/6/12.
 */
public class HomeActivity extends BaseActivity{

    public static void start(Context context, String query) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra("query", query);
        context.startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String query = intent.getStringExtra("query");
        getActionCreatorManager().getTranslateActionCreator().fetchTranslation(query);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        Fragment fragment = TranslateMainFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_container, fragment, TranslateMainFragment.TAG);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.phrasebook:
                PhrasebookActivity.start(this);
                break;
            case R.id.setting:
                SettingsActivity.start(HomeActivity.this);
                break;
            case R.id.open_copy_translate:
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.putExtra(android.content.Intent.EXTRA_EMAIL, "mailto:555@qq.com");
//                intent.setType("text/plain");
//                //intent.setData(Uri.parse("mailto:555@qq.com"));
//                HomeActivity.this.startActivity(Intent.createChooser(intent, "选择方式"));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}