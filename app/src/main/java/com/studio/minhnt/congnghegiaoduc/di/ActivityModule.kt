package com.studio.minhnt.congnghegiaoduc.di

import com.studio.minhnt.congnghegiaoduc.ui.MainActivity
import com.studio.minhnt.congnghegiaoduc.ui.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun provideMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun provideSplashActivity(): SplashActivity
}