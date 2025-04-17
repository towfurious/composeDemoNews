package com.example.newsapp.network

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.newsapp.model.ArticleCategory
import com.example.newsapp.model.TopNewsResponse
import com.example.newsapp.model.getArticleCategory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NewsRepository @Inject constructor(private val service: NewsService) {
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val selectedCategory: MutableState<ArticleCategory?> = mutableStateOf(null)

    suspend fun getArticles(countryCode: String = "us"): TopNewsResponse =
        withContext(ioDispatcher) {
            service.getTopArticles(countryCode)
        }

    suspend fun getArticlesByCategory(category: String): TopNewsResponse =
        withContext(ioDispatcher) {
            service.getArticlesByCategory(
                category = category
            )
        }

    suspend fun getArticlesBySources(sources: String): TopNewsResponse =
        withContext(ioDispatcher) {
            service.getArticlesBySources(
                sources = sources
            )
        }

    suspend fun getSearchedArticles(query: String): TopNewsResponse =
        withContext(ioDispatcher) {
            service.getArticles(
                query = query
            )
        }

    fun onSelectedCategoryChanged(category: String) {
        val newCategory = getArticleCategory(category = category)
        selectedCategory.value = newCategory
    }
}
