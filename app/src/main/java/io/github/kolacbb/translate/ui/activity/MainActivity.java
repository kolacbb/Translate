package io.github.kolacbb.translate.ui.activity;


import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.kolacbb.translate.R;
import io.github.kolacbb.translate.flux.actions.ActionCreator;
import io.github.kolacbb.translate.flux.dispatcher.Dispatcher;
import io.github.kolacbb.translate.flux.stores.MainStore;
import io.github.kolacbb.translate.model.entity.Result;
import io.github.kolacbb.translate.model.entity.YouDaoResult;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_point)
    TextView pointTextView;
    @BindView(R.id.phonetic)
    TextView tvPhonetic;

    @BindView(R.id.error_view)
    View errorView;
    @BindView(R.id.history_rec_view)
    RecyclerView historyRecView;
    @BindView(R.id.progress_bar)
    ContentLoadingProgressBar progressBar;
    @BindView(R.id.translate_view)
    ScrollView translateView;
    @BindView(R.id.translation)
    TextView translation;
    @BindView(R.id.dictionary_view)
    CardView dictionaryView;
    @BindView(R.id.basic)
    TextView basicTextView;

    Dispatcher dispatcher;
    MainStore mainStore;
    ActionCreator actionCreator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initDependencies();
        render(mainStore);
    }

    private void initDependencies() {
        dispatcher = Dispatcher.get();
        actionCreator = ActionCreator.get(dispatcher);
        mainStore = new MainStore();
        dispatcher.register(mainStore);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainStore.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mainStore.unregister(this);
    }

    @SuppressWarnings("ResourceType")
    public void render(MainStore store) {
        historyRecView.setVisibility(store.getHistoryRecycleViewVisiableState());
        if (store.isLoading()) {
            System.out.println("Loading");
            progressBar.show();
        } else {
            progressBar.hide();
        }
        //progressBar.setVisibility(store.getLoadingViewVisiableState());
        translateView.setVisibility(store.getTranslateViewVisiableState());
        errorView.setVisibility(store.getErrorViewState());

        if (store.isFinish() && store.getData() != null) {
            Result result = store.getData();
            translation.setText(result.getTranslation());
            if (result.getUk_phonetic() != null) {
                tvPhonetic.setText(result.getUk_phonetic());
                dictionaryView.setVisibility(View.VISIBLE);
            } else {
                tvPhonetic.setVisibility(View.GONE);
            }
            if (result.getBasic() != null) {
                basicTextView.setText(result.getBasic());
                dictionaryView.setVisibility(View.VISIBLE);
            } else {
                dictionaryView.setVisibility(View.GONE);
            }
//            YouDaoResult youDaoResult = store.getData();
//            translation.setText(youDaoResult.getTranslation().get(0));
//            YouDaoResult.Basic basic = youDaoResult.getBasic();
//            if (basic != null) {
//                dictionaryView.setVisibility(View.VISIBLE);
//                //发音设置
//                if (basic.getPhonetic() != null) {
//                    tvPhonetic.setText(basic.getUsPhonetic());
//                    tvPhonetic.setVisibility(View.VISIBLE);
//                } else {
//                    tvPhonetic.setVisibility(View.GONE);
//                }
//
//                StringBuilder stringBuilder = new StringBuilder();
//                for (String str : basic.getExplains()) {
//                    stringBuilder.append(str);
//                    stringBuilder.append("\n");
//                }
//                basicTextView.setText(stringBuilder.toString());
//            } else {
//                dictionaryView.setVisibility(View.GONE);
//                tvPhonetic.setVisibility(View.GONE);
//            }
        }

    }

    @Subscribe
    public void onStoreChange(MainStore.MainStoreChangeEvent event) {
        render(mainStore);
    }

    @OnClick({R.id.tv_clear, R.id.bt_translate, R.id.add_book})
    public void widgetOnClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_clear:
                pointTextView.setText("");
                actionCreator.initView();
                break;
            case R.id.bt_translate:
                //Toast.makeText(MainActivity.this, "translate", Toast.LENGTH_SHORT).show();
                if (pointTextView.getText().toString().trim().length() != 0)
                    actionCreator.fetchTranslation(pointTextView.getText().toString());
                break;
            case R.id.add_book:
                String query = pointTextView.getText().toString().trim();
                String answer = basicTextView.getText().toString().trim();
                if (query.length() != 0 && answer.length() != 0) {


                    Toast.makeText(MainActivity.this, "已加入生词本", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


}
