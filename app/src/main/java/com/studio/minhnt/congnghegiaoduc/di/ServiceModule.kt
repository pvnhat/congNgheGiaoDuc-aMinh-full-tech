package com.studio.minhnt.congnghegiaoduc.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.studio.minhnt.congnghegiaoduc.api.AppService
import com.studio.minhnt.congnghegiaoduc.api.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ServiceModule {
    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit {
        val builder = OkHttpClient.Builder().addInterceptor { chain ->
            val request = chain.request()
            val requestBuilder = request.newBuilder()
            requestBuilder.addHeader("api_key", "2f46b59f1ffe470091924b80b542e0df").build()

            chain.proceed(requestBuilder.build())
        }
        return Retrofit.Builder()
                .client(builder.build())
                .baseUrl("http://api.openfpt.vn")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .build()
    }

    @Singleton
    @Provides
    internal fun provideAppService(retrofit: Retrofit): AppService {
        return retrofit.create(AppService::class.java)
    }
}