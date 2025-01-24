package com.example.newsapp.network

import com.example.newsapp.model.TopNewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    @GET("top-headlines")
    fun getTopArticles(@Query("country") country:String, @Query("apiKey") apiKey:String, @Query("language") language: String): Call<TopNewsResponse>
}