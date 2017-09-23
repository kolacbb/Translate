package io.github.kolacbb.translate.data.remote;

import android.content.Context;

import java.util.List;

import io.github.kolacbb.translate.data.TranslateDataSource;
import io.github.kolacbb.translate.data.entity.SmsEntry;
import io.github.kolacbb.translate.data.entity.Translate;
import io.github.kolacbb.translate.data.entity.YouDaoResult;
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
    public void getTranslate(String query, final String source, final GetTranslateCallback callback) {
        Retrofit retrofit = RetrofitManager.getRetrofit();
        TranslateApi api = retrofit.create(TranslateApi.class);

        Call<YouDaoResult> call = api.getTranslationYouDao(query);

        call.enqueue(new Callback<YouDaoResult>() {
            @Override
            public void onResponse(Call<YouDaoResult> call, Response<YouDaoResult> response) {
                Translate t = response.body().getTranslate();
                t.setSource(source);
                callback.onTranslateLoaded(t);
            }

            @Override
            public void onFailure(Call<YouDaoResult> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public List<Translate> getPhrasebook() {
        return null;
    }

    @Override
    public void addPhrasebook(Translate translate) {

    }

    @Override
    public void addHistory(Translate translate) {

    }

    @Override
    public void deletePhrasebook(Translate translate) {

    }

    @Override
    public void deletePhrasebooks(List<Translate> translates) {

    }

    @Override
    public List<Translate> getHistory() {
        return null;
    }

    @Override
    public void deleteAllHistory() {

    }

    @Override
    public void deleteHistory(Translate translate) {

    }

    @Override
    public List<SmsEntry> getSms(Context context) {
        return null;
    }

    @Override
    public void clearHistory() {

    }
}
