package dev.nhonnq.data.repository.user

import dev.nhonnq.data.api.UserApi
import dev.nhonnq.domain.util.Result
import dev.nhonnq.data.util.safeApiCall
import dev.nhonnq.domain.entities.UserEntity

/**
 * Remote implementation of [UserDataSource.Remote] that fetches data from the [UserApi].
 */
@Suppress("UnusedPrivateMember")
class UserRemoteDataSource(
    private val userApi: UserApi
) : UserDataSource.Remote {

    /**
     * Fetches a list of users from the remote API.
     *
     * @param since The user ID to start fetching from (pagination).
     * @param perPage The number of users to fetch per page.
     * @return [Result.Success] with a list of [UserEntity] on success, or [Result.Error] on failure.
     */
    override suspend fun fetchUsers(since: Int, perPage: Int): Result<List<UserEntity>> = safeApiCall {
        userApi.getUsers(since, perPage)
    }

    /**
     * Fetches the details of a specific user from the remote API.
     *
     * @param loginUserName The login username of the user.
     * @return [Result.Success] with [UserEntity] on success, or [Result.Error] on failure.
     */
    override suspend fun fetchUserDetails(loginUserName: String): Result<UserEntity> = safeApiCall {
        userApi.getUserDetails(loginUserName)
    }
}

