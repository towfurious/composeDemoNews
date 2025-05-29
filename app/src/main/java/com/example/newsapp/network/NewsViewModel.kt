package com.example.newsapp.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.model.ArticleCategory
import com.example.newsapp.model.TopNewsResponse
import com.example.newsapp.model.getArticleCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository) : ViewModel() {

    private val _newsResponse = MutableStateFlow(TopNewsResponse())
    val newsResponse: StateFlow<TopNewsResponse> = _newsResponse.asStateFlow()

    private val _articleByCategory = MutableStateFlow(TopNewsResponse())
    val articleByCategory: StateFlow<TopNewsResponse> = _articleByCategory.asStateFlow()

    private val _articleBySource = MutableStateFlow(TopNewsResponse())
    val articleBySource: StateFlow<TopNewsResponse> = _articleBySource.asStateFlow()

    private val _selectedCategory = MutableStateFlow<ArticleCategory?>(null)
    val selectedCategory: StateFlow<ArticleCategory?> = _selectedCategory.asStateFlow()

    private val _searchedNewsResponse = MutableStateFlow(TopNewsResponse())
    val searchedNewsResponse: StateFlow<TopNewsResponse> = _searchedNewsResponse.asStateFlow()

    val query = MutableStateFlow("")

    val sourceName = MutableStateFlow("abc-news")

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<Boolean>(false)
    val error: StateFlow<Boolean> = _error.asStateFlow()

    private val errorHandler = CoroutineExceptionHandler { _, error ->
        if (error is Exception) {
            _error.update { true }
        }
    }

    fun refreshAll() {
        _isLoading.update { true }
        viewModelScope.launch(errorHandler) {
            _newsResponse.update { repository.getArticles() }
            _isLoading.update { false }
        }
    }

    fun getArticlesByCategory(category: String) {
        _isLoading.update { true }
        viewModelScope.launch(errorHandler) {
            val response = repository.getArticlesByCategory(category = category)
            _articleByCategory.update { response }
            _isLoading.update { false }
        }
    }

    fun getArticlesBySource() {
        _isLoading.update { true }
        viewModelScope.launch(errorHandler) {
            val response = repository.getArticlesBySources(sources = sourceName.value)
            _articleBySource.update { response }
            _isLoading.update { false }
        }
    }

    fun getSearchedArticles(query: String) {
        _isLoading.update { true }
        viewModelScope.launch(errorHandler) {
            val response = repository.getSearchedArticles(query = query)
            _searchedNewsResponse.update { response }
            _isLoading.update { false }
        }
    }

    fun onSelectedCategoryChanged(category: String) {
        val newCategory = getArticleCategory(category = category)
        _selectedCategory.update { newCategory }
    }
}