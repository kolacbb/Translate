package io.github.kolacbb.translate.inject.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.kolacbb.translate.protocol.ClientApi;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 * Created by Kola on 2016/6/5.
 */
@Module
public class ClientApiModel {
    private static final String BASE_URL = "http://fanyi.youdao.com";


    /**
     * 创建一个ClientAPI的实现类单例对象
     *
     * */
    @Provides
    @Singleton
    public static ClientApi provideClientApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ClientApi.class);
    }
}
