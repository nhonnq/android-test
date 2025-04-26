package dev.nhonnq.domain.repository

import androidx.paging.PagingData
import dev.nhonnq.domain.util.Result
import dev.nhonnq.domain.entities.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for User related operations.
 */
interface UserRepository {
    fun getUsers(since: Int, perPage: Int): Flow<PagingData<UserEntity>>
    suspend fun getUserDetails(loginUserName: String): Result<UserEntity>
}