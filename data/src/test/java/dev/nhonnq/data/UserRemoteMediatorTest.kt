package dev.nhonnq.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import dev.nhonnq.data.entities.UserData
import dev.nhonnq.data.mapper.toDb
import dev.nhonnq.data.repository.user.UserDataSource
import dev.nhonnq.data.repository.user.UserRemoteMediator
import dev.nhonnq.domain.entities.UserEntity
import dev.nhonnq.domain.util.Result
import dev.nhonnq.test.base.BaseTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.MockedStatic
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.mockStatic
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.verify

@OptIn(ExperimentalPagingApi::class)
class UserRemoteMediatorTest: BaseTest() {

    private lateinit var local: UserDataSource.Local
    private lateinit var remote: UserDataSource.Remote
    private lateinit var remoteMediator: UserRemoteMediator
    private lateinit var logMock: MockedStatic<Log>

    @Before
    fun setup() {
        logMock = mockStatic(Log::class.java).apply {
            `when`<Int> { Log.d(anyString(), anyString()) }.thenReturn(0)
        }
        local = mock()
        remote = mock()
        remoteMediator = UserRemoteMediator(local, remote)
    }

    @After
    fun tearDown() {
        logMock.close()
    }

    @Test
    fun testLoad() = runUnconfinedTest {
        val userList: List<UserEntity> = listOf(
            UserEntity("user1", "avatar1", "html1", "location1", 1, 1, "blog1"),
            UserEntity("user2", "avatar2", "html2", "location2", 2, 2, "blog2")
        )

        `when`(remote.fetchUsers(0, 20)).thenReturn(Result.Success(userList))

        val pagingState = PagingState<Int, UserData>(
            listOf(),
            null,
            PagingConfig(20),
            20
        )

        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertEquals(false, (result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
        verify(local).clearUsers()
        verify(local).clearRemoteKeys()
        verify(local).saveUsers(userList.map { it.toDb() })
        verify(local).saveRemoteKey(any())
    }
}
