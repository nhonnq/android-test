package dev.nhonnq.data.repository.user

import androidx.paging.PagingSource
import dev.nhonnq.data.db.user.UserDao
import dev.nhonnq.data.db.user.UserRemoteKeyDao
import dev.nhonnq.data.entities.UserData
import dev.nhonnq.data.entities.UserRemoteKeyDbData
import dev.nhonnq.data.exception.DataNotAvailableException
import dev.nhonnq.domain.util.Result

/**
 * Local implementation of [UserDataSource.Local] that interacts with the local database
 * through [UserDao] and [UserRemoteKeyDao].
 */
class UserLocalDataSource(
    private val userDao: UserDao,
    private val remoteKeyDao: UserRemoteKeyDao,
) : UserDataSource.Local {

    /**
     * Returns a [PagingSource] to load users from the local database.
     */
    override fun getUsers(): PagingSource<Int, UserData> = userDao.getUsers()

    /**
     * Fetches user details by the given [loginUserName].
     *
     * @param loginUserName the login name of the user to retrieve.
     * @return [Result.Success] with [UserData] if found, otherwise [Result.Error].
     */
    override suspend fun getUserDetails(loginUserName: String): Result<UserData> {
        return userDao.getUserDetails(loginUserName)?.let {
            Result.Success(it)
        } ?: Result.Error(DataNotAvailableException())
    }

    /**
     * Saves a list of users into the local database.
     *
     * @param users the list of [UserData] to save.
     */
    override suspend fun saveUsers(users: List<UserData>) {
        userDao.saveUsers(users)
    }

    /**
     * Updates a specific user in the local database.
     *
     * @param user the [UserData] object with updated information.
     */
    override suspend fun updateUser(user: UserData) {
        userDao.updateUser(user)
    }

    /**
     * Retrieves the last stored remote key from the local database.
     *
     * @return the last [UserRemoteKeyDbData] or null if not found.
     */
    override suspend fun getLastRemoteKey(): UserRemoteKeyDbData? {
        return remoteKeyDao.getLastRemoteKey()
    }

    /**
     * Saves a remote key into the local database.
     *
     * @param key the [UserRemoteKeyDbData] to be saved.
     */
    override suspend fun saveRemoteKey(key: UserRemoteKeyDbData) {
        remoteKeyDao.saveRemoteKey(key)
    }

    /**
     * Clears all user data from the local database.
     */
    override suspend fun clearUsers() {
        userDao.clearUsers()
    }

    /**
     * Clears all remote keys from the local database.
     */
    override suspend fun clearRemoteKeys() {
        remoteKeyDao.clearRemoteKeys()
    }
}
