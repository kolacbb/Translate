package io.github.kolacbb.translate.data.remote;

import io.github.kolacbb.translate.data.entity.YouDaoResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zhangd on 2017/9/14.
 */

public interface TranslateApi {
    //http://fanyi.youdao.com/openapi.do?keyfrom=Transalte&key=1238484380&type=data&doctype=json&version=1.1&q=要翻译的文本
    @GET("openapi.do?keyfrom=Transalte&key=1238484380&type=data&doctype=json&version=1.1")
    Call<YouDaoResult> getTranslationYouDao(@Query("q") String q);
}
