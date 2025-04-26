package dev.nhonnq.app.ui.users.details.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nhonnq.app.ui.base.BaseViewModel
import dev.nhonnq.app.ui.users.details.state.UserDetailsBundle
import dev.nhonnq.app.ui.users.details.state.UserDetailsState
import dev.nhonnq.data.usercase.GetUserLocal
import dev.nhonnq.data.mapper.toDb
import dev.nhonnq.domain.entities.UserEntity
import dev.nhonnq.domain.usecase.FetchUserDetails
import dev.nhonnq.domain.util.Result
import dev.nhonnq.domain.util.onError
import dev.nhonnq.domain.util.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val getUserLocal: GetUserLocal,
    private val fetchUserDetails: FetchUserDetails,
    userDetailsBundle: UserDetailsBundle,
) : BaseViewModel() {

    // UI state exposed to the UI
    private val _uiState: MutableStateFlow<UserDetailsState> = MutableStateFlow(UserDetailsState())
    val uiState = _uiState.asStateFlow()

    // Get loginUserName from bundle
    private val loginUserName: String = userDetailsBundle.loginUserName

    init {
        onInitialState()
    }

    // Initial State to get user details
    private fun onInitialState() = launch {
        // Get user details from local database
        getUserLocal(loginUserName).onSuccess {
            _uiState.value = UserDetailsState(it, true)
        }.onError {
            _uiState.value.isLoading = false
        }

        // Get user details from remote server
        getUserByLoginUserName(loginUserName).onSuccess {
            _uiState.value = UserDetailsState(it.toDb(), false)
        }.onError {
            _uiState.value.isLoading = false
        }
    }

    /*
     * Get user details from remote server
     */
    private suspend fun getUserByLoginUserName(loginUserName: String): Result<UserEntity> = fetchUserDetails(loginUserName)

}