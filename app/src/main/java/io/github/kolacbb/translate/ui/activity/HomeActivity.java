package io.github.kolacbb.translate.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import io.github.kolacbb.translate.R;
import io.github.kolacbb.translate.base.BaseActivity;
import io.github.kolacbb.translate.ui.fragment.TranslateMainFragment;

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
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragment = TranslateMainFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment, TranslateMainFragment.TAG);
        ft.commit();
    }



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.phrasebook:
//                PhrasebookActivity.start(this);
//                break;
//            case R.id.setting:
//                SettingsActivity.start(HomeActivity.this);
//                break;
//            case R.id.open_copy_translate:
////                Intent intent = new Intent(Intent.ACTION_SEND);
////                intent.putExtra(android.content.Intent.EXTRA_EMAIL, "mailto:555@qq.com");
////                intent.setType("text/plain");
////                //intent.setData(Uri.parse("mailto:555@qq.com"));
////                HomeActivity.this.startActivity(Intent.createChooser(intent, "选择方式"));
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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
