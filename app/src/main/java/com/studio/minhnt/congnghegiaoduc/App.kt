package com.studio.minhnt.congnghegiaoduc

import android.annotation.SuppressLint
import android.support.multidex.MultiDex
import com.google.android.gms.ads.MobileAds
import com.studio.minhnt.congnghegiaoduc.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

@SuppressLint("Registered")
class App : DaggerApplication() {
    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        MobileAds.initialize(this, "ca-app-pub-5290873807821544~1263721570")
    }
    override fun applicationInjector(): AndroidInjector<out App> {
        return DaggerAppComponent.builder().create(this)
    }
}