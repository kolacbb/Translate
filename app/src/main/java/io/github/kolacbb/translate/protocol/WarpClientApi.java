package io.github.kolacbb.translate.protocol;

import io.github.kolacbb.translate.model.entity.YouDaoResult;
import rx.Observable;

/**
 * Created by Kola on 2016/6/5.
 */
public class WarpClientApi {
    static ClientApi mClientApi;

    public WarpClientApi(ClientApi clientApi) {
        mClientApi = clientApi;
    }

    public Observable<YouDaoResult> translate(String word) {
       return mClientApi.getTranslationYouDao(ApiKey.YOUDAO_KEY_FROM,
                ApiKey.YOUDAO_KEY,
                ApiKey.YOUDAO_TYPE,
                ApiKey.YOUDAO_DOCTYPE,
                ApiKey.YOUDAO_VERSION,
                word);

    }

}
