package io.github.kolacbb.translate.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.github.kolacbb.translate.R;
import io.github.kolacbb.translate.base.BaseActivity;
import io.github.kolacbb.translate.flux.stores.SmsInputStore;
import io.github.kolacbb.translate.model.entity.SmsEntry;
import io.github.kolacbb.translate.ui.adapter.SmsAdapter;

/**
 * Created by Kola on 2016/6/26.
 */
public class SmsInputActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(android.R.id.list)
    ListView listView;
    @BindView(android.R.id.text1)
    TextView textView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    SmsAdapter smsAdapter;

    SmsInputStore store;

    public static void start(Context context) {
        Intent intent = new Intent(context, SmsInputActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sms_input;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        store = new SmsInputStore();
        smsAdapter = new SmsAdapter(this, new ArrayList<SmsEntry>());
        listView.setAdapter(smsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SmsEntry sms = (SmsEntry) smsAdapter.getItem(position);
                HomeActivity.start(SmsInputActivity.this, sms.getContent());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDispatcher().register(store);
        store.register(this);
        getActionCreatorManager().getTranslateActionCreator().fetchSmsList();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getDispatcher().unregister(store);
        store.unregister(this);
    }

    public void render() {
        List<SmsEntry> list = store.getList();
        progressBar.setVisibility(View.GONE);
        if (list != null && list.size() != 0) {
            smsAdapter.setData(list);
            listView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe
    public void onStoreChanged(SmsInputStore.SmsInputStoreChangeEventStore enent) {
        System.out.println("Activity Event");
        render();
    }
}
