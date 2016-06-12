package io.github.kolacbb.translate.inject.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.kolacbb.translate.flux.actions.creator.ActionCreatorManager;
import io.github.kolacbb.translate.flux.actions.creator.TranslateActionCreator;
import io.github.kolacbb.translate.flux.dispatcher.Dispatcher;
import io.github.kolacbb.translate.service.base.DataLayer;

/**
 * Created by Kola on 2016/6/12.
 */
@Module
public class FluxModule {

    /**
     * 提供分发器单例对象
     * */
    @Singleton
    @Provides
    public Dispatcher provideDispatcher() {
        return Dispatcher.get();
    }

    /**
     * 提供TranslateCreator单例对象
     * */
    @Singleton
    @Provides
    public TranslateActionCreator provideTranslateActionCreator(Dispatcher dispatcher, DataLayer dataLayer) {
        return new TranslateActionCreator(dispatcher, dataLayer);
    }

    /**
     * 提供ActionCreatorManager单例对象
     * */
    @Singleton
    @Provides
    public ActionCreatorManager provideActionCreatorManager(TranslateActionCreator translateActionCreator) {
        return new ActionCreatorManager(translateActionCreator);
    }
}
