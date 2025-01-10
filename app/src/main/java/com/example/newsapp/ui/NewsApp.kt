package com.example.newsapp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.newsapp.components.BottomMenu
import com.example.newsapp.data.MockData
import com.example.newsapp.ui.screen.BottomMenuScreen
import com.example.newsapp.ui.screen.Categories
import com.example.newsapp.ui.screen.DetailScreen
import com.example.newsapp.ui.screen.Sources
import com.example.newsapp.ui.screen.TopNews

@Composable
fun NewsApp() {
    val navController = rememberNavController()
    val scrollState = rememberScrollState()
    MainScreen(navController, scrollState)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavHostController, scrollState: ScrollState) {
    Scaffold(bottomBar = { BottomMenu(navController) }, content = {
        Navigation(navController, scrollState = scrollState)
    })
}

@Composable
fun Navigation(navHostController: NavHostController, scrollState: ScrollState) {

    NavHost(navController = navHostController, startDestination = "topNews") {
        bottomNavigation(navHostController)
        composable("TopNews") { TopNews(navHostController) }
        composable(
            "DetailScreen/{newsId}",
            arguments = listOf(navArgument("newsId") {
                type = NavType.IntType
            })
        )
        {
            val id = it.arguments?.getInt("newsId")
            val newsData = MockData.getNews(id)
            DetailScreen(newsData, scrollState = scrollState, navHostController)
        } 
    }
}

fun NavGraphBuilder.bottomNavigation(navController: NavController) {
    composable(BottomMenuScreen.TopNews.route) { TopNews(navController) }
    composable(BottomMenuScreen.Categories.route) { Categories() }
    composable(BottomMenuScreen.Sources.route) { Sources() }
}

@Preview(showBackground = true)
@Composable
fun NewsAppPreview() {
    NewsApp()
}