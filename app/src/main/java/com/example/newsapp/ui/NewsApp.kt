package com.example.newsapp.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import com.example.newsapp.model.TopNewsArticle
import com.example.newsapp.ui.screen.BottomMenuScreen
import com.example.newsapp.ui.screen.Categories
import com.example.newsapp.ui.screen.DetailScreen
import com.example.newsapp.ui.screen.Sources
import com.example.newsapp.ui.screen.TopNews
import com.example.newsapp.network.NewsManager

@Composable
fun NewsApp() {
    val navController = rememberNavController()
    val scrollState = rememberScrollState()
    MainScreen(navController, scrollState)
}

@Composable
fun MainScreen(navController: NavHostController, scrollState: ScrollState) {
    Scaffold(bottomBar = { BottomMenu(navController) }, content = { paddingValues ->
        Navigation(navController, scrollState = scrollState, paddingValues = paddingValues)
    })
}

@Composable
fun Navigation(
    navHostController: NavHostController,
    scrollState: ScrollState,
    newsManager: NewsManager = NewsManager(),
    paddingValues: PaddingValues
) {
    val articles = newsManager.newsResponse.value.articles

    articles?.let {
        NavHost(
            navController = navHostController,
            startDestination = BottomMenuScreen.TopNews.route,
            modifier = Modifier.padding(paddingValues = paddingValues)
        ) {
            bottomNavigation(navHostController, articles, newsManager)
            composable(
                "DetailScreen/{index}",
                arguments = listOf(navArgument("index") {
                    type = NavType.IntType
                })
            )
            {   navBackStackEntry ->
                val index = navBackStackEntry.arguments?.getInt("index")
                index?.let {
                    val acticle = articles[index]
                    DetailScreen(acticle, scrollState = scrollState, navHostController)
                }
            }
        }
    }
}

fun NavGraphBuilder.bottomNavigation(
    navController: NavController,
    articles: List<TopNewsArticle>,
    newsManager: NewsManager
) {
    composable(BottomMenuScreen.TopNews.route) { TopNews(navController, articles) }
    composable(BottomMenuScreen.Categories.route) {
        Categories(newsManager = newsManager, onFetchCategory = {
            newsManager.onSelectedCategoryChanged(it)
        })
    }
    composable(BottomMenuScreen.Sources.route) { Sources() }
}

@Preview(showBackground = true)
@Composable
fun NewsAppPreview() {
    NewsApp()
}