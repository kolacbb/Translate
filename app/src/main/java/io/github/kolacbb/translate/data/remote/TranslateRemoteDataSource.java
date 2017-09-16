package io.github.kolacbb.translate.data.remote;

import io.github.kolacbb.translate.data.TranslateDataSource;
import io.github.kolacbb.translate.data.local.TranslateDB;
import io.github.kolacbb.translate.model.entity.Result;
import io.github.kolacbb.translate.model.entity.YouDaoResult;
import io.github.kolacbb.translate.net.RetrofitManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by zhangd on 2017/9/14.
 */

public class TranslateRemoteDataSource implements TranslateDataSource {
    @Override
    public void getTranslation(String message, final LoadTranslationCallback callback) {
        Retrofit retrofit = RetrofitManager.getRetrofit();
        TranslateApi api = retrofit.create(TranslateApi.class);

        Call<YouDaoResult> call = api.getTranslationYouDao(message);

        call.enqueue(new Callback<YouDaoResult>() {
            @Override
            public void onResponse(Call<YouDaoResult> call, Response<YouDaoResult> response) {
                Result result = response.body().getResult();
                TranslateDB.getInstance().saveToHistory(result);
                callback.onTranslationLoaded(result);
            }

            @Override
            public void onFailure(Call<YouDaoResult> call, Throwable t) {
                callback.onTranslationLoaded(null);
            }
        });


    }
}
