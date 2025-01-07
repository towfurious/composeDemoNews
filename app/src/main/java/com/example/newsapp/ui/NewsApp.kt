package com.example.newsapp.ui

import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.newsapp.data.MockData
import com.example.newsapp.ui.screen.DetailScreen
import com.example.newsapp.ui.screen.TopNews

@Composable
fun NewsApp() {
    Navigation()
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val rememberScrollState = rememberScrollState()
    NavHost(navController = navController, startDestination = "topNews") {
        composable("TopNews") { TopNews(navController) }
        composable(
            "DetailScreen/{newsId}",
            arguments = listOf(navArgument("newsId") {
                type = NavType.IntType
            })
        )
        {
            val id = it.arguments?.getInt("newsId")
            val newsData = MockData.getNews(id)
            DetailScreen(newsData, scrollState = rememberScrollState, navController)
        }
    }
}