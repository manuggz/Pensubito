package com.pensubito.pensubito.di;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import android.app.Application;

import com.pensubito.pensubito.MyApplication;
import dagger.android.AndroidInjector;
import javax.inject.Singleton;

@Singleton
@Component(modules = { AndroidInjectionModule.class, MyApplicationModule.class,PensubitoDaoModule.class})
public interface MyApplicationComponent {
    @Component.Builder
    interface Builder {
        @dagger.BindsInstance Builder application(Application application);
        MyApplicationComponent build();
    }
    void inject(MyApplication myApplication);

}