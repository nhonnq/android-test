package dev.nhonnq.app.ui.users.viewmodel

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.nhonnq.app.ui.users.state.UsersNavigationState
import dev.nhonnq.app.ui.users.state.UsersUiState
import dev.nhonnq.domain.entities.UserEntity
import dev.nhonnq.domain.usecase.FetchUserList
import dev.nhonnq.domain.util.NetworkMonitor
import dev.nhonnq.domain.util.NetworkState
import dev.nhonnq.test.base.BaseTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.anyInt
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class UsersViewModelTest: BaseTest() {

    @Mock
    private lateinit var networkMonitor: NetworkMonitor

    @Mock
    private lateinit var fetchUserList: FetchUserList

    private lateinit var viewModel: UsersViewModel

    private val networkState = MutableStateFlow(NetworkState(isOnline = true, shouldRefresh = false))
    private val users = listOf(UserEntity("testUser", "avatarUrl", "htmlUrl"))
    private val pagingData: Flow<PagingData<UserEntity>> = flowOf(PagingData.from(users))

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        whenever(fetchUserList(perPage = anyInt())).thenReturn(pagingData)
        whenever(networkMonitor.networkState).thenReturn(networkState)
        viewModel = UsersViewModel(networkMonitor, fetchUserList)
    }

    @Test
    fun `test initial state`() = runUnconfinedTest {
            val initialState = UsersUiState()
            assertEquals(initialState, viewModel.uiState.value)
        }

    @Test
    fun `test showing loader when loading data`() = runUnconfinedTest {
        viewModel.onLoadStateUpdate(mockLoadState(LoadState.Loading))
        assertThat(viewModel.uiState.value.showLoading).isTrue()
    }

    @Test
    fun `test showing error message on loading failure`() = runUnconfinedTest {
        val errorMessage = "error"
        viewModel.onLoadStateUpdate(mockLoadState(LoadState.Error(Throwable(errorMessage))))

        viewModel.uiState.test {
            val emission: UsersUiState = awaitItem()
            assertThat(emission.showLoading).isFalse()
            assertThat(emission.errorMessage).isEqualTo(errorMessage)
        }
    }

    @Test
    fun `test showing users on loading success`() = runUnconfinedTest {
        viewModel.onLoadStateUpdate(mockLoadState(LoadState.NotLoading(true)))

        viewModel.uiState.test {
            val emission: UsersUiState = awaitItem()
            assertThat(emission.showLoading).isFalse()
            assertThat(emission.errorMessage).isNull()
        }
    }

    @Test
    fun `verify navigation to user details when an user is clicked`() = runUnconfinedTest {
        val loginUserName = "testUser"

        launch {
            viewModel.navigationState.test {
                val emission = awaitItem()
                assertThat(emission).isInstanceOf(UsersNavigationState.UserDetails::class.java)
                when (emission) {
                    is UsersNavigationState.UserDetails -> assertThat(emission.loginUserName).isEqualTo(loginUserName)
                }
            }
        }

        viewModel.onUserClicked(loginUserName)
    }

    @Test
    fun `test refreshing users`() = runUnconfinedTest {
        viewModel.refreshListState.test {
            viewModel.onRefresh()
            assertThat(awaitItem()).isEqualTo(Unit)
        }
    }

    @Test
    fun `test refreshing users when network state is lost`() = runUnconfinedTest {
        viewModel.refreshListState.test {
            networkState.emit(NetworkState(isOnline = false, shouldRefresh = true))
            assertThat(awaitItem()).isEqualTo(Unit)
        }
    }

    private fun mockLoadState(state: LoadState): CombinedLoadStates =
        CombinedLoadStates(
            refresh = state,
            prepend = state,
            append = state,
            source = LoadStates(state, state, state)
        )
}
