package com.pensubito.pensubito.di;

import android.app.Application;
import android.arch.persistence.room.Room;

import javax.inject.Singleton;
import com.pensubito.pensubito.db.PensubitoDb;
import com.pensubito.pensubito.db.PensubitoDao;
import dagger.Module;
import dagger.Provides;

@Singleton
@Module
public class PensubitoDaoModule {

    @Singleton
    @Provides
    PensubitoDb provideDb(Application app) {
        return Room.databaseBuilder(app, PensubitoDb.class,"pensubito.db").build();
    }

    @Singleton @Provides
    PensubitoDao providePensubitoDao(PensubitoDb db) {
        return db.getPensubitoDao();
    }
}
