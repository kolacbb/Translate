package io.github.kolacbb.translate.inject.modules;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Kola on 2016/6/11.
 */
@Module(includes = {DataLayerModule.class, ClientApiModel.class, FluxModule.class})
public class ApplicationModule {
    Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    /**
     * 提供Application单例对象
     * */

    @Singleton
    @Provides
    public Application provideApplication() {
        return application;
    }
}
