package dev.nhonnq.app.ui.users.details.state

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import dev.nhonnq.app.navigation.Screen
import dev.nhonnq.data.entities.UserData
import javax.inject.Inject

data class UserDetailsState(
    var userData: UserData? = null,
    var isLoading: Boolean? = false
)

class UserDetailsBundle @Inject constructor(
    savedStateHandle: SavedStateHandle
) {
    val loginUserName: String = savedStateHandle.toRoute<Screen.UserDetails>().loginUserName
}