package io.github.kolacbb.translate.inject.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.kolacbb.translate.service.TranslateManager;
import io.github.kolacbb.translate.service.base.DataLayer;

/**
 * Created by Kola on 2016/6/11.
 */
@Module
public class DataLayerModule {

    @Singleton
    @Provides
    public TranslateManager provideTranslateManager() {
        return new TranslateManager();
    }

    @Singleton
    @Provides
    public DataLayer provideDataLayer(TranslateManager translateManager) {
        return new DataLayer(translateManager);
    }

}
