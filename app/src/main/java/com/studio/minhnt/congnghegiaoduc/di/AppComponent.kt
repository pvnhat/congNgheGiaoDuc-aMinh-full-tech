package com.studio.minhnt.congnghegiaoduc.di

import com.studio.minhnt.congnghegiaoduc.App
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [(AndroidSupportInjectionModule::class),
    (ActivityModule::class),
    (DatabaseModule::class),
    (ServiceModule::class),
    (ViewModelModule::class)])
interface AppComponent : AndroidInjector<App> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>()
}