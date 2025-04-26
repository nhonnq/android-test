package dev.nhonnq.domain

import dev.nhonnq.domain.entities.UserEntity
import dev.nhonnq.domain.repository.UserRepository
import dev.nhonnq.domain.usecase.FetchUserDetails
import dev.nhonnq.domain.util.Result
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class FetchUserDetailsTest {

    @Mock
    private lateinit var userRepository: UserRepository
    private lateinit var fetchUserDetails: FetchUserDetails

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        fetchUserDetails = FetchUserDetails(userRepository)
    }

    @Test
    fun `invoke should return user details when repository returns success`() = runBlocking {
        val loginUserName = "testUser"
        val userEntity = UserEntity(
            login = loginUserName,
            avatarUrl = "avatarUrl",
            htmlUrl = "htmlUrl",
            location = "location",
            followers = 10,
            following = 5,
            blog = "blog"
        )
        Mockito.`when`(userRepository.getUserDetails(loginUserName)).thenReturn(Result.Success(userEntity))

        val result = fetchUserDetails(loginUserName)

        assertEquals(Result.Success(userEntity), result)
    }

    @Test
    fun `invoke should return error when repository returns error`() = runBlocking {
        val loginUserName = "testUser"
        val exception = Exception("Error")
        Mockito.`when`(userRepository.getUserDetails(loginUserName)).thenReturn(Result.Error(exception))

        val result = fetchUserDetails(loginUserName)

        assertEquals(Result.Error<UserEntity>(exception), result)
    }
}
