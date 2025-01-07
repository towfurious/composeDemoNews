@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.newsapp.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.newsapp.R
import com.example.newsapp.data.MockData
import com.example.newsapp.data.MockData.getTimeAgo
import com.example.newsapp.model.NewsData

@Composable
fun DetailScreen(newsData: NewsData, scrollState: ScrollState, navController: NavController) {
    Scaffold(topBar = { DetailTopAppBar(onBackPressed = { navController.popBackStack() }) },
        content = { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Detail Screen", fontWeight = FontWeight.SemiBold)
                Image(painter = painterResource(id = newsData.image), contentDescription = "")

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    InfoWithIcon(Icons.Default.Edit, info = newsData.author)
                    InfoWithIcon(
                        icon = Icons.Default.Edit,
                        info = MockData.stringToDate(newsData.publishedAt).getTimeAgo()
                    )
                }
                Text(text = newsData.title, fontWeight = FontWeight.Bold)
                Text(text = newsData.description, modifier = Modifier.padding(top = 16.dp))
            }


        })

}

@Composable
fun DetailTopAppBar(onBackPressed: () -> Unit = {}) {
    TopAppBar(title = {
        Text(text = "Detail Screen", fontWeight = FontWeight.SemiBold)
    },
        navigationIcon = {
            IconButton(onClick = { onBackPressed() }, content = {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
            })
        })
}

@Composable
fun InfoWithIcon(icon: ImageVector, info: String) {
    Row {
        Icon(
            imageVector = icon,
            contentDescription = "Author",
            modifier = Modifier.padding(end = 8.dp),
            colorResource(id = R.color.purple_500)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = info, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    DetailScreen(
        NewsData(
            2,
            R.drawable.namita,
            author = "Namita Singh",
            title = "Cleo Smith news — live: Kidnap suspect 'in hospital again' as 'hard police grind' credited for breakthrough - The Independent",
            description = "The suspected kidnapper of four-year-old Cleo Smith has been treated in hospital for a second time amid reports he was “attacked” while in custody.",
            publishedAt = "2021-11-04T04:42:40Z"
        ), rememberScrollState(), rememberNavController()

    )
}