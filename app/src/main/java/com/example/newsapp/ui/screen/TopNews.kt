package com.example.newsapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newsapp.R
import com.example.newsapp.data.MockData
import com.example.newsapp.data.MockData.getTimeAgo
import com.example.newsapp.model.TopNewsArticle
import com.example.newsapp.network.NewsViewModel
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun TopNews(
    navController: NavController,
    articles: List<TopNewsArticle>,
    query: MutableState<String>,
    viewModel: NewsViewModel,
    isLoading: MutableState<Boolean>,
    isError: MutableState<Boolean>
) {
    /*     Modifier.safeDrawingPadding()
           Modifier.systemBarsPadding()
     This snippet applies the safeDrawing window
     insets as padding around the entire content of the app.
     While this ensures that interactable elements don't overlap with the system UI */
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBar(query = query, viewModel = viewModel)
        val searchedText = query.value
        val resultList = mutableListOf<TopNewsArticle>()
        if (searchedText != "") {
            resultList.addAll(viewModel.searchedNewsResponse.collectAsState().value.articles ?: articles)
        } else {
            resultList.addAll(articles)
        }

        when {
            isLoading.value -> {
                LoadingUI()
            }

            isError.value -> {
                ErrorUI()
            }

            else -> {
                LazyColumn {
                    items(resultList.size) { index ->
                        TopNewsItem(
                            article = resultList[index],
                            onNewsClick = { navController.navigate("DetailScreen/$index") }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TopNewsItem(article: TopNewsArticle, onNewsClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .height(200.dp)
            .padding(8.dp)
            .clickable {
                onNewsClick()
            }) {
        CoilImage(
            imageModel = article.urlToImage,
            contentScale = ContentScale.Crop,
            error = ImageBitmap.imageResource(R.drawable.breaking_news),
            placeHolder = ImageBitmap.imageResource(R.drawable.breaking_news)
        )
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(top = 16.dp, start = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            article.publishedAt?.let {
                Text(
                    text = MockData.stringToDate(article.publishedAt).getTimeAgo(),
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(80.dp))
            article.title?.let {
                Text(
                    text = article.title, color = Color.White,
                    fontWeight = FontWeight.Normal, maxLines = 2
                )
            }
        }
    }
}
