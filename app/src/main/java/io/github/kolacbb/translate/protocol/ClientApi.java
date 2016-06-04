package io.github.kolacbb.translate.protocol;

import io.github.kolacbb.translate.model.entity.YouDaoResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Kola on 2016/6/4.
 */
public interface ClientApi {
    //http://fanyi.youdao.com/openapi.do?keyfrom=Transalte&key=1238484380&type=data&doctype=json&version=1.1&q=要翻译的文本
    @GET("openapi.do")
    Call<YouDaoResult> getTranslationYouDao(@Query("keyfrom") String keyfrom,
                                            @Query("key") String key,
                                            @Query("type") String type,
                                            @Query("doctype") String doctype,
                                            @Query("version") String version,
                                            @Query("q") String q);
}
