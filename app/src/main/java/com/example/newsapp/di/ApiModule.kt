package com.example.newsapp.di

import com.example.newsapp.network.Api
import com.example.newsapp.network.NewsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun provideApiService(): NewsService {
        return Api.retrofitService
    }
}