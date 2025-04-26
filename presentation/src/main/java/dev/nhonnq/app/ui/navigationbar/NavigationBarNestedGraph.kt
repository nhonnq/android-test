package dev.nhonnq.app.ui.navigationbar

import UsersScreen
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.nhonnq.app.navigation.Screen
import dev.nhonnq.app.ui.users.viewmodel.UsersViewModel
import dev.nhonnq.app.ui.main.MainRouter
import dev.nhonnq.app.util.composableHorizontalSlide
import kotlin.reflect.KClass

@Composable
fun NavigationBarNestedGraph(
    navController: NavHostController,
    mainNavController: NavHostController,
    parentRoute: KClass<*>?
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Users,
        route = parentRoute
    ) {
        // Main router for Users screen
        composableHorizontalSlide<Screen.Users> {
            val viewModel = hiltViewModel<UsersViewModel>()
            UsersScreen(
                mainRouter = MainRouter(mainNavController),
                viewModel = viewModel
            )
        }
    }
}