package dev.nhonnq.data

import dev.nhonnq.data.api.UserApi
import dev.nhonnq.data.repository.user.UserRemoteDataSource
import dev.nhonnq.domain.entities.UserEntity
import dev.nhonnq.domain.util.Result
import dev.nhonnq.domain.util.asSuccessOrNull
import dev.nhonnq.test.base.BaseTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class UserRemoteDataSourceTest: BaseTest() {

    @Mock
    private var userApi: UserApi = mock()
    private lateinit var userRemoteDataSource: UserRemoteDataSource

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        userRemoteDataSource = UserRemoteDataSource(userApi)
    }

    @Test
    fun `fetchUsers should return success result`() = runUnconfinedTest {
        val users = listOf(UserEntity("login", "avatarUrl", "htmlUrl", "location", 1, 1, "blog"))
        Mockito.`when`(userApi.getUsers(0, 20)).thenReturn(users)

        val result = userRemoteDataSource.fetchUsers(0, 20)
        assertTrue(result is Result.Success)
        assertEquals(1, result.asSuccessOrNull()?.size)
        assertEquals(users, result.asSuccessOrNull())
    }

    @Test
    fun `test fetchUsers returns error when API call fails`() = runUnconfinedTest {
        Mockito.`when`(userApi.getUsers(0, 20)).thenThrow(RuntimeException("Network error"))

        val result = userRemoteDataSource.fetchUsers(0, 20)

        assertTrue(result is Result.Error)
    }

    @Test
    fun `fetchUserDetails should return success result`() = runUnconfinedTest {
        val user = UserEntity("login", "avatarUrl", "htmlUrl", "location", 1, 1, "blog")
        Mockito.`when`(userApi.getUserDetails("login")).thenReturn(user)

        val result = userRemoteDataSource.fetchUserDetails("login")

        assertEquals(Result.Success(user), result)
    }

    @Test
    fun `fetchUserDetails should return error result`() = runUnconfinedTest {
        val exception = RuntimeException("Network error")
        Mockito.`when`(userApi.getUserDetails("login")).thenThrow(exception)

        val result = userRemoteDataSource.fetchUserDetails("login")

        assertTrue(result is Result.Error)
    }
}
