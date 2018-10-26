package com.studio.minhnt.congnghegiaoduc.di

import android.arch.persistence.room.Room
import com.studio.minhnt.congnghegiaoduc.db.AppDatabase
import com.studio.minhnt.congnghegiaoduc.db.MessageDao
import dagger.Module
import dagger.Provides
import com.studio.minhnt.congnghegiaoduc.App
import javax.inject.Singleton

@Module
internal class DatabaseModule {
    @Singleton
    @Provides
    internal fun provideAppDb(app: App): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "app.db").fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    internal fun provideMessageDao(db: AppDatabase): MessageDao {
        return db.messageDao()
    }
}
