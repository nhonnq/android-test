package dev.nhonnq.data.db.user

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.nhonnq.data.entities.UserData

@Dao
interface UserDao {

    /**
     * Get all users from the users table.
     *
     * @return all users.
     */
    @Query("SELECT * FROM users")
    fun getUsers(): PagingSource<Int, UserData>

    /**
     * Get user by login user name.
     * **/
    @Query("SELECT * FROM users WHERE login = :login")
    suspend fun getUserDetails(login: String): UserData?

    /**
     * Insert all users.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUsers(users: List<UserData>)

    /**
     * Update user.
     */
    @Update
    suspend fun updateUser(user: UserData)

    /**
     * Delete all users.
     */
    @Query("DELETE FROM users")
    suspend fun clearUsers()
}