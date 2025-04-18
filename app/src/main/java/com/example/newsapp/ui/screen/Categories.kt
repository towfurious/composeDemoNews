package com.example.newsapp.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.newsapp.R
import com.example.newsapp.util.DateUtil
import com.example.newsapp.util.DateUtil.getTimeAgo
import com.example.newsapp.model.TopNewsArticle
import com.example.newsapp.model.getAllArticleCategories
import com.example.newsapp.network.NewsViewModel
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun Categories(onFetchCategory: (String) -> Unit = {}, newsViewModel: NewsViewModel,
               isLoading: MutableState<Boolean>, isError: MutableState<Boolean>) {
    val tabsItems = getAllArticleCategories()
    val isSelected = newsViewModel.selectedCategory.collectAsState().value
    val articles = newsViewModel.articleByCategory.collectAsState().value
    Column {
        when {
            isLoading.value -> {
                LoadingUI()
            }
            isError.value -> {
                ErrorUI()
            }
            else -> {
                LazyRow {
                    items(tabsItems.size) {
                        val category = tabsItems[it]
                        CategoryTab(
                            category = category.categoryName,
                            onFetchCategory = onFetchCategory,
                            isSelected = isSelected == category
                        )
                    }
                }
            }
        }
        ArticleContent(articles = articles.articles ?: listOf())
    }
}

@Composable
fun CategoryTab(category: String, isSelected: Boolean = false,
                onFetchCategory: (String) -> Unit) {
        val background = if (isSelected) colorResource(id = R.color.purple_200) else colorResource(id = R.color.purple_700)
        Surface (
            modifier = Modifier
                .padding(horizontal = 4.dp, vertical = 16.dp)
                .clickable {
                    onFetchCategory(category)
                },
            shape = MaterialTheme.shapes.small,
            color = background,
            content = {
                Text(
                    text = category,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
            }
        )
}

@Composable
fun ArticleContent(articles: List<TopNewsArticle>, modifier: Modifier = Modifier) {
    LazyColumn {
        items(articles) {
            article ->
            Card(modifier.padding(8.dp), border = BorderStroke(2.dp, color = MaterialTheme.colorScheme.primary))
            {
                Row(modifier.fillMaxWidth()) {
                    CoilImage(
                        imageModel = article.urlToImage,
                        modifier = Modifier.size(100.dp),
                        placeHolder = painterResource(id = R.drawable.breaking_news),
                        error = painterResource(id = R.drawable.breaking_news)
                    )
                    Column(modifier.padding(8.dp)) {
                        Text(text = article.title ?: "not available",
                            fontWeight = FontWeight.Bold,
                            maxLines = 3, overflow = TextOverflow.Ellipsis)
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = article.author ?: "not available", maxLines = 1)
                            Text(text = DateUtil.stringToDate(
                                article.publishedAt ?: "2021-11-10T14:25:20Z").getTimeAgo(), maxLines = 1)
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ArticleContentPreview() {
    ArticleContent(
        articles = listOf(
            TopNewsArticle(
                author = "CBSBoston.com Staff",
                title = "Principal Beaten Unconscious At Dorchester School; Classes Canceled Thursday - CBS Boston",
                description = "Principal Patricia Lampron and another employee were assaulted at Henderson Upper Campus during dismissal on Wednesday.",
                publishedAt = "2021-11-04T01:55:00Z"
            )
        )
    )
}