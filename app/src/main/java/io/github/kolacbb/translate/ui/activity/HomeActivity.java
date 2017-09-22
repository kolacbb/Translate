package io.github.kolacbb.translate.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import io.github.kolacbb.translate.R;
import io.github.kolacbb.translate.base.BaseActivity;
import io.github.kolacbb.translate.base.BaseFragment;
import io.github.kolacbb.translate.data.TranslateRepository;
import io.github.kolacbb.translate.mvp.presenter.TranslatePresenter;
import io.github.kolacbb.translate.mvp.view.TranslateFragment;

/**
 * Created by Kola on 2016/6/12.
 */
public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    TranslateFragment mTranslateFragment;

    public static void start(Context context, String query) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra("query", query);
        context.startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String query = intent.getStringExtra("query");
        if (query != null && query.trim().length() != 0) {
            if (mTranslateFragment != null && mTranslateFragment.isAdded()) {
                mTranslateFragment.onNewTranslate(query);
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        mTranslateFragment = TranslateFragment.newInstance();
        TranslatePresenter presenter = new TranslatePresenter(TranslateRepository.getInstance(), mTranslateFragment);
        mTranslateFragment.setPresenter(presenter);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, mTranslateFragment, TranslateFragment.TAG);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        // 分发返回键点击事件
        if (!mTranslateFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_phrasebook) {
            PhrasebookActivity.start(HomeActivity.this);
        } else if (id == R.id.nav_sms) {
            SmsInputActivity.start(HomeActivity.this);
        } else if (id == R.id.nav_setting) {
            SettingsActivity.start(HomeActivity.this);
        }
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }
}
