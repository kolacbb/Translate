package io.github.kolacbb.translate.ui.activity;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_point)
    TextView pointTextView;
    @BindView(R.id.answer)
    TextView answerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_clear, R.id.bt_translate, R.id.add_book})
    public void widgetOnClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_clear:
                pointTextView.setText("");
                break;
            case R.id.bt_translate:
                //Toast.makeText(MainActivity.this, "translate", Toast.LENGTH_SHORT).show();
                if (pointTextView.getText().toString().trim().length() != 0)
                    queryTranslate(pointTextView.getText().toString());
                break;
            case R.id.add_book:
                String query = pointTextView.getText().toString().trim();
                String answer = answerTextView.getText().toString().trim();
                if (query.length() != 0 && answer.length() != 0) {


                    Toast.makeText(MainActivity.this, "已加入生词本", Toast.LENGTH_SHORT).show();
                }
                break;
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
                answerTextView.setText(response.body().getTranslation().get(0));
            }

            @Override
            public void onFailure(Call<YouDaoResult> call, Throwable t) {

            }
        });
    }


}
