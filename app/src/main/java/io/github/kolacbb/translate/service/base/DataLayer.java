package io.github.kolacbb.translate.service.base;


import io.github.kolacbb.translate.model.entity.Result;
import io.github.kolacbb.translate.model.entity.YouDaoResult;
import rx.Observable;

/**
 * 获取应用中所有的Observable
 * Created by Kola on 2016/6/6.
 */
public class DataLayer {
    TranslateService translateService;

    public DataLayer(TranslateService translateService) {
        this.translateService = translateService;
    }

    public TranslateService getTranslateService() {
        return translateService;
    }

    public interface TranslateService {

        /**
         * 获取翻译结果
         * */
        Observable<YouDaoResult> getTranslation(String query);

        /**
         * 获取本地翻译结果
         * */
        Observable<Result> getLocalTranslation(String query);


    }
}
