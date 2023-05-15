package com.ekachandra.jetasean

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ekachandra.jetasean.ui.components.BottomBar
import com.ekachandra.jetasean.ui.navigation.Screen
import com.ekachandra.jetasean.ui.screen.about.AboutScreen
import com.ekachandra.jetasean.ui.screen.detail.DetailScreen
import com.ekachandra.jetasean.ui.screen.favorite.FavoriteScreen
import com.ekachandra.jetasean.ui.screen.home.HomeScreen
import com.ekachandra.jetasean.ui.theme.JetASEANTheme

@ExperimentalMaterial3Api
@Composable
fun JetAseanApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.Detail.route) {
                BottomBar(navController = navController)
            }
        },
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    navigateToDetail = { countryId ->
                        navController.navigate(Screen.Detail.createRoute(countryId)) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Screen.Favorite.route) {
                FavoriteScreen(
                    navigateToDetail = { countryId ->
                        navController.navigate(Screen.Detail.createRoute(countryId)) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Screen.About.route) {
                AboutScreen()
            }

            composable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument("countryId") { type = NavType.LongType })
            ) {
                val id = it.arguments?.getLong("countryId") ?: -1L

                DetailScreen(
                    countryId = id,
                    navigateBack = {
                        navController.navigateUp()
                    }
                )


            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun JetAseanAppPreview() {
    JetASEANTheme {
        JetAseanApp()
    }
}