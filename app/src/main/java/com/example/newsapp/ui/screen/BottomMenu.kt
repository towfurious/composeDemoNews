package com.example.newsapp.ui.screen

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun BottomMenu(navController: NavController) {
    val menuItems = listOf(
        BottomMenuScreen.TopNews,
        BottomMenuScreen.Categories,
        BottomMenuScreen.Sources
    )

    NavigationBar(containerColor = MaterialTheme.colorScheme.background, content = {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        menuItems.forEach {
            NavigationBarItem(
                selected = currentRoute == it.route,
                icon = { Icon(imageVector = it.icon, contentDescription = it.title) },
                alwaysShowLabel = true,
                colors = navBarItemColors(),
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

@Composable
fun navBarItemColors() : NavigationBarItemColors {
    return NavigationBarItemDefaults.colors(
        unselectedIconColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
        selectedIconColor = MaterialTheme.colorScheme.primary,
        unselectedTextColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
        selectedTextColor = MaterialTheme.colorScheme.primary,
        indicatorColor = MaterialTheme.colorScheme.surfaceVariant)
}

@Preview(showBackground = true)
@Composable
fun BottomMenuPreview() {
    BottomMenu(rememberNavController())
}