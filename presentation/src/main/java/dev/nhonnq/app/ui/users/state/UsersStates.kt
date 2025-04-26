package dev.nhonnq.app.ui.users.state

data class UsersUiState(
    val showLoading: Boolean = true,
    val errorMessage: String? = null,
)

sealed class UsersNavigationState {
    data class UserDetails(val loginUserName: String) : UsersNavigationState()
}
