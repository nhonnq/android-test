package dev.nhonnq.domain

import androidx.paging.PagingData
import dev.nhonnq.domain.entities.UserEntity
import dev.nhonnq.domain.repository.UserRepository
import dev.nhonnq.domain.usecase.FetchUserList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class FetchUserListTest {

    @Mock
    private lateinit var userRepository: UserRepository
    private lateinit var fetchUserList: FetchUserList

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        fetchUserList = FetchUserList(userRepository)
    }

    @Test
    fun `invoke should return user list`() = runBlocking {
        val perPage = 20
        val userList = listOf(
            UserEntity(
                login = "testUser1",
                avatarUrl = "avatarUrl1",
                htmlUrl = "htmlUrl1"
            ),
            UserEntity(
                login = "testUser2",
                avatarUrl = "avatarUrl2",
                htmlUrl = "htmlUrl2"
            )
        )
        val pagingData = PagingData.from(userList)
        val flow: Flow<PagingData<UserEntity>> = flowOf(pagingData)
        Mockito.`when`(userRepository.getUsers(0, perPage)).thenReturn(flow)

        val result = fetchUserList(perPage)

        assertEquals(flow, result)
    }
}
