package dev.nhonnq.app.ui.main

import androidx.navigation.NavHostController
import dev.nhonnq.app.navigation.Screen

/**
 * [MainRouter] is responsible for handling navigation within the main application flow.
 * It encapsulates the navigation logic using a [NavHostController] and provides
 * methods to navigate to specific screens.
 *
 * @property mainNavController The [NavHostController] used to manage navigation within the main application.
 */
class MainRouter(
    private val mainNavController: NavHostController
) {

    fun navigateToUserDetails(loginUserName: String) {
        mainNavController.navigate(Screen.UserDetails(loginUserName))
    }
}