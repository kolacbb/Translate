package io.github.kolacbb.translate.protocol;

import io.github.kolacbb.translate.model.entity.Result;
import io.github.kolacbb.translate.model.entity.YouDaoResult;
import rx.Observable;

/**
 * Created by Kola on 2016/6/11.
 */
public class TranslateManager extends BaseManager implements DataLayer.TranslateService {
    @Override
    public Observable<YouDaoResult> getTranslation(String query) {
        return getApi().getTranslationYouDao(ApiKey.YOUDAO_KEY_FROM,
                ApiKey.YOUDAO_KEY,
                ApiKey.YOUDAO_TYPE,
                ApiKey.YOUDAO_DOCTYPE,
                ApiKey.YOUDAO_VERSION,
                query);
    }

    @Override
    public Observable<Result> getLocalTranslation(String query) {
        return null;
    }
}
