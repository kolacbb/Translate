package io.github.kolacbb.translate.inject.component;

import android.app.Application;
import android.support.annotation.Nullable;

import javax.inject.Singleton;

import dagger.Component;
import io.github.kolacbb.translate.base.BaseActivity;
import io.github.kolacbb.translate.base.BaseFragment;
import io.github.kolacbb.translate.base.BasePreferenceFragment;
import io.github.kolacbb.translate.component.service.ClipboardListenerService;
import io.github.kolacbb.translate.inject.modules.ApplicationModule;
import io.github.kolacbb.translate.service.base.BaseManager;
import io.github.kolacbb.translate.ui.activity.CopyDropActivity;

/**
 * Created by Kola on 2016/6/11.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(BaseActivity activity);

    void inject(BaseManager manager);

    void inject(BaseFragment fragment);

    void inject(BasePreferenceFragment preferenceFragment);

    void inject(ClipboardListenerService service);

    Application getApplication();

    class Instance {
        private static ApplicationComponent sComponent;

        public static void init(@Nullable ApplicationComponent component) {
            sComponent = component;
        }

        public static ApplicationComponent get() {
            return sComponent;
        }
    }
}
