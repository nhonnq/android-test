package dev.nhonnq.data.repository.user

import androidx.paging.PagingSource
import dev.nhonnq.data.entities.UserData
import dev.nhonnq.data.entities.UserRemoteKeyDbData
import dev.nhonnq.domain.entities.UserEntity
import dev.nhonnq.domain.util.Result

interface UserDataSource {

    interface Remote {
        suspend fun fetchUsers(since: Int, perPage: Int): Result<List<UserEntity>>
        suspend fun fetchUserDetails(loginUserName: String): Result<UserEntity>
    }

    interface Local {
        fun getUsers(): PagingSource<Int, UserData>
        suspend fun getUserDetails(loginUserName: String): Result<UserData>
        suspend fun saveUsers(users: List<UserData>)
        suspend fun updateUser(user: UserData)

        suspend fun getLastRemoteKey(): UserRemoteKeyDbData?
        suspend fun saveRemoteKey(key: UserRemoteKeyDbData)

        suspend fun clearUsers()
        suspend fun clearRemoteKeys()
    }
}