package com.example.newsapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.newsapp.R
import com.example.newsapp.model.TopNewsArticle
import com.example.newsapp.network.NewsViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Sources(
    newsViewModel: NewsViewModel,
    isLoading: MutableState<Boolean>,
    isError: MutableState<Boolean>
) {
    val items = listOf(
        "TechCrunch" to "techcrunch",
        "TalkSport" to "talksport",
        "Business Insider" to "business-insider",
        "Reuters" to "reuters",
        "Politico" to "the-verge"
    )

    Scaffold(topBar = {
        TopAppBar(
            windowInsets = WindowInsets(0.dp),
            modifier = Modifier
                .fillMaxWidth(),
            title = {
                Text(
                    text = "${
                        newsViewModel.sourceName.collectAsState().value.uppercase(Locale.getDefault())
                    } Source"
                )
            },
            actions = {
                var menuExpanded by remember { mutableStateOf(false) }
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = null)
                }
                MaterialTheme(
                    shapes = MaterialTheme.shapes.copy(
                        medium = RoundedCornerShape(16.dp)
                    )
                ) {
                    DropdownMenu(
                        expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                        items.forEach {
                            DropdownMenuItem(text = { Text(text = it.first) }, onClick = {
                                newsViewModel.sourceName.value = it.second
                                newsViewModel.getArticlesBySource()
                                menuExpanded = false
                            })
                        }
                    }
                }
            })
    }) { paddingValues ->
        when {
            isLoading.value -> {
                LoadingUI()
            }
            isError.value -> {
                ErrorUI()
            }
            else -> {
                newsViewModel.getArticlesBySource()
                val articles = newsViewModel.articleBySource.collectAsState().value.articles
                SourceContent(paddingValues, articles = articles ?: emptyList())
            }
        }
    }
}

@Composable
fun SourceContent(paddingValues: PaddingValues, articles: List<TopNewsArticle>) {
    val uriHandler = LocalUriHandler.current

    LazyColumn(contentPadding = paddingValues) {
        items(articles) { article ->
            val annotatedString = buildAnnotatedString {
                pushStringAnnotation(
                    tag = "URL",
                    annotation = article.url ?: "newsapi.org"
                )
                withStyle(
                    style = SpanStyle(
                        color = colorResource(id = R.color.purple_500),
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    withLink(LinkAnnotation.Url(url = article.url ?: "newsapi.org")) {
                        append("website")
                    }
                    append("Read Full Article Here")
                }
            }
            Card(modifier = Modifier.padding(8.dp), content = {
                Column(
                    modifier = Modifier
                        .height(200.dp)
                        .padding(end = 8.dp, start = 8.dp),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = article.title ?: "Not Available",
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.testTag("sourcesArticleTitle")
                    )

                    Text(
                        text = article.description ?: "Not Available",
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.testTag("sourcesArticleText")

                    )
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ), content = {
                            Text(annotatedString, Modifier
                                .clickable {
                                    annotatedString.getStringAnnotations(
                                        tag = "URL",
                                        start = 0,
                                        end = annotatedString.length
                                    ).firstOrNull()?.let { result ->
                                        if (result.tag == "URL") {
                                            uriHandler.openUri(result.item)
                                        }
                                    }
                                }
                                .padding(8.dp)
                                .testTag("sourcesArticleLink"))

                        })
                }
            })
        }
    }
}

@Preview
@Composable
fun SourceContentPreview() {
    val paddingValues: PaddingValues = PaddingValues()
    val articles = listOf(TopNewsArticle(
        author = "Namita Singh",
        title = "Cleo Smith news — live: Kidnap suspect 'in hospital again' as 'hard police grind' credited for breakthrough - The Independent",
        description = "The suspected kidnapper of four-year-old Cleo Smith has been treated in hospital for a second time amid reports he was “attacked” while in custody.",
        publishedAt = "2021-11-04T04:42:40Z"
    ))
    SourceContent(paddingValues, articles)
}