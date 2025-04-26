package dev.nhonnq.app.ui.users.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dev.nhonnq.app.ui.base.BaseViewModel
import dev.nhonnq.app.util.singleSharedFlow
import dev.nhonnq.domain.util.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nhonnq.app.ui.users.state.UsersNavigationState
import dev.nhonnq.app.ui.users.state.UsersUiState
import dev.nhonnq.data.entities.UserData
import dev.nhonnq.data.mapper.toDb
import dev.nhonnq.data.util.DataConstants.PAGE_SIZE
import dev.nhonnq.domain.usecase.FetchUserList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val networkMonitor: NetworkMonitor,
    fetchUserList: FetchUserList
) : BaseViewModel() {

    // Fetch user list from server and handle local database caching
    val users: Flow<PagingData<UserData>> = fetchUserList(
        perPage = PAGE_SIZE
    ).map { it.map { user -> user.toDb() } }.cachedIn(viewModelScope)

    // UI State
    private val _uiState: MutableStateFlow<UsersUiState> = MutableStateFlow(UsersUiState())
    val uiState = _uiState.asStateFlow()

    // Navigation State
    private val _navigationState: MutableSharedFlow<UsersNavigationState> = singleSharedFlow()
    val navigationState = _navigationState.asSharedFlow()

    // Refresh List State
    private val _refreshListState: MutableSharedFlow<Unit> = singleSharedFlow()
    val refreshListState = _refreshListState.asSharedFlow()

    init {
        observeNetworkStatus()
    }

    /*
     * Observe network status and refresh list when network is available
     */
    private fun observeNetworkStatus() {
        networkMonitor.networkState
            .onEach { if (it.shouldRefresh) onRefresh() }
            .launchIn(viewModelScope)
    }

    /*
     * Handle user click event
     */
    fun onUserClicked(loginUserName: String) =
        _navigationState.tryEmit(UsersNavigationState.UserDetails(loginUserName))

    /*
     * Handle load state update
     */
    fun onLoadStateUpdate(loadState: CombinedLoadStates) {
        val showLoading = loadState.refresh is LoadState.Loading

        val error = when (val refresh = loadState.refresh) {
            is LoadState.Error -> refresh.error.message
            else -> null
        }

        _uiState.update { it.copy(showLoading = showLoading, errorMessage = error) }
    }

    /*
     * Handle refresh event
     */
    fun onRefresh() = launch {
        _refreshListState.emit(Unit)
    }
}
