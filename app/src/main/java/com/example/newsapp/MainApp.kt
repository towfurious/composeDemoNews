package com.example.newsapp

import android.app.Application
import com.example.newsapp.network.Api
import com.example.newsapp.network.NewsProvider

class MainApp: Application() {

    val provider by lazy {
        NewsProvider(Api.retrofitService)
    }
}