package com.example.newsapp.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.newsapp.R
import com.example.newsapp.ui.screen.BottomMenuScreen

@Composable
fun BottomMenu(navController: NavController) {
    val menuItems = listOf(
        BottomMenuScreen.TopNews,
        BottomMenuScreen.Categories,
        BottomMenuScreen.Sources
    )

    NavigationBar(containerColor = colorResource(id = R.color.white), content = {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        menuItems.forEach {
            NavigationBarItem(
                selected = currentRoute == it.route,
                icon = { Icon(imageVector = it.icon, contentDescription = it.title) },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    unselectedTextColor = Color.Gray, // Set unselected text color
                    selectedTextColor = Color.Black, // Set selected text color
                    indicatorColor = Color.LightGray // Set indicator color
                ),
                label = { Text(text = it.title)},
                        onClick = {
                    navController.navigate(it.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    })
}

@Preview(showBackground = true)
@Composable
fun BottomMenuPreview() {
    BottomMenu(rememberNavController())
}