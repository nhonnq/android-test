package dev.nhonnq.data

import dev.nhonnq.data.entities.UserData
import dev.nhonnq.data.entities.toDomain
import dev.nhonnq.data.repository.user.UserDataSource
import dev.nhonnq.data.repository.user.UserRemoteMediator
import dev.nhonnq.data.repository.user.UserRepositoryImpl
import dev.nhonnq.domain.util.Result
import dev.nhonnq.test.base.BaseTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class UserRepositoryImplTest: BaseTest() {

    private lateinit var remote: UserDataSource.Remote
    private lateinit var local: UserDataSource.Local
    private lateinit var remoteMediator: UserRemoteMediator
    private lateinit var userRepository: UserRepositoryImpl

    @Before
    fun setUp() {
        remote = mock()
        local = mock()
        remoteMediator = mock()
        userRepository = UserRepositoryImpl(remote, local, remoteMediator)
    }

    @Test
    fun `getUserDetails should return UserEntity from remote`() = runUnconfinedTest {
        val userData = UserData(login = "test", avatarUrl = null, htmlUrl = null)
        `when`(remote.fetchUserDetails("test")).thenReturn(Result.Success(userData.toDomain()))
        `when`(local.getUserDetails("test")).thenReturn(Result.Success(userData))

        val result = userRepository.getUserDetails("test")

        assert(result is Result.Success)
        assertEquals(userData.login, (result as Result.Success).data.login)
    }

    @Test
    fun `getUserDetails should return UserEntity from local when remote fails`() = runBlocking {
        val userData = UserData(login = "test", avatarUrl = null, htmlUrl = null)
        `when`(remote.fetchUserDetails("test")).thenReturn(Result.Error(Exception()))
        `when`(local.getUserDetails("test")).thenReturn(Result.Success(userData))

        val result = userRepository.getUserDetails("test")

        assert(result is Result.Success)
        assertEquals((result as Result.Success).data.login, userData.login)
    }
}
