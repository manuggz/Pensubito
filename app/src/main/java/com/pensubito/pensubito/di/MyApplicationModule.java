package com.pensubito.pensubito.di;

import android.app.Application;
import android.arch.persistence.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;
import com.pensubito.pensubito.MainActivity;
import com.pensubito.pensubito.TrimestreAddActivity;
import com.pensubito.pensubito.TrimestreDetailActivity;
import com.pensubito.pensubito.TrimestreListActivity;
import com.pensubito.pensubito.TrimestreMateriaAddActivity;

@Module(includes = ViewModelModule.class)
public abstract class MyApplicationModule {

    @ContributesAndroidInjector
    abstract MainActivity contributeActivityInjector();
    @ContributesAndroidInjector
    abstract TrimestreListActivity contributeTrimestreListActivity();
    @ContributesAndroidInjector
    abstract TrimestreAddActivity contributeTrimestreAddActivity();
    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract TrimestreDetailActivity contributeTrimestreDetailActivity();
    @ContributesAndroidInjector
    abstract TrimestreMateriaAddActivity contributeTrimestreMateriaAddActivity();
}