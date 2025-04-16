package com.example.newsapp.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.newsapp.ui.screen.BottomMenu
import com.example.newsapp.model.TopNewsArticle
import com.example.newsapp.network.Api
import com.example.newsapp.network.NewsProvider
import com.example.newsapp.network.NewsViewModel
import com.example.newsapp.ui.screen.BottomMenuScreen
import com.example.newsapp.ui.screen.Categories
import com.example.newsapp.ui.screen.DetailScreen
import com.example.newsapp.ui.screen.Sources
import com.example.newsapp.ui.screen.TopNews

@Composable
fun NewsApp(newsViewModel: NewsViewModel) {
    val navController = rememberNavController()
    val scrollState = rememberScrollState()
    MainScreen(navController, scrollState, newsViewModel)
}

@Composable
fun MainScreen(
    navController: NavHostController,
    scrollState: ScrollState,
    newsViewModel: NewsViewModel
) {
    Scaffold(
        bottomBar = { BottomMenu(navController) },
        content = { paddingValues ->
            Navigation(
                navHostController = navController,
                scrollState = scrollState,
                paddingValues = paddingValues,
                viewModel = newsViewModel
            )
        })
}

@Composable
fun Navigation(
    navHostController: NavHostController,
    scrollState: ScrollState,
    paddingValues: PaddingValues,
    viewModel: NewsViewModel
) {
    val articles = mutableListOf(TopNewsArticle())
    val topArticles = viewModel.newsResponse.collectAsState().value.articles
    articles.addAll(topArticles ?: listOf())

    NavHost(
        navController = navHostController,
        startDestination = BottomMenuScreen.TopNews.route,
        modifier = Modifier.padding(paddingValues = paddingValues)
    ) {

        val queryState = mutableStateOf(viewModel.query.value)
        bottomNavigation(navHostController, articles, queryState, viewModel)
        composable(
            "DetailScreen/{index}",
            arguments = listOf(navArgument("index") {
                type = NavType.IntType
            })
        )
        { navBackStackEntry ->
            val index = navBackStackEntry.arguments?.getInt("index")
            index?.let {
                if (queryState.value != "") {
                    articles.clear()
                    articles.addAll(viewModel.searchedNewsResponse.value.articles ?: listOf())
                } else {
                    articles.clear()
                    articles.addAll(viewModel.newsResponse.value.articles ?: listOf())
                }
                val article = articles[index]
                DetailScreen(article, scrollState = scrollState, navHostController)
            }
        }
    }
}

fun NavGraphBuilder.bottomNavigation(
    navController: NavController,
    articles: List<TopNewsArticle>,
    query: MutableState<String>,
    newsViewModel: NewsViewModel
) {
    composable(BottomMenuScreen.TopNews.route) {
        TopNews(
            navController,
            articles,
            query,
            viewModel = newsViewModel
        )
    }
    composable(BottomMenuScreen.Categories.route) {
        newsViewModel.onSelectedCategoryChanged("business")
        newsViewModel.getArticlesByCategory("business")

        Categories(newsViewModel = newsViewModel, onFetchCategory = {
            newsViewModel.onSelectedCategoryChanged(it)
            newsViewModel.getArticlesByCategory(it)
        })
    }
    composable(BottomMenuScreen.Sources.route) { Sources(newsViewModel = newsViewModel) }
}

@Preview(showBackground = true)
@Composable
fun NewsAppPreview() {
    NewsApp(viewModel())
}