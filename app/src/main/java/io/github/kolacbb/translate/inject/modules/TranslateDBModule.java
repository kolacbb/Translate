package io.github.kolacbb.translate.inject.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.kolacbb.translate.data.local.TranslateDB;
import io.github.kolacbb.translate.inject.component.ApplicationComponent;

/**
 * Created by Kola on 2016/6/21.
 */
@Module
public class TranslateDBModule {

    /**
     * 创建单例一个TranslateDB对象
     **/
    @Provides
    @Singleton
    public static TranslateDB provideTranslateDB() {
        TranslateDB.init(ApplicationComponent.Instance.get().getApplication());
        return TranslateDB.getInstance();
    }
}
