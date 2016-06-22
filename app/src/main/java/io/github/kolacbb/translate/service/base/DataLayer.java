package io.github.kolacbb.translate.service.base;


import java.util.List;

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
        Observable<Result> getTranslation(String query);

        /**
         * 获取本地翻译结果
         * */
        Observable<Result> getLocalTranslation(String query);

        /**
         * 获取所有History记录
         * */
        Observable<List<Result>> getAllHistory();

        /**
         * 将翻译结果加入数据库（History）
         */
        void saveToHistory(Result result);

        /**
         * 向Phrasebook 添加记录
         * */
        void saveToPhrasebook(Result result);
    }
}
