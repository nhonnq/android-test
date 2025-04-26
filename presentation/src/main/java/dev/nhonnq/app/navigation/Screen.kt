package dev.nhonnq.app.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object NavigationBar : Screen()

    @Serializable
    data object Users : Screen()

    @Serializable
    data class UserDetails(val loginUserName: String) : Screen()
}

sealed class Graph {
    @Serializable
    data object Main : Graph()
}