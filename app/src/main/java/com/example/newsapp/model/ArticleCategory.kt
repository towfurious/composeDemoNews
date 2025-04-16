package com.example.newsapp.model

import com.example.newsapp.model.ArticleCategory.BUSINESS
import com.example.newsapp.model.ArticleCategory.ENTERTAINMENT
import com.example.newsapp.model.ArticleCategory.GENERAL
import com.example.newsapp.model.ArticleCategory.HEALTH
import com.example.newsapp.model.ArticleCategory.SCIENCE
import com.example.newsapp.model.ArticleCategory.SPORTS
import com.example.newsapp.model.ArticleCategory.TECHNOLOGY

import com.example.newsapp.model.ArticleCategory.*

enum class ArticleCategory(val categoryName: String) {
    BUSINESS("business"),
    ENTERTAINMENT("entertainment"),
    GENERAL("general"),
    HEALTH("health"),
    SCIENCE("science"),
    SPORTS("sports"),
    TECHNOLOGY("technology")
}

fun getAllArticleCategories() : List<ArticleCategory> {
    return listOf(BUSINESS, ENTERTAINMENT, GENERAL, HEALTH, SCIENCE, SPORTS, TECHNOLOGY)
}

fun getArticleCategory(category: String): ArticleCategory? {
    val map = entries.associateBy(ArticleCategory::categoryName)
    return map[category]
}