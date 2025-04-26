package dev.nhonnq.data.repository.user

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import dev.nhonnq.data.entities.toDomain
import dev.nhonnq.domain.entities.UserEntity
import dev.nhonnq.domain.repository.UserRepository
import dev.nhonnq.domain.util.Result
import dev.nhonnq.domain.util.Result.Error
import dev.nhonnq.domain.util.Result.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import dev.nhonnq.domain.util.map

/**
 * Implementation of [UserRepository] that coordinates between remote and local data sources,
 * and manages pagination using [UserRemoteMediator].
 */
class UserRepositoryImpl(
    private val remote: UserDataSource.Remote,
    private val local: UserDataSource.Local,
    private val remoteMediator: UserRemoteMediator
) : UserRepository {

    /**
     * Returns a [Flow] of paginated users.
     *
     * Fetches users from the local database first, while using [remoteMediator] to sync data with the remote source.
     *
     * @param since The starting user ID (ignored when using [RemoteMediator]).
     * @param perPage Number of users to load per page.
     * @return A [Flow] of [PagingData] containing [UserEntity].
     */
    @OptIn(ExperimentalPagingApi::class)
    override fun getUsers(since: Int, perPage: Int): Flow<PagingData<UserEntity>> = Pager(
        config = PagingConfig(pageSize = perPage),
        remoteMediator = remoteMediator,
        pagingSourceFactory = { local.getUsers() }
    ).flow.map { pagingData -> pagingData.map { it.toDomain() } }

    /**
     * Retrieves detailed information for a user.
     *
     * First tries to fetch from the remote source. If successful, updates the local database with new details.
     * If the remote fetch fails, falls back to the local cached data.
     *
     * @param loginUserName The login username of the user.
     * @return [Result.Success] with updated [UserEntity] or [Result.Error] if not available.
     */
    override suspend fun getUserDetails(loginUserName: String): Result<UserEntity> {
        return when (val result = remote.fetchUserDetails(loginUserName)) {
            is Success -> {
                result.map { userDetails ->
                    local.getUserDetails(loginUserName).map { user ->
                        user.location = userDetails.location
                        user.followers = userDetails.followers
                        user.following = userDetails.following
                        user.blog = userDetails.blog
                        local.updateUser(user)
                    }
                    userDetails
                }
            }
            is Error -> {
                local.getUserDetails(loginUserName).map { it.toDomain() }
            }
        }
    }
}

