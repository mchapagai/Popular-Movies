package com.example.mchapagai.injection;

import com.example.mchapagai.common.BaseApplication;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(
        modules = {
                AndroidSupportInjectionModule.class,
                AppModule.class,
                ViewsModule.class
        }
)
interface AppComponent extends AndroidInjector<BaseApplication> {
    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<BaseApplication> {
    }
}