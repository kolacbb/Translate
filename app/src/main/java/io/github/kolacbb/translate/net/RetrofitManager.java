package io.github.kolacbb.translate.net;

import io.github.kolacbb.translate.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhangd on 2017/9/14.
 */

public class RetrofitManager {
    private static Retrofit sRetrofit;
    private static Retrofit sBaiduRetorfit;
    private static OkHttpClient sOkHttpClient;

    static {

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        //DEBUG模式下 添加日志拦截器
        if(BuildConfig.DEBUG){
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClientBuilder.addInterceptor(interceptor);
        }
        sOkHttpClient = httpClientBuilder.build();

        sRetrofit = new Retrofit.Builder()
                .baseUrl("http://fanyi.youdao.com")
                .client(sOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        sBaiduRetorfit = new Retrofit.Builder()
                .baseUrl("http://fanyi.youdao.com")
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static OkHttpClient getOkHttpClient() {
        return sOkHttpClient;
    }

    public static Retrofit getRetrofit() {
        return sRetrofit;
    }

    public static Retrofit getBaiduRetorfit() {
        return sBaiduRetorfit;
    }
}
