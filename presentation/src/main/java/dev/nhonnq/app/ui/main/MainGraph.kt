package dev.nhonnq.app.ui.main

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dev.nhonnq.app.navigation.Graph
import dev.nhonnq.app.navigation.Screen
import dev.nhonnq.app.ui.navigationbar.NavigationBarNestedGraph
import dev.nhonnq.app.ui.navigationbar.NavigationBarScreen
import dev.nhonnq.app.ui.users.details.UserDetailsScreen
import dev.nhonnq.app.ui.users.details.viewmodel.UserDetailsViewModel
import dev.nhonnq.app.util.composableHorizontalSlide

@Composable
fun MainGraph(
    mainNavController: NavHostController,
    darkMode: Boolean,
    onThemeUpdated: () -> Unit
) {
    NavHost(
        navController = mainNavController,
        startDestination = Screen.NavigationBar,
        route = Graph.Main::class
    ) {
        composableHorizontalSlide<Screen.NavigationBar> {
            val nestedNavController = rememberNavController()
            NavigationBarScreen(
                darkMode = darkMode,
                onThemeUpdated = onThemeUpdated
            ) {
                NavigationBarNestedGraph(
                    navController = nestedNavController,
                    mainNavController = mainNavController,
                    parentRoute = Graph.Main::class
                )
            }
        }

        composableHorizontalSlide<Screen.UserDetails> {
            val viewModel = hiltViewModel<UserDetailsViewModel>()
            UserDetailsScreen(
                mainNavController = mainNavController,
                viewModel = viewModel,
            )
        }
    }
}