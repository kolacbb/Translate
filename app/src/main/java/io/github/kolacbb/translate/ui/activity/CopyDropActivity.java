package io.github.kolacbb.translate.ui.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.kolacbb.translate.R;
import io.github.kolacbb.translate.model.entity.YouDaoResult;
import io.github.kolacbb.translate.protocol.ApiKey;
import io.github.kolacbb.translate.protocol.ClientApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CopyDropActivity extends Activity {
    private CharSequence text; //Selection Text
    private boolean readOnly; //is the Selection Text can change


    @BindView(R.id.query)
    TextView queryTextView;
    @BindView(R.id.translation)
    TextView translationTextView;

    @BindView(R.id.replace_btn)
    Button replaceButton;

    @BindView(R.id.container)
    ViewGroup mContainerView;
    @BindView(R.id.error_view)
    View errorView;

    PopupMenu popupMenu = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy_drop);


        WindowManager wm = this.getWindowManager();

        int width = wm.getDefaultDisplay().getWidth();

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        //lp.
        //lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.width =(int) ( width * 0.9);
        lp.verticalMargin = 0.02F;
        lp.gravity = Gravity.TOP;
        getWindow().setAttributes(lp);

        ButterKnife.bind(this);


        translationTextView.setMovementMethod(new ScrollingMovementMethod());

        handleIntent(getIntent());
    }


    public void handleIntent(Intent intent) {
        //Intent intent = getIntent();
        text = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
        readOnly = intent.getBooleanExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, false);
        replaceButton.setClickable(!readOnly);
        if (text != null) {
            queryTextView.setText(text.toString());
            queryTranslate(text.toString());
            Toast.makeText(CopyDropActivity.this, text, Toast.LENGTH_SHORT).show();
        }
    }

    public void queryTranslate(String text){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fanyi.youdao.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ClientApi clientApi = retrofit.create(ClientApi.class);

        Call<YouDaoResult> call = clientApi.getTranslationYouDao(ApiKey.YOUDAO_KEY_FROM,
                ApiKey.YOUDAO_KEY,
                ApiKey.YOUDAO_TYPE,
                ApiKey.YOUDAO_DOCTYPE,
                ApiKey.YOUDAO_VERSION,
                text);
        call.enqueue(new Callback<YouDaoResult>() {
            @Override
            public void onResponse(Call<YouDaoResult> call, Response<YouDaoResult> response) {
                translationTextView.setText(response.body().getTranslation().get(0));
            }

            @Override
            public void onFailure(Call<YouDaoResult> call, Throwable t) {

            }
        });
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    public void setCallBackResult(String replacementText) {
        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_PROCESS_TEXT, replacementText);
        setResult(RESULT_OK, intent);
        finish();
    }
    @OnClick({R.id.close_btn, R.id.replace_btn, R.id.more_menu})
    public void widgetOnClicked(View view) {
        switch (view.getId()) {
            case R.id.close_btn:
                CopyDropActivity.this.finish();
                break;
            case R.id.replace_btn:
                setCallBackResult(translationTextView.getText().toString());
                break;
            case R.id.more_menu:
                if (popupMenu == null) {
                    initPopupMenu(view);
                } else {
                    popupMenu.show();
                }
                break;
        }
    }

    private void initPopupMenu(View view) {
        popupMenu = new PopupMenu(this, view);
        getMenuInflater().inflate(R.menu.copy_drop_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.copy:
                        ClipboardManager clipboard = (ClipboardManager)
                                getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("simple text",translationTextView.getText().toString());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(CopyDropActivity.this, "Translation copied, paste to enter", Toast.LENGTH_SHORT).show();
                        popupMenu.dismiss();
                        finish();
                        break;
                    case R.id.open_application:
                        Toast.makeText(CopyDropActivity.this, "open", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }
}
