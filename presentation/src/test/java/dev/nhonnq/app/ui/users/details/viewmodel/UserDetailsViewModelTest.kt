package dev.nhonnq.app.ui.users.details.viewmodel

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.nhonnq.app.ui.users.details.state.UserDetailsBundle
import dev.nhonnq.app.ui.users.details.state.UserDetailsState
import dev.nhonnq.data.usercase.GetUserLocal
import dev.nhonnq.domain.entities.UserEntity
import dev.nhonnq.domain.usecase.FetchUserDetails
import dev.nhonnq.domain.util.Result
import dev.nhonnq.test.base.BaseTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class UserDetailsViewModelTest: BaseTest() {

    @Mock
    private lateinit var getUserLocal: GetUserLocal

    @Mock
    private lateinit var fetchUserDetails: FetchUserDetails

    private lateinit var viewModel: UserDetailsViewModel

    private val userDetailsBundle: UserDetailsBundle = mock()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getUserLocal = mock()
        fetchUserDetails = mock()
    }

    @Test
    fun `test ui state reflects user details correctly`() = runUnconfinedTest {
        val loginUserName = "testUser"
        val user = UserEntity("testUser", "avatarUrl", "htmlUrl")

        createViewModel(
            loginUserName = loginUserName,
            userDetailsResult = Result.Success(user)
        )

        viewModel.uiState.test {
            val emission = awaitItem()
            assertThat(emission.userData?.login).isEqualTo(user.login)
            assertThat(emission.userData?.avatarUrl).isEqualTo(user.avatarUrl)
            assertThat(emission.userData?.htmlUrl).isEqualTo(user.htmlUrl)
        }

        verify(getUserLocal).invoke(loginUserName)
    }

    private suspend fun createViewModel(
        loginUserName: String,
        userDetailsResult: Result<UserEntity>,
    ) {
        whenever(userDetailsBundle.loginUserName).thenReturn(loginUserName)
        whenever(fetchUserDetails(loginUserName)).thenReturn(userDetailsResult)

        viewModel = UserDetailsViewModel(
            getUserLocal = getUserLocal,
            fetchUserDetails = fetchUserDetails,
            userDetailsBundle = userDetailsBundle
        )
    }

    @Test
    fun `test no change in UI when user login is invalid`() = runUnconfinedTest {
        val loginUserName = ""
        createViewModel(
            loginUserName = loginUserName,
            userDetailsResult = Result.Error(mock())
        )

        viewModel.uiState.test {
            val emission = awaitItem()
            assertThat(emission).isEqualTo(UserDetailsState())
        }
    }
}
