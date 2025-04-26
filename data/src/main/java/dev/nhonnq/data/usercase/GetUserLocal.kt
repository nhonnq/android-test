package dev.nhonnq.data.usercase

import dev.nhonnq.data.entities.UserData
import dev.nhonnq.data.repository.user.UserDataSource
import dev.nhonnq.domain.util.Result

/**
 * `GetUserLocal` is a Use Case class responsible for retrieving user data from a local data source.
 *
 * This class encapsulates the logic for fetching user details based on a given username from a local data source.
 * It interacts with the `UserDataSource.Local` interface to perform the data retrieval operation.
 *
 * @property local An instance of `UserDataSource.Local` providing access to the local data storage.
 */
class GetUserLocal(
    private val local: UserDataSource.Local,
) {
    suspend operator fun invoke(loginUserName: String): Result<UserData> = local.getUserDetails(loginUserName = loginUserName)
}
