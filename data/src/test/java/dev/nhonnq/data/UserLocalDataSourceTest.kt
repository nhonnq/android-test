package dev.nhonnq.data

import androidx.paging.PagingSource
import dev.nhonnq.test.base.BaseTest
import dev.nhonnq.data.db.user.UserDao
import dev.nhonnq.data.db.user.UserRemoteKeyDao
import dev.nhonnq.data.entities.UserData
import dev.nhonnq.data.entities.UserRemoteKeyDbData
import dev.nhonnq.data.exception.DataNotAvailableException
import dev.nhonnq.data.repository.user.UserLocalDataSource
import dev.nhonnq.domain.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class UserLocalDataSourceTest: BaseTest() {

    private lateinit var userDao: UserDao
    private lateinit var remoteKeyDao: UserRemoteKeyDao
    private lateinit var userLocalDataSource: UserLocalDataSource

    @Before
    fun setUp() {
        userDao = mock(UserDao::class.java)
        remoteKeyDao = mock(UserRemoteKeyDao::class.java)
        userLocalDataSource = UserLocalDataSource(userDao, remoteKeyDao)
    }

    @Test
    fun `test users returns correct PagingSource`() {
        val pagingSource: PagingSource<Int, UserData> = mock()
        whenever(userDao.getUsers()).thenReturn(pagingSource)

        val result = userLocalDataSource.getUsers()

        assertEquals(pagingSource, result)
    }

    @Test
    fun testGetUsers() {
        val pagingSource: PagingSource<Int, UserData> = mock()
        `when`(userDao.getUsers()).thenReturn(pagingSource)

        val result = userLocalDataSource.getUsers()

        assertEquals(pagingSource, result)
        verify(userDao).getUsers()
    }

    @Test
    fun testGetUserDetails() = runUnconfinedTest {
        val loginUserName = "testUser"
        val userData = UserData(login = loginUserName, avatarUrl = null, htmlUrl = null)
        `when`(userDao.getUserDetails(loginUserName)).thenReturn(userData)

        val result = userLocalDataSource.getUserDetails(loginUserName)

        assertTrue(result is Result.Success)
        assertEquals(userData, (result as Result.Success).data)
        verify(userDao).getUserDetails(loginUserName)
    }

    @Test
    fun testGetUserDetailsNotFound() = runUnconfinedTest {
        val loginUserName = "testUser"
        `when`(userDao.getUserDetails(loginUserName)).thenReturn(null)

        val result = userLocalDataSource.getUserDetails(loginUserName)

        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).error is DataNotAvailableException)
        verify(userDao).getUserDetails(loginUserName)
    }

    @Test
    fun testSaveUsers() = runUnconfinedTest {
        val users = listOf(UserData(login = "testUser", avatarUrl = null, htmlUrl = null))

        userLocalDataSource.saveUsers(users)

        verify(userDao).saveUsers(users)
    }

    @Test
    fun testUpdateUser() = runUnconfinedTest {
        val user = UserData(login = "testUser", avatarUrl = null, htmlUrl = null)

        userLocalDataSource.updateUser(user)

        verify(userDao).updateUser(user)
    }

    @Test
    fun testGetLastRemoteKey() = runUnconfinedTest {
        val remoteKey = UserRemoteKeyDbData(id = 1, prevPage = null, nextPage = 2)
        `when`(remoteKeyDao.getLastRemoteKey()).thenReturn(remoteKey)

        val result = userLocalDataSource.getLastRemoteKey()

        assertEquals(remoteKey, result)
        verify(remoteKeyDao).getLastRemoteKey()
    }

    @Test
    fun testSaveRemoteKey() = runUnconfinedTest {
        val remoteKey = UserRemoteKeyDbData(id = 1, prevPage = null, nextPage = 2)

        userLocalDataSource.saveRemoteKey(remoteKey)

        verify(remoteKeyDao).saveRemoteKey(remoteKey)
    }

    @Test
    fun testClearUsers() = runUnconfinedTest {
        userLocalDataSource.clearUsers()

        verify(userDao).clearUsers()
    }

    @Test
    fun testClearRemoteKeys() = runUnconfinedTest {
        userLocalDataSource.clearRemoteKeys()

        verify(remoteKeyDao).clearRemoteKeys()
    }
}
